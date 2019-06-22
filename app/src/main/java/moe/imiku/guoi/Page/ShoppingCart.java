package moe.imiku.guoi.Page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import moe.imiku.guoi.MessageTable;
import moe.imiku.guoi.Model.CartItem;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;
import moe.imiku.guoi.ShoppingCartTable;

import static moe.imiku.guoi.Util.FileUtil.getBitmapFromAsset;

public class ShoppingCart extends PageLoader {
    private ArrayList<View> view_list;

    protected ShoppingCart(Context context) {
        super(context, R.layout.page_shoppign_card);
    }

    @Override
    protected PageLoader subLoad() {
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
            View v = findViewById(R.id.cover);
            v.setVisibility(View.VISIBLE);
            findViewById(R.id.pay).setOnClickListener(v13 -> {});
            return;
        }
        else {
            View v = findViewById(R.id.cover);
            v.setVisibility(View.GONE);
            findViewById(R.id.pay).setOnClickListener(v13 -> {
                if (ShoppingCartTable.getCartSize() >= 1
                && ShoppingCartTable.getCart(0).getCount() > 0) {
                    if (ShoppingCartTable.payAll())
                        MessageTable.sendMessage(context, "支付成功");
                    else
                        MessageTable.sendMessage(context, "支付失败，余额不足");
                }
            });
        }

        ShoppingCartTable.removeEmpty();
        LinearLayout cart_field = findViewById(R.id.cart_field);
        cart_field.removeAllViews();
        new Thread(() -> {
            view_list = new ArrayList<>();
            for (int i = 0; i < ShoppingCartTable.getCartSize(); i++) {
                final CartItem item = ShoppingCartTable.getCart(i);
                View view = View.inflate(context, R.layout.item_shopping_cart, null);
                TextView text = view.findViewById(R.id.name);
                text.setText(item.getFruit().getName());
                text = view.findViewById(R.id.price);
                text.setText(String.format("￥%.2f/kg", item.getFruit().getPrice()));
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
                ImageView image = view.findViewById(R.id.image);
                image.setImageBitmap(getBitmapFromAsset(context.getAssets(), item.getFruit().getImage()));
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
                    ((TextView)findViewById(R.id.price_all)).setText(String.format("总价：%.2f元", ftp)));
        }).start();
    }
}
