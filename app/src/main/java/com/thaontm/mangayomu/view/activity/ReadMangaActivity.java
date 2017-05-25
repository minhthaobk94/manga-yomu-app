package com.thaontm.mangayomu.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.TabSelectionInterceptor;
import com.squareup.picasso.Picasso;
import com.thaontm.mangayomu.R;
import com.thaontm.mangayomu.model.bean.ChapterImage;
import com.thaontm.mangayomu.model.bean.MangaChapter;
import com.thaontm.mangayomu.model.bean.TouchInfo;
import com.thaontm.mangayomu.model.bean.translation.Translation;
import com.thaontm.mangayomu.model.bean.translation.TranslationResponse;
import com.thaontm.mangayomu.model.provider.Callback;
import com.thaontm.mangayomu.model.provider.KakalotMangaProvider;
import com.thaontm.mangayomu.rest.ApiClient;
import com.thaontm.mangayomu.rest.ApiInterface;
import com.thaontm.mangayomu.utils.BusyIndicatorManager;
import com.thaontm.mangayomu.utils.StringUtils;
import com.thaontm.mangayomu.view.fragment.MangaChapterFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.thaontm.mangayomu.view.activity.MangaDetailActivity.CHAPTER;

public class ReadMangaActivity extends AppCompatActivity implements MangaChapterFragment.OnListFragmentInteractionListener, TabSelectionInterceptor {
    @BindView(R.id.image_page)
    ImageView mImageView;
    @BindView(R.id.llReadManga)
    LinearLayout llReadManga;
    Snackbar snackbar;

    private BusyIndicatorManager mBusyIndicatorManager;

