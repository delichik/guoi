package moe.imiku.guoi;

import java.util.ArrayList;

import moe.imiku.guoi.Model.CartItem;
import moe.imiku.guoi.listener.GlobalValueChangedListener;

public class ShoppingCartTable {

    private static GlobalValueChangedListener listener = null;

    private static ArrayList<CartItem> cart_list = new ArrayList<>();

    public static void addNewToCart (CartItem item) {
        cart_list.add(item.clone());
        sendChangedMessage();
    }

    public static void removeCart (int index) {
        cart_list.remove(index);
        sendChangedMessage();
    }

    public static void addToCart (int index) {
		addToCart(index, 1);
    }
	
	public static void addToCart (int index, int count) {
        cart_list.get(index).setCount(cart_list.get(index).getCount() + count);
        sendChangedMessage();
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
        if (listener != null)
            listener.onChanged(null);
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
