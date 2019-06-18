package moe.imiku.guoi.Page;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import moe.imiku.guoi.Activity.MainActivity;
import moe.imiku.guoi.DataProvider.FruitProvider;
import moe.imiku.guoi.Model.Fruit;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

import static moe.imiku.guoi.Util.FileUtil.getBitmapFromAsset;

public class Classification extends PageLoader {

    private FruitProvider fruitProvider;

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
            params.height = dip2px(40);
            _filed.setPadding(10, 0, 0, 0);
            _filed.setLayoutParams(params);
            header_field.addView(_filed);
        }
        header_field.check(header_field.getChildAt(0).getId());
        return this;
    }

    private void refreshList (String class_name) {
        LinearLayout fruit_field = findViewById(R.id.fruit_field);
        fruit_field.removeAllViews();
        for (Fruit fruit : fruitProvider.getFruitsByClass(class_name)) {
            View view = View.inflate(context, R.layout.item_class, null);
            ((TextView)view.findViewById(R.id.name)).setText(fruit.getName());
            ((TextView)view.findViewById(R.id.price)).setText(String.valueOf(fruit.getPrice()));
            ((TextView)view.findViewById(R.id.id)).setText(fruit.getId());
            view.setOnClickListener(v -> {
                String id = ((TextView)view.findViewById(R.id.id)).getText().toString();
                ((MainActivity)context).toDetail(id);
            });
            ImageView image = view.findViewById(R.id.image);
            image.setImageBitmap(getBitmapFromAsset(context.getAssets(), fruit.getImage()));
            fruit_field.addView(view);
        }
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
