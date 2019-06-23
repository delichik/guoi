package moe.imiku.guoi.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class NetResourceUtil {
    public static Bitmap getURLimage(String url) {
        Bitmap bmp;
        try {
            URL myurl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);
            conn.setUseCaches(true);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                is.close();
            }
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bmp;
    }
}
