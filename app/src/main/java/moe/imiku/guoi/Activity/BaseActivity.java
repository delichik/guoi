package moe.imiku.guoi.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import moe.imiku.guoi.R;

/*
 *
 * 这个文件禁止使用 ButterKnife
 *
 */

public class BaseActivity extends AppCompatActivity {
    int wait_cover_apply_time = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        ((LinearLayout)super.findViewById(R.id.base_filed)).addView(View.inflate(this, layoutResID, null));
    }

    public void showWaitCover() {
        wait_cover_apply_time++;
        super.findViewById(R.id.base_wait_cover).setVisibility(View.VISIBLE);
        super.findViewById(R.id.base_filed).setVisibility(View.GONE);
    }

    public void hideWaitCover() {
        handler.post(() -> {
            if (--wait_cover_apply_time > 0)
                return;
            super.findViewById(R.id.base_wait_cover).setVisibility(View.GONE);
            super.findViewById(R.id.base_filed).setVisibility(View.VISIBLE);
        });
    }
}
