package moe.imiku.guoi.Page;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import moe.imiku.guoi.Adaptar.ClassRecycleAdapter;
import moe.imiku.guoi.DataProvider.FruitProvider;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

public class Classification extends PageLoader {

    RadioGroup headerField;
    RecyclerView fruitField;
    private FruitProvider fruitProvider;

    protected Classification(Context context) {
        super(context, R.layout.page_class);
        fruitField = findViewById(R.id.fruit_field);
        fruitField.setLayoutManager(new LinearLayoutManager(context));
        headerField = findViewById(R.id.header_field);
    }

    @Override
    protected PageLoader subLoad() {
        headerField.setOnCheckedChangeListener((group, checkedId) -> {
            String class_name = ((RadioButton) group.findViewById(checkedId)).getText().toString();
            refreshList(class_name);
        });

        new Thread(() -> {
            fruitProvider = new FruitProvider();
            for (String str : fruitProvider.getClasses()) {
                RadioButton button = new RadioButton(context);
                button.setText(str);
                button.setBackgroundResource(R.drawable.class_radio_button);
                button.setButtonDrawable(null);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.width = dip2px(100);
                params.height = dip2px(50);
                button.setTextSize(16);
                button.setGravity(Gravity.CENTER);
                button.setLayoutParams(params);
                runInMainThread(() -> headerField.addView(button));
            }
            runInMainThread(() -> headerField.check(headerField.getChildAt(0).getId()));
        }).start();
        return this;
    }

    private void refreshList(String class_name) {
        fruitField.setAdapter(new ClassRecycleAdapter(fruitProvider.getFruitsByClass(class_name), context));
//        fruitField.removeAllViews();
//        new Thread(() -> {
//            for (Fruit fruit : fruitProvider.getFruitsByClass(class_name)) {
//                View view = View.inflate(context, R.layout.item_class, null);
//                ((TextView) view.findViewById(R.id.name)).setText(fruit.getName());
//                ((TextView) view.findViewById(R.id.price)).setText(String.format("￥%.2f/kg", fruit.getPrice()));
//                ((TextView) view.findViewById(R.id.id)).setText(fruit.getId());
//                view.setOnClickListener(v -> {
//                    String id = ((TextView) view.findViewById(R.id.id)).getText().toString();
//                    ((MainActivity) context).toDetail(fruitProvider.getFruitById(id));
//                });
//                ImageView image = view.findViewById(R.id.image);
//                image.setImageBitmap(getBitmapFromAsset(context.getAssets(), fruit.getImage()));
//                runInMainThread(() -> fruitField.addView(view));
//            }
//        }).start();
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
