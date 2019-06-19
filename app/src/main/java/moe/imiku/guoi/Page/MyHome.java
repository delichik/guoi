package moe.imiku.guoi.Page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import moe.imiku.guoi.CurrentUser;
import moe.imiku.guoi.MessageTable;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

import static moe.imiku.guoi.Util.FileUtil.getBitmapFromAsset;

public class MyHome extends PageLoader {

    private Random random;
    protected MyHome(Context context) {
        super(context, R.layout.page_my_home);
        random = new Random(System.currentTimeMillis());
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected PageLoader subLoad() {
        findViewById(R.id.give_me_money).setOnClickListener(v -> {
            String msg;
            if (CurrentUser.pay(50)) {
                double t = random.nextInt(200);
                CurrentUser.saveMoney(t);
                msg = String.format("获得 %.2f 元", t);
                if (t < 50)
                    msg += "，非洲人";
                else
                    msg += "，小赚";
            }
            else
                msg = "你的余额不足";
            MessageTable.sendMessage(context, msg);
        });

        ((TextView) findViewById(R.id.money)).setText(String.format("%.2f元", CurrentUser.getMoney()));
        ((TextView) findViewById(R.id.user_name)).setText(CurrentUser.getName());
        ((ImageView) findViewById(R.id.user_head)).setImageBitmap(getBitmapFromAsset(context.getAssets(), CurrentUser.getHead_img()));
        CurrentUser.setListener((obj) ->
                ((TextView) findViewById(R.id.money))
                        .setText(String.format("%.2f元", CurrentUser.getMoney())));
        CurrentUser.saveMoney(0);
        return this;
    }

    @Override
    protected PageLoader subUnLoad() {
        return this;
    }
}
