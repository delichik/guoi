package moe.imiku.guoi.Page;

import android.content.Context;

import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

public class ShoppingCart extends PageLoader {

    protected ShoppingCart(Context context) {
        super(context, R.layout.page_shoppign_card);
    }

    @Override
    protected PageLoader subLoad() {
        return null;
    }

    @Override
    protected PageLoader subUnLoad() {
        return null;
    }
}
