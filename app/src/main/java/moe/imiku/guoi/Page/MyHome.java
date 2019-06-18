package moe.imiku.guoi.Page;

import android.content.Context;

import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

public class MyHome extends PageLoader {

    protected MyHome(Context context) {
        super(context, R.layout.page_my_home);
    }

    @Override
    protected PageLoader subLoad() {

        return this;
    }

    @Override
    protected PageLoader subUnLoad() {
        return this;
    }
}
