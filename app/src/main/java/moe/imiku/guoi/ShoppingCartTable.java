package moe.imiku.guoi;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

import moe.imiku.guoi.Model.CartItem;
import moe.imiku.guoi.Util.ThreadPool;
import moe.imiku.guoi.listener.GlobalValueChangedListener;

public class ShoppingCartTable {

    private static GlobalValueChangedListener listener = null;

    private static ArrayList<CartItem> cart_list = new ArrayList<>();
    private static ThreadPool threadPool = new ThreadPool(Config.THREAD_COUNT_FAST);
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void addToCart (CartItem item, Context context) {
        threadPool.addNewThread(() -> {
            for (int i = 0; i < ShoppingCartTable.getCartSize(); i++) {
                if (ShoppingCartTable.getCart(i).getFruit().getId().equals(item.getFruit().getId())) {
                    cart_list.get(i).setCount(cart_list.get(i).getCount() + item.getCount());
                    MessageTable.sendMessageAndToast(context,
                            item.getFruit().getName()
                                    + " x "
                                    + item.getCount()
                                    + "已添加到购物车");
                    sendChangedMessage();
                    return;
                }
            }
            cart_list.add(item.clone());
            MessageTable.sendMessageAndToast(context,
                    item.getFruit().getName()
                            + " x "
                            + item.getCount()
                            + "已添加到购物车");
            sendChangedMessage();
        });
    }

    public static void removeAllCart () {
        cart_list.clear();
        sendChangedMessage();
    }

    public static CartItem getCart (int index) {
        return cart_list.get(index);
    }

    public static int getCartSize () {
        return cart_list.size();
    }

    public static void setListener (GlobalValueChangedListener listener) {
        ShoppingCartTable.listener = listener;
    }

    private static void sendChangedMessage () {
        handler.post(() -> {
            if (listener != null)
                listener.onChanged(null);
        });
    }

    public static void removeEmpty() {
        for (CartItem item : cart_list) {
            if (item.getCount() == 0)
                cart_list.remove(item);
        }
    }

    public static void payAll(Context context) {
        if (!AccountManager.isLogined()) {
            MessageTable.sendToast(context, R.string.please_login_first);
            return;
        }
        double totalPrice = 0;
        for (int i = 0; i < cart_list.size(); i++) {
            totalPrice += ShoppingCartTable.getCart(i).getTotalPrice();
        }

        AccountManager.pay(totalPrice, (code) -> {
            switch ((int)code){
                case AccountManager.USER_DATA_UPDATE_OK:
                    cart_list.clear();
                    sendChangedMessage();
                    break;
                case AccountManager.USER_DATA_UPDATE_ERR_NO_ENOUGH_MONEY:
                    MessageTable.sendToast(context, R.string.no_enough_money);
                    break;
                case AccountManager.USER_DATA_UPDATE_ERR_NOT_LOGINED_YET:
                    MessageTable.sendToast(context, R.string.please_login_first);
                    break;
            }
        });
    }
}
