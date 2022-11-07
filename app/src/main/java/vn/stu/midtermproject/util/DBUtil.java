package vn.stu.midtermproject.util;

import android.app.Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DBUtil {
    public static void copyDBFileFromAssets(Activity context, String dbName, String pathSuffix) {
        File dbFile = context.getDatabasePath(dbName);
        if (dbFile.exists()) {
            return;
        }

        File dbDir = new File(context.getApplicationInfo().dataDir + pathSuffix);
        if (!dbDir.exists()) {
            dbDir.mkdir();
        }

        try {
            InputStream is = context.getAssets().open(dbName);
            String outputFilePath = context.getApplicationInfo().dataDir + pathSuffix + dbName;
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
}
