package moe.imiku.guoi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moe.imiku.guoi.MessageTable;
import moe.imiku.guoi.Model.CartItem;
import moe.imiku.guoi.Model.Fruit;
import moe.imiku.guoi.R;
import moe.imiku.guoi.ShoppingCartTable;

import static moe.imiku.guoi.Util.FileUtil.getBitmapFromAsset;

public class DetailActivity extends Activity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.image_field)
    LinearLayout imageField;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.count)
    Button count;

    private Fruit fruit;
    private String[] images;
    private CartItem item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        fruit = getIntent().getParcelableExtra("fruit");
        initImages();
        init();
    }

    public void init() {
        item = new CartItem();
        item.setCount(1);
        item.setFruit(fruit);

        imageView.setImageBitmap(getBitmapFromAsset(getAssets(), fruit.getImage()));
        name.setText(fruit.getName());
        money.setText(String.format("￥%.2f", fruit.getPrice()));
        price.setText(String.format("￥%.2f", item.getTotalPrice()));

        for (String image : images) {
            ImageView imageView1 = new ImageView(this);
            imageView1.setImageBitmap(getBitmapFromAsset(getAssets(), image));
            imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView1.setAdjustViewBounds(true);
            imageField.addView(imageView1);
        }

    }


    public void initImages() {
        AssetManager am = getAssets();
        ArrayList<String> list = new ArrayList<>();
        String base = fruit.getImage();
        for (int i = 1; true; i++) {
            try {
                String t = base.replaceAll("_p[0-9].jpg", "_p" + i + ".jpg");
                am.open(t);
                list.add(t);
            } catch (IOException e) {
                break;
            }
        }
        images = list.toArray(new String[]{});
    }

    @OnClick({R.id.back, R.id.to_cart, R.id.addcar, R.id.add, R.id.min})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.to_cart:
                Intent intent = new Intent();
                intent.putExtra("toCart", true);
                setResult(23333, intent);
                finish();
                break;
            case R.id.add:
                if (item.getCount() >= 99)
                    return;
                item.setCount(item.getCount() + 1);
                price.setText(String.format("￥%.2f", item.getTotalPrice()));
                count.setText(item.getCount() + "");
                break;
            case R.id.min:
                if (item.getCount() <= 0)
                    return;
                item.setCount(item.getCount() - 1);
                price.setText(String.format("￥%.2f", item.getTotalPrice()));
                count.setText(item.getCount() + "");
                break;
            case R.id.addcar:
                if (item.getCount() <= 0)
                    break;
                for (int i = 0; i < ShoppingCartTable.getCartSize(); i++) {
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
                break;
        }
    }
}
