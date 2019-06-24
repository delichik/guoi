package moe.imiku.guoi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moe.imiku.guoi.Config;
import moe.imiku.guoi.Model.CartItem;
import moe.imiku.guoi.Model.Fruit;
import moe.imiku.guoi.Model.ImageViewLoaderItem;
import moe.imiku.guoi.R;
import moe.imiku.guoi.ShoppingCartTable;
import moe.imiku.guoi.Util.ThreadPool;

public class DetailActivity extends Activity {

//    @BindView(R.id.imageView)
//    ImageView imageView;
//    @BindView(R.id.name)
//    TextView name;
//    @BindView(R.id.money)
//    TextView money;
//    @BindView(R.id.image_field)
//    LinearLayout imageField;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.count)
    Button count;
    @BindView(R.id.webview)
    WebView webView;

    private Fruit fruit;
    private CartItem item;
    private ThreadPool threadPool;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ((ImageViewLoaderItem)msg.obj)
                            .getImageView()
                            .setImageBitmap(((ImageViewLoaderItem)msg.obj).getBitmap());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        threadPool = new ThreadPool(Config.THREAD_COUNT_FAST);

        fruit = getIntent().getParcelableExtra("fruit");
//        initImages();
        init();
    }

    public void init() {
        item = new CartItem();
        item.setCount(1);
        item.setFruit(fruit);
//        threadPool.addNewThread(() -> {
//            Bitmap bitmap = getURLimage(fruit.getImage());
//            Message msg = new Message();
//            msg.what = 0;
//            msg.obj = new ImageViewLoaderItem();
//            ((ImageViewLoaderItem) msg.obj).setBitmap(bitmap);
//            ((ImageViewLoaderItem) msg.obj).setImageView(imageView);
//            handler.sendMessage(msg);
//        });
//        name.setText(fruit.getName());
//        money.setText(String.format("￥%.2f", fruit.getPrice()));
        price.setText(String.format("￥%.2f", item.getTotalPrice()));

        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.loadUrl(Config.BASE_DETAIL_URL + "?id=" + fruit.getId());
    }


//    boolean stop = false;
//    public void initImages() {
//        stop = false;
//        new Thread(() -> {
//            String base = fruit.getImage();
//            for (int i = 1; !stop; i++) {
//                String t = base.replaceAll("_p[0-9].jpg", "_p" + i + ".jpg");
//                threadPool.addNewThread(() -> {
//                    Bitmap bitmap = getURLimage(t);
//                    if (bitmap == null) {
//                        stop = true;
//                        return;
//                    }
//
//                    ImageView imageView = new ImageView(this);
//                    Message msg = new Message();
//                    msg.what = 0;
//                    msg.obj = new ImageViewLoaderItem();
//                    ((ImageViewLoaderItem) msg.obj).setBitmap(bitmap);
//                    ((ImageViewLoaderItem) msg.obj).setImageView(imageView);
//                    handler.sendMessage(msg);
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    imageView.setAdjustViewBounds(true);
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                            ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.WRAP_CONTENT);
//                    handler.post(() -> imageField.addView(imageView, params));
//                });
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

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
                price.setText(String.format("￥%.2f", item.getTotalPrice()));
                count.setText(item.getCount() + "");
                break;
            case R.id.min:
                if (item.getCount() <= 0)
                    return;
                item.setCount(item.getCount() - 1);
                price.setText(String.format("￥%.2f", item.getTotalPrice()));
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
