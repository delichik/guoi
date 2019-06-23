package moe.imiku.guoi.Adaptar;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import moe.imiku.guoi.Activity.MainActivity;
import moe.imiku.guoi.Config;
import moe.imiku.guoi.Model.CartItem;
import moe.imiku.guoi.Model.Fruit;
import moe.imiku.guoi.Model.ImageViewLoaderItem;
import moe.imiku.guoi.R;
import moe.imiku.guoi.ShoppingCartTable;
import moe.imiku.guoi.Util.NetResourceUtil;
import moe.imiku.guoi.Util.ThreadPool;

public class ClassRecycleAdapter extends RecyclerView.Adapter<ClassRecycleAdapter.VH>{

public static class VH extends RecyclerView.ViewHolder{

    public final TextView name;
    public final TextView price;
    public final TextView add;
    public final ImageView image;

    public VH(View v) {
        super(v);
        name = v.findViewById(R.id.name);
        price = v.findViewById(R.id.price);
        add = v.findViewById(R.id.add);
        image = v.findViewById(R.id.image);
    }
}

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

    private List<Fruit> fruits;
    private Context context;
    private ThreadPool threadPool;
    public ClassRecycleAdapter(List<Fruit> data, Context context) {
        this.fruits = data;
        this.context = context;
        this.threadPool = new ThreadPool(Config.THREAD_COUNT_FAST);
    }

    private void loadURLBitmap(String url, ImageView imageView) {
        Message msg = new Message();
        msg.what = 0;
        msg.obj = new ImageViewLoaderItem();
        ((ImageViewLoaderItem) msg.obj).setBitmap(NetResourceUtil.getURLimage(url));
        ((ImageViewLoaderItem) msg.obj).setImageView(imageView);
        handler.sendMessage(msg);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Fruit fruit = fruits.get(position);

        holder.name.setText(fruit.getName());
        holder.price.setText(String.format("￥%.2f/kg", fruit.getPrice()));
        holder.itemView.setOnClickListener(v -> {
            ((MainActivity) context).toDetail(fruits.get(position));
        });
        holder.add.setOnClickListener(v -> {
            CartItem item = new CartItem();
            item.setFruit(fruit);
            item.setCount(1);
            ShoppingCartTable.addToCart(item, v.getContext());
        });
        threadPool.addNewThread(() -> loadURLBitmap(fruit.getImage(), holder.image));
    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new VH(v);
    }
}