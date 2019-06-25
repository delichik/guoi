package moe.imiku.guoi;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import moe.imiku.guoi.Model.Cookie;
import moe.imiku.guoi.Model.User;
import moe.imiku.guoi.Util.DebugUtil;
import moe.imiku.guoi.Util.DoubleUtil;
import moe.imiku.guoi.Util.NetResourceUtil;
import moe.imiku.guoi.listener.GlobalValueChangedListener;

public class AccountManager {

    public static final int USER_DATA_UPDATE_ERR_NO_ENOUGH_MONEY = 0;
    public static final int USER_DATA_UPDATE_ERR_NOT_LOGINED_YET = 1;
    public static final int USER_DATA_UPDATE_OK = 2;

    private static GlobalValueChangedListener listener = null;

    private static User user_now_logined;
    private static Cookie cookie = new Cookie();

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static boolean isLogined () {
        return user_now_logined != null;
    }

    public static boolean logout () {
        user_now_logined = null;
        cookie.removeAll();
        sendLoginStatusChangedMessage();
        NetResourceUtil.getURLText(Config.BASE_USER_URL + "?order=1");
        return true;
    }

    public static String getAvatar () {
        if (user_now_logined == null)
            return User.DEFAULT_AVATAR;
        return user_now_logined.getAvatar();
    }

    public static String getName() {
        if (user_now_logined == null)
            return User.DEFAULT_NAME;
        return user_now_logined.getName();
    }

    public static void login (String username, String password) {
        String[] result = NetResourceUtil.getURLTextAndCookies(Config.BASE_USER_URL
                + "?order=0"
                + "&name=" + username
                + "&password=" + password);
        cookie.parseCookie(result[1]);
        DebugUtil.printLog(result);
        if (DoubleUtil.getDouble(result[0]) > 0) {
            String info = NetResourceUtil.getURLText(Config.BASE_USER_URL + "?order=3");

            String[] t = info.split("\n");
            DebugUtil.printLog(cookie.getCookie());
            DebugUtil.printLog(t);
            user_now_logined = new User();
            user_now_logined.setName(username);
            user_now_logined.setMoney(DoubleUtil.getDouble(t[0]));
            user_now_logined.setAvatar(t[1]);
            sendLoginStatusChangedMessage();
        }
    }

    public static String getCookie() {
        System.out.println(cookie.getCookie());
        return cookie.getCookie();
    }

    private static boolean updateMoney(double money) {
        if (!isLogined()) return false;
        String info = NetResourceUtil.getURLText(Config.BASE_USER_URL + "?order=4&money=" + money);
        if (DoubleUtil.getDouble(info) > 0) {
            user_now_logined.setMoney(DoubleUtil.getDouble(info));
            sendChangedMessage();
            return true;
        }
        return false;
    }

    public static double getMoney() {
        if (!isLogined()) return -1;
        return user_now_logined.getMoney();
    }

    public static void setListener (GlobalValueChangedListener listener) {
        AccountManager.listener = listener;
    }

    public static void pay (double money, @NonNull GlobalValueChangedListener tlistener) {
        new Thread(() -> {
            if (!isLogined()) {
                handler.post(() -> tlistener.onChanged(USER_DATA_UPDATE_ERR_NOT_LOGINED_YET));
                return;
            }
            if (user_now_logined.getMoney() < money) {
                handler.post(() -> tlistener.onChanged(USER_DATA_UPDATE_ERR_NO_ENOUGH_MONEY));
                return;
            }
            updateMoney(-money);
            handler.post(() -> tlistener.onChanged(USER_DATA_UPDATE_OK));
        }).start();
    }

    public static void saveMoney (double money, @NonNull GlobalValueChangedListener tlistener) {
        new Thread(() -> {
            if (!isLogined()) {
                handler.post(() -> tlistener.onChanged(USER_DATA_UPDATE_ERR_NOT_LOGINED_YET));
                return;
            }
            if (updateMoney(money))
                handler.post(() -> tlistener.onChanged(USER_DATA_UPDATE_OK));
        }).start();

    }

    private static void sendChangedMessage () {
        if (listener != null)
            listener.onChanged("money");
    }

    private static void sendLoginStatusChangedMessage () {
        if (listener != null)
            listener.onChanged("login");
    }

}
