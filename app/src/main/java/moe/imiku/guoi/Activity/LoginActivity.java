package moe.imiku.guoi.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moe.imiku.guoi.AccountManager;
import moe.imiku.guoi.MessageTable;
import moe.imiku.guoi.R;


public class LoginActivity extends Activity {

    @BindView(R.id.id)
    AutoCompleteTextView id;
    @BindView(R.id.password)
    AutoCompleteTextView password;
    @BindView(R.id.cover)
    LinearLayout cover;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_login);

        ButterKnife.bind(this);
        handler = new Handler();
    }

    @OnClick(R.id.sign_in_button)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button: {
                Login();
                break;
            }
        }
    }

    private void Login() {
        cover.setVisibility(View.VISIBLE);
        if (id.getText().toString().equals("")) {
            MessageTable.sendToast(this, R.string.username_should_not_be_empty);
            cover.setVisibility(View.GONE);
            return;
        }

        if (password.getText().toString().equals("")) {
            MessageTable.sendToast(this, R.string.password_should_not_be_empty);
            cover.setVisibility(View.GONE);
            return;
        }
        new Thread(() -> {
            AccountManager.login(id.getText().toString(), password.getText().toString());
            handler.post(() -> {
                if (AccountManager.isLogined()) {
                    MessageTable.sendToast(this, R.string.login_success);
                    finish();
                } else {
                    cover.setVisibility(View.GONE);
                    MessageTable.sendToast(this, R.string.login_failed);
                }
            });
        }).start();
    }

    @OnClick({R.id.back, R.id.copyright})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.copyright:
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle(R.string.title_copyright);
                builder.setMessage(R.string.copyright);
                builder.setNegativeButton(R.string.sure, null);
                builder.show();
                break;
        }
    }
}