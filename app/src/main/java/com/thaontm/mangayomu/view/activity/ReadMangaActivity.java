package com.thaontm.mangayomu.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.thaontm.mangayomu.R;

public class ReadMangaActivity extends AppCompatActivity {
    private TextRecognizer detector;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_manga);
        ImageView imageView = (ImageView) findViewById(R.id.imange_page);
        imageView.setImageResource(R.drawable.page2);
        detector = new TextRecognizer.Builder(getApplicationContext()).build();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.page2);
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> textBlocks = detector.detect(frame);
        String blocks = "";
        for (int index = 0; index < textBlocks.size(); index++) {
            TextBlock tBlock = textBlocks.valueAt(index);
            blocks = blocks + tBlock.getValue() + "\n" + "\n";
        }
        Toast.makeText(this, blocks.toString(), Toast.LENGTH_SHORT).show();
    }
}
