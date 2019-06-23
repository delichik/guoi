package moe.imiku.guoi.Page;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.IdRes;

import moe.imiku.guoi.Activity.MainActivity;
import moe.imiku.guoi.DataProvider.FruitProvider;
import moe.imiku.guoi.Model.Fruit;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

public class Main extends PageLoader {
    protected Main(Context context) {
        super(context, R.layout.page_main);
    }

    private String[] fruit_ids = {"1", "2", "3", "4", "5", "6", "7"};

    private FruitProvider provider;

    @Override
    protected PageLoader subLoad() {
        provider = new FruitProvider();
        new Thread(() -> {
            setImage(R.id.image1, fruit_ids[0]);
            setImage(R.id.image2, fruit_ids[1]);
            setImage(R.id.image3, fruit_ids[2]);
            setImage(R.id.image4, fruit_ids[3]);
            setImage(R.id.image5, fruit_ids[4]);
            setImage(R.id.image6, fruit_ids[5]);
            setImage(R.id.image7, fruit_ids[6]);
        }).start();
        return this;
    }

    private void setImage (@IdRes int imageView_id, String fid) {
        ImageView imageView = findViewById(imageView_id);
        Fruit fruit = provider.getFruitById(fid);
        String image = getInfoImage(fruit);
        loadURLBitmap(image, imageView);
        imageView.setOnClickListener((v) -> ((MainActivity)context).toDetail(fruit));
    }

    private String getInfoImage(Fruit fruit) {
        return fruit.getImage().replaceAll("_p0\\.", "_p1\\.");
    }

    @Override
    protected PageLoader subUnLoad() {
        return this;
    }
}
