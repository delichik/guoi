package moe.imiku.guoi;

import moe.imiku.guoi.listener.GlobalValueChangedListener;

public class CurrentUser {
    private static GlobalValueChangedListener listener = null;
    private static String name;
    private static String head_img;
    private static double money = 1000;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        CurrentUser.name = name;
    }

    public static String getHead_img() {
        return head_img;
    }

    public static void setHead_img(String head_img) {
        CurrentUser.head_img = head_img;
    }

    public static double getMoney() {
        return money;
    }

    public static void setMoney(double money) {
        CurrentUser.money = money;
        sendChangedMessage();
    }

    public static void setListener (GlobalValueChangedListener listener) {
        CurrentUser.listener = listener;
    }

    public static boolean pay (double money) {
        if (CurrentUser.money < money)
            return false;
        CurrentUser.money -= money;
        sendChangedMessage();
        return true;
    }

    public static void saveMoney (double money) {
        CurrentUser.money += money;
        sendChangedMessage();
    }

    private static void sendChangedMessage () {
        if (listener != null)
            listener.onChanged(null);
    }
}
