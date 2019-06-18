package moe.imiku.guoi;

import java.util.ArrayList;

import moe.imiku.guoi.Model.CartItem;

public class ShoppingCartTable {

    private static ArrayList<CartItem> cart_list = new ArrayList<>();

    public static void addNewToCart (CartItem item) {
        cart_list.add(item);
    }

    public static void removeCart (int index) {
        cart_list.remove(index);
    }

    public static void addToCart (int index) {
        cart_list.get(index).setCount(cart_list.get(index).getCount() + 1);
    }

    public static void minCart (int index) {
        cart_list.get(index).setCount(cart_list.get(index).getCount() - 1);
    }

    public static CartItem getCart (int index) {
        return cart_list.get(index);
    }

    public static int getCartSize () {
        return cart_list.size();
    }

}