    boolean isActionDown = false, isActionUp = false;
    float x1 = 0, y1 = 0, x2 = 0, y2 = 0;
    private TextRecognizer detector;
    private BottomBar bottomNavigationView;
    private List<ChapterImage> chapterImages;
    private ChapterImage chapterImage;
    private KakalotMangaProvider kakalotMangaProvider;
    private int currentIndex = 0;
    private SparseArray<TextBlock> recognizedTextBlocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);
        ButterKnife.bind(this);

        mBusyIndicatorManager = new BusyIndicatorManager(this);

        MangaChapter mangaChapter = (MangaChapter) getIntent().getSerializableExtra(CHAPTER);
        kakalotMangaProvider = new KakalotMangaProvider();
        // show busy indicator
        mBusyIndicatorManager.showBusyIndicator();
        kakalotMangaProvider.getMangaChapterImages(mangaChapter.getBaseUrl(), new Callback<List<ChapterImage>>() {
            @Override
            public void onSuccess(final List<ChapterImage> result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chapterImages = result;
                        chapterImage = chapterImages.get(currentIndex);
                        Picasso.with(ReadMangaActivity.this).load(chapterImage.getBaseUrl()).fit().into(mImageView);
                        // hide busy indicator
                        mBusyIndicatorManager.hideBusyIndicator();

                    }
                });
            }

            @Override
            public void onError(Throwable what) {
                // hide busyIndicator
                mBusyIndicatorManager.hideBusyIndicator();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.NETWORK_ERR_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        bottomNavigationView = (BottomBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationView.setTabSelectionInterceptor(this);

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isActionDown = true;
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        isActionDown = false;
                        isActionUp = true;
                        x2 = event.getX();
                        y2 = event.getY();
                        break;
                }

                if (isActionUp) {
                    // get recognized text blocks
                    recognizedTextBlocks = getRecognizedTextBlocks();
                    SparseArray<TextBlock> touchedTextBlock = getTouchedTextBlocks(new TouchInfo(x1, y1, x2, y2));
                    final StringBuilder selectedText = new StringBuilder();
                    for (int i = 0; i < touchedTextBlock.size(); i++) {
                        selectedText.append(touchedTextBlock.get(i).getValue());
                    }
                    // translate
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                translate(StringUtils.removeLineBreaks(selectedText.toString()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    isActionUp = false;
                    x1 = y1 = x2 = y2 = 0.0f;
                }
                return true;
            }
        });

        // set up snackbar
        snackbar = Snackbar.make(llReadManga, "Text", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.white));
        snackbar.setAction(R.string.close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.snackbar_background));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(5);
        textView.setTextColor(getResources().getColor(R.color.primary));
    }

    @Override
    public void onListFragmentInteraction(MangaChapter item) {
    }

    @Override
    public boolean shouldInterceptTabSelection(@IdRes int oldTabId, @IdRes int newTabId) {
        if (newTabId == R.id.action_previous) {
            // close Snackbar
            closeSnackBar();
            // show busy indicator
            mBusyIndicatorManager.showBusyIndicator();
            if (currentIndex > 0) {
                chapterImage = chapterImages.get(--currentIndex);
                Picasso.with(ReadMangaActivity.this).load(chapterImage.getBaseUrl()).fit().into(mImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Loading image success !", Toast.LENGTH_SHORT).show();
                        mBusyIndicatorManager.hideBusyIndicator();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getApplicationContext(), "Loading image error !", Toast.LENGTH_SHORT).show();
                        mBusyIndicatorManager.hideBusyIndicator();
                    }
                });
            } else {
                mBusyIndicatorManager.hideBusyIndicator();
            }
        } else if (newTabId == R.id.action_forward) {
            // close Snackbar
            closeSnackBar();
            // show busy indicator
            mBusyIndicatorManager.showBusyIndicator();
            if (currentIndex < chapterImages.size() - 1) {
                chapterImage = chapterImages.get(++currentIndex);
                Picasso.with(ReadMangaActivity.this).load(chapterImage.getBaseUrl()).fit().into(mImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(), "Loading image success !", Toast.LENGTH_SHORT).show();
                        mBusyIndicatorManager.hideBusyIndicator();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getApplicationContext(), "Loading image error !", Toast.LENGTH_SHORT).show();
                        mBusyIndicatorManager.hideBusyIndicator();

                    }
                });
            } else {
                mBusyIndicatorManager.hideBusyIndicator();
            }
        } else if (newTabId == R.id.action_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return false;
    }

    private void closeSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeSnackBar();
        mBusyIndicatorManager.hideBusyIndicator();
    }

    /*
        * Translate using Google Translate API
        * */
    public void translate(final String input) {
        final String API_KEY = getResources().getString(R.string.API_KEY);
        final String SOURCE = getResources().getString(R.string.SOURCE_LANGUAGE);
        final String TARGET = getResources().getString(R.string.TARGET_LANGUAGE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TranslationResponse> call = apiService.getTranslationResponse(API_KEY, SOURCE, TARGET, input);
        call.enqueue(new retrofit2.Callback<TranslationResponse>() {
            @Override
            public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                Translation translation = response.body().getData();
                if (translation != null) {
                    String result = translation.getTranslations().get(0).getTranslatedText().trim();
                    if (result.length() > 0) {
                        snackbar.setText(result);
                        snackbar.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {

            }
        });
    }

    private SparseArray<TextBlock> getRecognizedTextBlocks() {
        ImageView imageView = (ImageView) findViewById(R.id.image_page);
        detector = new TextRecognizer.Builder(getApplicationContext()).build();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap;
        if (bitmapDrawable != null) {
            bitmap = bitmapDrawable.getBitmap();
        } else {
            return new SparseArray<>();
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        return detector.detect(frame);
    }

    private SparseArray<TextBlock> getTouchedTextBlocks(TouchInfo touchInfo) {
        SparseArray<TextBlock> touchedTextBlocks = new SparseArray<>();

        int i = 0;
        for (int index = 0; index < recognizedTextBlocks.size(); index++) {
            TextBlock tBlock = recognizedTextBlocks.valueAt(index);
            if (tBlock.getBoundingBox().intersect(touchInfo.getRect())) {
                touchedTextBlocks.put(i++, tBlock);
            }
        }
        return touchedTextBlocks;
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