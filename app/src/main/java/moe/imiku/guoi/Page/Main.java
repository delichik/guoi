package moe.imiku.guoi.Page;

import android.content.Context;
import android.widget.ImageView;

import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

import static moe.imiku.guoi.Util.FileUtil.getBitmapFromAsset;

public class Main extends PageLoader {
    protected Main(Context context) {
        super(context, R.layout.page_main);
    }

    @Override
    protected PageLoader subLoad() {
        ImageView imageView = findViewById(R.id.image1);
        imageView.setImageBitmap(getBitmapFromAsset(context.getAssets(), "image/list01_item01_p1.jpg"));
        imageView = findViewById(R.id.image2);
        imageView.setImageBitmap(getBitmapFromAsset(context.getAssets(), "image/list01_item02_p1.jpg"));
        imageView = findViewById(R.id.image3);
        imageView.setImageBitmap(getBitmapFromAsset(context.getAssets(), "image/list01_item03_p1.jpg"));
        imageView = findViewById(R.id.image4);
        imageView.setImageBitmap(getBitmapFromAsset(context.getAssets(), "image/list01_item04_p1.jpg"));
        imageView = findViewById(R.id.image5);
        imageView.setImageBitmap(getBitmapFromAsset(context.getAssets(), "image/list01_item05_p1.jpg"));
        imageView = findViewById(R.id.image6);
        imageView.setImageBitmap(getBitmapFromAsset(context.getAssets(), "image/list01_item06_p1.jpg"));
        imageView = findViewById(R.id.image7);
        imageView.setImageBitmap(getBitmapFromAsset(context.getAssets(), "image/list01_item07_p1.jpg"));
        return this;
    }

    @Override
    protected PageLoader subUnLoad() {
        return this;
    }
}
