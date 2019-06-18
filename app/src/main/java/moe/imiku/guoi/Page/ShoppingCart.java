package moe.imiku.guoi.Page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import moe.imiku.guoi.Model.CartItem;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

import static moe.imiku.guoi.ShoppingCartTable.getCart;
import static moe.imiku.guoi.ShoppingCartTable.getCartSize;
import static moe.imiku.guoi.ShoppingCartTable.removeCart;
import static moe.imiku.guoi.Util.FileUtil.getBitmapFromAsset;

public class ShoppingCart extends PageLoader {
    ArrayList<View> view_list;

    protected ShoppingCart(Context context) {
        super(context, R.layout.page_shoppign_card);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected PageLoader subLoad() {
        view_list = new ArrayList<>();
        LinearLayout cart_field = findViewById(R.id.cart_field);
        refreshPrice();
        for (int i = 0; i < getCartSize(); i++) {
            final CartItem item = getCart(i);
            if (item.getCount() <= 0) {
                removeCart(i--);
                continue;
            }
            View view = View.inflate(context, R.layout.item_shopping_cart, null);
            TextView text = view.findViewById(R.id.name);
            text.setText(item.getFruit().getName());
            text = view.findViewById(R.id.price);
            text.setText(String.format("%.2g元/Kg", item.getFruit().getPrice()));
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
            cart_field.addView(view);
        }
        refreshPrice();

        return this;
    }

    @Override
    protected PageLoader subUnLoad() {
        return this;
    }

    @SuppressLint("DefaultLocale")
    private void refreshPrice () {
        double totalPrice = 0;
        for (int i = 0; i < view_list.size(); i++) {
            View view = view_list.get(i);
            ((TextView) view.findViewById(R.id.count))
                    .setText(String.valueOf(getCart(i).getCount()));
            totalPrice += getCart(i).getTotalPrice();
        }
        ((TextView)findViewById(R.id.price_all)).setText(String.format("总价：%.2g元", totalPrice));
    }
}
