package moe.imiku.guoi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moe.imiku.guoi.Config;
import moe.imiku.guoi.Model.CartItem;
import moe.imiku.guoi.Model.Fruit;
import moe.imiku.guoi.R;
import moe.imiku.guoi.ShoppingCartTable;

public class DetailActivity extends Activity {

    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.count)
    Button count;
    @BindView(R.id.webview)
    WebView webView;

    private Fruit fruit;
    private CartItem item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        fruit = getIntent().getParcelableExtra("fruit");
        init();
    }

    public void init() {
        item = new CartItem();
        item.setCount(1);
        item.setFruit(fruit);
        price.setText(String.format(getResources().getString(R.string._format_money), item.getTotalPrice()));

        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        webView.loadUrl(Config.BASE_DETAIL_URL + "?id=" + fruit.getId());
    }

    @OnClick({R.id.back, R.id.to_cart, R.id.addcar, R.id.add, R.id.min})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.to_cart:
                Intent intent = new Intent();
                intent.putExtra("toCart", true);
                setResult(23333, intent);
                finish();
                break;
            case R.id.add:
                if (item.getCount() >= 99)
                    return;
                item.setCount(item.getCount() + 1);
                price.setText(String.format(getResources().getString(R.string._format_money), item.getTotalPrice()));
                count.setText(item.getCount() + "");
                break;
            case R.id.min:
                if (item.getCount() <= 0)
                    return;
                item.setCount(item.getCount() - 1);
                price.setText(String.format(getResources().getString(R.string._format_money), item.getTotalPrice()));
                count.setText(item.getCount() + "");
                break;
            case R.id.addcar:
                if (item.getCount() <= 0)
                    break;
                ShoppingCartTable.addToCart(item, this);
                break;
        }
    }
}
