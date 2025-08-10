package com.movies.player.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.movies.player.utils.Constants;
import com.movies.player.utils.LogUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes.dex */
public class StealthDBHelper extends SQLiteOpenHelper {
    private static Context mContext;
    private static StealthDBHelper mInstance;

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public StealthDBHelper getInstance() {
        return mInstance;
    }

    public StealthDBHelper(Context context) {
        super(context, Constants.DB_NAME, (SQLiteDatabase.CursorFactory) null, Constants.DB_VERSION);
        Constants.DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        mContext = context;
        dataBaseCheck();
        mInstance = this;
    }

    private void dataBaseCheck() {
        if (new File(Constants.DB_PATH + Constants.DB_NAME).exists()) {
            return;
        }
        File file = new File(Constants.DB_PATH);
        if (file.exists()) {
            for (File file2 : file.listFiles()) {
                file2.delete();
            }
        }
        dbCopy();
    }

    private void dbCopy() {
        try {
            File file = new File(Constants.DB_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            InputStream open = mContext.getAssets().open(Constants.DB_NAME);
            FileOutputStream fileOutputStream = new FileOutputStream(Constants.DB_PATH + Constants.DB_NAME);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    open.close();
                    return;
                }
            }
        } catch (IOException e) {
            LogUtils.uploadLog(e);
        }
    }
}
