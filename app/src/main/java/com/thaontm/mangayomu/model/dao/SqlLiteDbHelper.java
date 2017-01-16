package com.thaontm.mangayomu.model.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thaontm.mangayomu.model.bean.MangaOverview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thao on 1/14/2017.
 * Copyright thao 2017.
 */

public class SqlLiteDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mangakiss.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context mContext;

    public SqlLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public SqlLiteDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public List<MangaOverview> GetMangas() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MangaOverview> mMangas = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM manga where type = 1", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            MangaOverview mangaOverview = new MangaOverview();
            mangaOverview.setId(cursor.getInt(0));
            mangaOverview.setName(cursor.getString(1));
            mangaOverview.setMDescription(cursor.getString(2));
            mangaOverview.setMState(cursor.getString(4));
            //mangaOverview.setGenres(cursor.getString(5));
            mangaOverview.setPreviewImageUrl("http://ibdp.huluim.com/video/12816667?size=320x180");
            mMangas.add(mangaOverview);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return mMangas;
    }

    private static String getDatabasePath() {
        return mContext.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset() throws IOException {

        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

        String outFileName = getDatabasePath();

        File f = new File(mContext.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = mContext.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
