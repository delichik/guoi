package moe.imiku.guoi.Activity;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;

import moe.imiku.guoi.MessageTable;
import moe.imiku.guoi.Model.CartItem;
import moe.imiku.guoi.Model.Fruit;
import moe.imiku.guoi.R;
import moe.imiku.guoi.ShoppingCartTable;

import static moe.imiku.guoi.Util.FileUtil.getBitmapFromAsset;

public class DetailActivity extends Activity {
    private Fruit fruit;
    private String[] images;
    private CartItem item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        fruit = getIntent().getParcelableExtra("fruit");
        initImages();
        init();
    }

    public void init () {
        item = new CartItem();
        item.setCount(1);
        item.setFruit(fruit);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(getBitmapFromAsset(getAssets(),fruit.getImage()));
        TextView textView_name = findViewById(R.id.name);
        textView_name.setText(fruit.getName());
        TextView textView_money = findViewById(R.id.money);
        textView_money.setText(String.format("￥%.2f",fruit.getPrice()));
        LinearLayout linearLayout = findViewById(R.id.image_field);
        TextView textView_price = findViewById(R.id.price);
        textView_price.setText(String.format("￥%.2f",item.getTotalPrice()));
        TextView textView_count = findViewById(R.id.count);

        for (String image : images){
            ImageView imageView1 = new ImageView(this);
            imageView1.setImageBitmap(getBitmapFromAsset(getAssets(),image));
            linearLayout.addView(imageView1);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView1.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            imageView1.setLayoutParams(params);
        }

        findViewById(R.id.min).setOnClickListener(v -> {
            if (item.getCount() <= 0)
                return;
            item.setCount(item.getCount() - 1);
            textView_price.setText(String.format("￥%.2f",item.getTotalPrice()));
            textView_count.setText(item.getCount() + "");
        });

        findViewById(R.id.add).setOnClickListener(v -> {
            if (item.getCount() >= 99)
                return;
            item.setCount(item.getCount() + 1);

            textView_price.setText(String.format("￥%.2f",item.getTotalPrice()));
            textView_count.setText(item.getCount() + "");
        });

        findViewById(R.id.addcar).setOnClickListener(v -> {
            for (int i = 0;i < ShoppingCartTable.getCartSize();i++){
                if (ShoppingCartTable.getCart(i).getFruit().getId().equals(fruit.getId())) {
                    ShoppingCartTable.addToCart(i, item.getCount());
                    MessageTable.sendMessage(DetailActivity.this,
                            item.getFruit().getName()
                                    + " x "
                                    + item.getCount()
                                    + "已添加到购物车");
                    return;
                }
            }
            ShoppingCartTable.addNewToCart(item);
            MessageTable.sendMessage(DetailActivity.this,
                    item.getFruit().getName()
                            + " x "
                            + item.getCount()
                            + "已添加到购物车");
        });
    }


    public void initImages () {
        AssetManager am = getAssets();
        ArrayList<String> list = new ArrayList<>();
        String base = fruit.getImage();
        for (int i = 1; true; i++) {
            try {
                String t = base.replaceAll("_p[0-9].jpg", "_p" + i + ".jpg");
                am.open(t);
                list.add(t);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        images = list.toArray(new String[]{});
    }
}
