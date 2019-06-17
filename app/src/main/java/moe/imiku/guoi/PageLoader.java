package moe.imiku.guoi;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.IdRes;

import moe.imiku.guoi.Model.ImageViewLoaderItem;
import moe.imiku.guoi.Util.NetResourceUtil;

public abstract class PageLoader {
    protected View page_view;
    protected Context context;
    protected boolean loaded = false;
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

    protected PageLoader(Context context, int id) {
        this.page_view = View.inflate(context, id, null);
        this.context = context;
    }

    protected PageLoader(Context context, View view) {
        this.page_view = view;
        this.context = context;
    }

    public PageLoader load (){
        if (loaded)
            return this;
        loaded = true;
        return subLoad();
    }

    public PageLoader reLoad () {
        return subLoad();
    }

    public PageLoader unLoad (){
        if (!loaded)
            return this;
        loaded = false;
        return subUnLoad();
    }

    protected abstract PageLoader subLoad();

    protected abstract PageLoader subUnLoad();

    public View getView (){
        return page_view;
    }

    protected void loadURLBitmap(String url, ImageView imageView) {
        Message msg = new Message();
        msg.what = 0;
        msg.obj = new ImageViewLoaderItem();
        ((ImageViewLoaderItem) msg.obj).setBitmap(NetResourceUtil.getURLimage(url));
        ((ImageViewLoaderItem) msg.obj).setImageView(imageView);
        handler.sendMessage(msg);
    }

    protected void runInMainThread (Runnable runnable) {
        handler.post(runnable);
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        return page_view.findViewById(id);
    }

}
