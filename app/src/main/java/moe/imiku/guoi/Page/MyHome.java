package moe.imiku.guoi.Page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import moe.imiku.guoi.AccountManager;
import moe.imiku.guoi.Activity.LoginActivity;
import moe.imiku.guoi.MessageTable;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

public class MyHome extends PageLoader {

    private LinearLayout login_cover;
    private Button give_me_money;
    private ProgressBar wait_cover;
    private TextView money;
    private TextView user_name;
    private ImageView user_head;
    private Button login;

    private Handler handler;
    private Random random;

    public MyHome(Context context) {
        super(context, R.layout.page_my_home);
        random = new Random(System.currentTimeMillis());
        handler = new Handler(Looper.getMainLooper());
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected PageLoader subLoad() {
        login_cover = findViewById(R.id.login_cover);
        give_me_money = findViewById(R.id.give_me_money);
        wait_cover = findViewById(R.id.wait);
        money = findViewById(R.id.money);
        user_name = findViewById(R.id.user_name);
        user_head = findViewById(R.id.user_head);
        login = findViewById(R.id.login);

        AccountManager.setListener(changedObj -> handler.post(() -> {
            switch ((String)changedObj) {
                case "money":
                    ((TextView) findViewById(R.id.money)).setText(String.format(
                            context.getString(R.string._format_money),
                            AccountManager.getMoney()));
                    break;
                case "login":
                    refresh();
                    break;
            }
        }));
        refresh();
        return this;
    }

    private void refresh () {
        if (AccountManager.isLogined()) {
            login_cover.setVisibility(View.GONE);
            give_me_money.setOnClickListener(v -> {
                wait_cover.setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                AccountManager.pay(50, (code) -> {
                    switch ((int)code) {
                        case AccountManager.USER_DATA_UPDATE_OK:
                            double t = random.nextInt(200);
                            AccountManager.saveMoney(t, changedObj -> {
                                String msg = String.format(
                                        context.getString(R.string._format_got_money),
                                        t);
                                if (t < 50)
                                    msg += context.getString(R.string.got_money_fail);
                                else
                                    msg += context.getString(R.string.got_money_win);
                                MessageTable.sendMessageAndToast(context, msg);
                                wait_cover.setVisibility(View.GONE);
                                v.setVisibility(View.VISIBLE);
                            });
                            break;
                        case AccountManager.USER_DATA_UPDATE_ERR_NO_ENOUGH_MONEY:
                            MessageTable.sendMessageAndToast(context, R.string.no_enough_money);
                            wait_cover.setVisibility(View.GONE);
                            v.setVisibility(View.VISIBLE);
                            break;
                        case AccountManager.USER_DATA_UPDATE_ERR_NOT_LOGINED_YET:
                            MessageTable.sendMessageAndToast(context, R.string.please_login_first);
                            wait_cover.setVisibility(View.GONE);
                            v.setVisibility(View.VISIBLE);
                            break;
                    }
                });
            });

            money.setText(String.format(context.getString(R.string._format_money), AccountManager.getMoney()));
            user_name.setText(AccountManager.getName());
            user_head.setOnClickListener((v) -> {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle(R.string.logout);
                builder.setPositiveButton(R.string.cancel, null);
                builder.setNegativeButton(R.string.sure, (dialogInterface, i) ->
                        new Thread(AccountManager::logout).start()
                );
                builder.show();
            });
            new Thread(() -> loadURLBitmap(
                    AccountManager.getAvatar(),
                    user_head)
            ).start();
            AccountManager.saveMoney(0, (obj) ->{});
            login.setOnClickListener(null);
        }
        else {
            login_cover.setVisibility(View.VISIBLE);
            give_me_money.setOnClickListener(null);
            money.setText(String.format(context.getString(R.string._format_money), AccountManager.getMoney()));
            user_name.setText(AccountManager.getName());
            user_head.setOnClickListener(null);
            user_head.setImageBitmap(null);
            login.setOnClickListener((v) ->
                    context.startActivity(new Intent(context, LoginActivity.class)));

        }
    }

    @Override
    protected PageLoader subUnLoad() {
        return this;
    }
}
