package moe.imiku.guoi.Page;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import moe.imiku.guoi.Activity.MainActivity;
import moe.imiku.guoi.Config;
import moe.imiku.guoi.DataProvider.FruitProvider;
import moe.imiku.guoi.Model.Fruit;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;
import moe.imiku.guoi.Util.ThreadPool;

import static moe.imiku.guoi.Util.FileUtil.getBitmapFromAsset;

public class Classification extends PageLoader {

    private FruitProvider fruitProvider;
    private ThreadPool threadPool;

    protected Classification(Context context) {
        super(context, R.layout.page_class);
    }

    @Override
    protected PageLoader subLoad() {
        RadioGroup header_field = findViewById(R.id.header_field);
        header_field.setOnCheckedChangeListener((group, checkedId) -> {
            String class_name = ((RadioButton)group.findViewById(checkedId)).getText().toString();
            refreshList(class_name);
        });

        threadPool = new ThreadPool(Config.THREAD_COUNT_FAST);
        new Thread(() -> {
            fruitProvider = new FruitProvider(context.getAssets());
            for(String str : fruitProvider.getClasses()){
                RadioButton _filed = new RadioButton(context);
                _filed.setText(str);
                _filed.setBackgroundResource(R.drawable.class_radio_button);
                _filed.setButtonDrawable(null);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.width = dip2px(100);
                params.height = dip2px(50);
                _filed.setTextSize(16);
                _filed.setGravity(Gravity.CENTER);
                _filed.setLayoutParams(params);
                runInMainThread(() -> header_field.addView(_filed));
            }
            runInMainThread(() -> header_field.check(header_field.getChildAt(0).getId()));
        }).start();
        return this;
    }

    private void refreshList (String class_name) {
        LinearLayout fruit_field = findViewById(R.id.fruit_field);
        fruit_field.removeAllViews();
        new Thread(() -> {
            for (Fruit fruit : fruitProvider.getFruitsByClass(class_name)) {
                View view = View.inflate(context, R.layout.item_class, null);
                ((TextView) view.findViewById(R.id.name)).setText(fruit.getName());
                ((TextView) view.findViewById(R.id.price)).setText(String.format("￥%.2f/kg", fruit.getPrice()));
                ((TextView) view.findViewById(R.id.id)).setText(fruit.getId());
                view.setOnClickListener(v -> {
                    String id = ((TextView) view.findViewById(R.id.id)).getText().toString();
                    ((MainActivity) context).toDetail(fruitProvider.getFruitById(id));
                });
                ImageView image = view.findViewById(R.id.image);
                image.setImageBitmap(getBitmapFromAsset(context.getAssets(), fruit.getImage()));
                runInMainThread(() -> fruit_field.addView(view));
            }
        }).start();
    }

    @Override
    protected PageLoader subUnLoad() {
        return null;
    }

    private int dip2px(float dipValue) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }
}
