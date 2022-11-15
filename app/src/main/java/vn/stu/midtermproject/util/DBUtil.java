package vn.stu.midtermproject.util;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DBUtil extends AppCompatActivity {
    private static String DB_NAME = "doan.db";
    private static String PATH_SUFFIX = "/databases/";

    public static void copyDBFileFromAssets(Activity context) {
        File dbFile = context.getDatabasePath(DB_NAME);
        if (dbFile.exists()) {
            return;
        }

        File dbDir = new File(context.getApplicationInfo().dataDir + PATH_SUFFIX);
        if (!dbDir.exists()) {
            dbDir.mkdir();
        }

        try {
            InputStream is = context.getAssets().open(DB_NAME);
            String outputFilePath = context.getApplicationInfo().dataDir + PATH_SUFFIX + DB_NAME;
            OutputStream os = new FileOutputStream(outputFilePath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            os.flush();
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SQLiteDatabase openOrCreateDataBases(Activity context) {
        return SQLiteDatabase.openOrCreateDatabase(context.getApplicationInfo().dataDir + PATH_SUFFIX + DB_NAME, null);
    }
}
