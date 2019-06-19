package moe.imiku.guoi.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

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
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(getBitmapFromAsset(getAssets(),fruit.getImage()));
        TextView textView_name = findViewById(R.id.name);
        textView_name.setText(fruit.getName());
        TextView textView_money = findViewById(R.id.money);
        textView_money.setText(String.format("￥%.2f",fruit.getPrice()));
        LinearLayout linearLayout = findViewById(R.id.image_field);
        TextView textView_price = findViewById(R.id.price);
        textView_price.setText(String.format("￥%.2f",item.getTotalPrice()));
        item = new CartItem();
        item.setCount(1);
        item.setFruit(fruit);
        for (String image : images){
            ImageView imageView1 = new ImageView(this);
            imageView1.setImageBitmap(getBitmapFromAsset(getAssets(),image));
            linearLayout.addView(imageView1);
        }
        findViewById(R.id.min).setOnClickListener(v -> {
            if (item.getCount() <= 0)
                return;
            item.setCount(item.getCount() - 1);
            textView_price.setText(String.format("￥%.2f",item.getTotalPrice()));
        });
        findViewById(R.id.add).setOnClickListener(v -> {
            if (item.getCount() >= 99)
                return;
            item.setCount(item.getCount() + 1);
            textView_price.setText(String.format("￥%.2f",item.getTotalPrice()));
        });
        findViewById(R.id.addcar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<ShoppingCartTable.getCartSize();i++){
                    
                }
//                ShoppingCartTable.addNewToCart(item);
            }
        });

    }
}
