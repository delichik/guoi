package moe.imiku.guoi.Page;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;

import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

public class Classification extends PageLoader {
    protected Classification(Context context) {
        super(context, R.layout.page_class);
    }

    @Override
    protected PageLoader subLoad() {
        LinearLayout header_field = findViewById(R.id.header_field);
        Button _field = new Button(context);
        _field.setText("dsf");
        return this;
    }

    @Override
    protected PageLoader subUnLoad() {
        return null;
    }
}
