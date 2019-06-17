package moe.imiku.guoi.Page;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import moe.imiku.guoi.DataProvider.FruitProvider;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

public class Classification extends PageLoader {
    protected Classification(Context context) {
        super(context, R.layout.page_class);
    }
    private RadioGroup.OnCheckedChangeListener listen=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int id= group.getCheckedRadioButtonId();
        }
    };
    @Override
    protected PageLoader subLoad() {
        LinearLayout header_field = findViewById(R.id.header_field);
        FruitProvider fruitProvider = new FruitProvider(context.getAssets());
        for(String str :fruitProvider.getClasses()){
            RadioButton _filed = new RadioButton(context);
            _filed.setText(str);
            _filed.setBackground(null);
            header_field.addView(_filed);
        }
        return this;
    }


    @Override
    protected PageLoader subUnLoad() {
        return null;
    }
}
