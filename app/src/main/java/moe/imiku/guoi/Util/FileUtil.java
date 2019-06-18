package moe.imiku.guoi.Util;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static String getTextFromAssets (AssetManager am, String path) {
        try {
            InputStream is = am.open(path);
            StringBuilder stringBuilder = new StringBuilder();
            byte[] bytes = new byte[1024];
            int length;
            while ((length = is.read(bytes)) != -1) {
                byte[] t = new byte[length];
                System.arraycopy(bytes, 0, t, 0, length);
                stringBuilder.append(new String(t));
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromAsset (AssetManager am, String path) {
        try {
            return BitmapFactory.decodeStream(am.open(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
