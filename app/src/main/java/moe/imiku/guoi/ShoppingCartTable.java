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
                    MessageTable.sendMessage(context,
                            item.getFruit().getName()
                                    + " x "
                                    + item.getCount()
                                    + "已添加到购物车");
                    return;
                }
            }
            cart_list.add(item.clone());
            MessageTable.sendMessage(context,
                    item.getFruit().getName()
                            + " x "
                            + item.getCount()
                            + "已添加到购物车");
            sendChangedMessage();
        });
    }

    public static void removeCart (int index) {
        cart_list.remove(index);
        sendChangedMessage();
    }


    public static void removeAllCart () {
        cart_list.clear();
        sendChangedMessage();
    }

    public static void removeAllCartNoCallback () {
        cart_list.clear();
    }

    public static void minCart (int index) {
        cart_list.get(index).setCount(cart_list.get(index).getCount() - 1);
        sendChangedMessage();
    }

    public static void addToCartNoCallback (int index) {
        cart_list.get(index).setCount(cart_list.get(index).getCount() + 1);
    }

    public static void minCartNoCallback (int index) {
        cart_list.get(index).setCount(cart_list.get(index).getCount() - 1);
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

    public static boolean payAll() {
        double totalPrice = 0;
        for (int i = 0; i < cart_list.size(); i++) {
            totalPrice += ShoppingCartTable.getCart(i).getTotalPrice();
        }
        boolean r = CurrentUser.pay(totalPrice);
        if (r) {
            cart_list.clear();
            sendChangedMessage();
        }
        return r;
    }
}
