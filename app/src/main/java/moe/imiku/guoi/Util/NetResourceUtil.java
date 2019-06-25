package moe.imiku.guoi.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import moe.imiku.guoi.AccountManager;

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

    public static String getURLText (String url) {
        StringBuilder str = new StringBuilder();
        try {
            URL myurl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);

            conn.setRequestProperty("cookie", AccountManager.getCookie());
            conn.connect();
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            int b;
            while((b = isr.read()) != -1){
                str.append((char)b);
            }
            isr.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public static String[] getURLTextAndCookies (String url) {
        StringBuilder str = new StringBuilder();
        StringBuilder cookies = new StringBuilder();
        try {
            URL myurl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);

            conn.setRequestProperty("cookie", AccountManager.getCookie());
            conn.connect();
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            cookies.append(conn.getHeaderField("Set-Cookie"));
            int b;
            while((b = isr.read()) != -1){
                str.append((char)b);
            }
            isr.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[] {str.toString(), cookies.toString()};
    }
}
