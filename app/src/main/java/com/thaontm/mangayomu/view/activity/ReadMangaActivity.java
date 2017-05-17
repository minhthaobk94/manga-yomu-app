package com.thaontm.mangayomu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.roughike.bottombar.TabSelectionInterceptor;
import com.squareup.picasso.Picasso;
import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.ChapterImage;
import com.thaontm.mangayomu.model.bean.MangaChapter;
import com.thaontm.mangayomu.model.provider.Callback;
import com.thaontm.mangayomu.model.provider.KakalotMangaProvider;
import com.thaontm.mangayomu.view.fragment.MangaChapterFragment;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.thaontm.mangayomu.view.activity.MangaDetailActivity.CHAPTER;

public class ReadMangaActivity extends AppCompatActivity implements MangaChapterFragment.OnListFragmentInteractionListener, TabSelectionInterceptor {
    private TextRecognizer detector;
    private BottomBar bottomNavigationView;
    private List<ChapterImage> chapterImages;
    private ChapterImage chapterImage;
    private KakalotMangaProvider kakalotMangaProvider;
    private int currentIndex = 0;
    @BindView(R.id.image_page)
    ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);
        ImageView imageView = (ImageView) findViewById(R.id.image_page);
        extractText(imageView);
        ButterKnife.bind(this);

        MangaChapter mangaChapter = (MangaChapter) getIntent().getSerializableExtra(CHAPTER);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) findViewById(R.id.image_page);
                detector = new TextRecognizer.Builder(getApplicationContext()).build();
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<TextBlock> textBlocks = detector.detect(frame);
                String blocks = "";
                for (int index = 0; index < textBlocks.size(); index++) {
                    TextBlock tBlock = textBlocks.valueAt(index);
                    blocks = blocks + tBlock.getValue() + ".";
                }

                if (blocks.endsWith("."))
                    blocks = blocks.substring(0, blocks.length() - 1);

                final String finalBlocks = blocks;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            translate(finalBlocks);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        kakalotMangaProvider = new KakalotMangaProvider();
        kakalotMangaProvider.getMangaChapterImages(mangaChapter.getBaseUrl(), new Callback<List<ChapterImage>>() {
            @Override
            public void onSuccess(final List<ChapterImage> result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chapterImages = result;
                        chapterImage = chapterImages.get(currentIndex);
                        Picasso.with(ReadMangaActivity.this).load(chapterImage.getBaseUrl()).fit().into(mImageView);

                    }
                });
            }

            @Override
            public void onError(Throwable what) {

            }
        });
        bottomNavigationView = (BottomBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setTabSelectionInterceptor(this);
    }


    private void extractText(ImageView imageView) {
//        imageView.setImageResource(R.drawable.page2);
//        detector = new TextRecognizer.Builder(getApplicationContext()).build();
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.page2);
//        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//        SparseArray<TextBlock> textBlocks = detector.detect(frame);
//        String blocks = "";
//        for (int index = 0; index < textBlocks.size(); index++) {
//            TextBlock tBlock = textBlocks.valueAt(index);
//            blocks = blocks + tBlock.getValue() + "\n" + "\n";
//        }
//        Toast.makeText(this, blocks.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListFragmentInteraction(MangaChapter item) {

    }

    @Override
    public boolean shouldInterceptTabSelection(@IdRes int oldTabId, @IdRes int newTabId) {
        if (newTabId == R.id.action_previous) {
            if (currentIndex > 0) {
                chapterImage = chapterImages.get(--currentIndex);
                Picasso.with(ReadMangaActivity.this).load(chapterImage.getBaseUrl()).fit().into(mImageView);

            }
        } else if (newTabId == R.id.action_forward) {
            if (currentIndex < chapterImages.size() - 1) {
                chapterImage = chapterImages.get(++currentIndex);
                Picasso.with(ReadMangaActivity.this).load(chapterImage.getBaseUrl()).fit().into(mImageView);

            }
        } else if (newTabId == R.id.action_home) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
        return false;
    }

    public void translate(final String input) throws IOException {
        Document doc;
        Connection.Response response = Jsoup.connect("https://www.engtoviet.com/translate.php").requestBody("q=" + input)
                .method(Connection.Method.POST).execute();

        doc = response.parse();
        String session = response.cookie("PHPSESSID");

        String all = doc.toString();
        int scriptIndex = all.indexOf("sTs (\"target_text_1\", \"en_vn\",");

        if (scriptIndex < 0) {
            String[] results = doc.getElementsByClass("result_text").text().split(";");
            showResult(results.length > 0 ? results[0] : null);
        }

        String content = all.substring(scriptIndex + "sTs (\"target_text_1\", \"en_vn\",".length(),
                all.indexOf(");", scriptIndex));

        String[] parts = content.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replace("\"", "").trim();
        }

        String url = String.format("https://www.engtoviet.com/smt_translate.api.php?lang=en_vn&q=" + parts[0].replace("==", "")
                + "&var=" + parts[1] + "&key=" + parts[2]);
        doc = Jsoup
                .connect(url)
                .cookie("PHPSESSID", session).get();

        String result = doc.toString();
        scriptIndex = result.indexOf("translatecallback(\"");
        result = result.substring(scriptIndex + "translatecallback(\"".length(), result.indexOf("\")"));

        showResult(result);
    }

    private void showResult(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (result != null) {
                    Toast.makeText(ReadMangaActivity.this, result, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReadMangaActivity.this, "Cannot translate", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
