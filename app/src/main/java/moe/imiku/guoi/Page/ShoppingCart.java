package moe.imiku.guoi.Page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import moe.imiku.guoi.Model.CartItem;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;
import moe.imiku.guoi.ShoppingCartTable;

public class ShoppingCart extends PageLoader {

    private LinearLayout cover;
    private TextView pay;
    private TextView clear;
    private LinearLayout cart_field;
    private TextView price_all;

    private ArrayList<View> view_list;

    protected ShoppingCart(Context context) {
        super(context, R.layout.page_shoppign_card);
    }

    @Override
    protected PageLoader subLoad() {
        cover = findViewById(R.id.cover);
        pay = findViewById(R.id.pay);
        clear = findViewById(R.id.clear);
        cart_field = findViewById(R.id.cart_field);
        price_all = findViewById(R.id.price_all);

        refreshView();
        ShoppingCartTable.setListener((obj) -> refreshView());
        return this;
    }

    @Override
    protected PageLoader subUnLoad() {
        return this;
    }

    @SuppressLint("DefaultLocale")
    private void refreshView () {

        if (ShoppingCartTable.getCartSize() == 0) {
            cover.setVisibility(View.VISIBLE);
            pay.setOnClickListener(v13 -> {});
            clear.setOnClickListener(null);
            return;
        }
        else {
            cover.setVisibility(View.GONE);
            pay.setOnClickListener(v13 -> {
                if (ShoppingCartTable.getCartSize() >= 1
                        && ShoppingCartTable.getCart(0).getCount() > 0) {
                    ShoppingCartTable.payAll(context);
                }
            });

            clear.setOnClickListener(v13 -> ShoppingCartTable.removeAllCart());
        }

        ShoppingCartTable.removeEmpty();
        cart_field.removeAllViews();
        new Thread(() -> {
            view_list = new ArrayList<>();
            for (int i = 0; i < ShoppingCartTable.getCartSize(); i++) {
                final CartItem item = ShoppingCartTable.getCart(i);
                View view = View.inflate(context, R.layout.item_shopping_cart, null);
                TextView text = view.findViewById(R.id.name);
                text.setText(item.getFruit().getName());
                text = view.findViewById(R.id.price);
                text.setText(String.format(context.getString(R.string._format_money), item.getFruit().getPrice()));
                text = view.findViewById(R.id.count);
                text.setText(String.valueOf(item.getCount()));
                view.findViewById(R.id.min).setOnClickListener(v -> {
                    if (item.getCount() <= 0)
                        return;
                    item.setCount(item.getCount() - 1);
                    refreshPrice();
                });
                view.findViewById(R.id.add).setOnClickListener(v -> {
                    if (item.getCount() >= 99)
                        return;
                    item.setCount(item.getCount() + 1);
                    refreshPrice();
                });
                loadURLBitmap(item.getFruit().getImage(), view.findViewById(R.id.image));
                view_list.add(view);
                runInMainThread(() -> cart_field.addView(view));
            }
            refreshPrice();
        }).start();

    }

    @SuppressLint("DefaultLocale")
    private void refreshPrice () {
        new Thread(() -> {
            double totalPrice = 0;
            for (int i = 0; i < view_list.size(); i++) {
                View view = view_list.get(i);
                final int fi = i;
                runInMainThread(() ->
                        ((TextView) view.findViewById(R.id.count))
                                .setText(String.valueOf(ShoppingCartTable.getCart(fi).getCount())));
                totalPrice += ShoppingCartTable.getCart(i).getTotalPrice();
            }
            final double ftp = totalPrice;
            runInMainThread(() ->
                    price_all.setText(String.format(context.getString(R.string.price_all) + context.getString(R.string._format_money), ftp)));
        }).start();
    }
}
