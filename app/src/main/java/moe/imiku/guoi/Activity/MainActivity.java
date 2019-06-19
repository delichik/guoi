package moe.imiku.guoi.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import moe.imiku.guoi.Adaptar.MyGridViewAdapter;
import moe.imiku.guoi.Adaptar.MyViewPagerAdapter;
import moe.imiku.guoi.DataProvider.PageConfigProvider;
import moe.imiku.guoi.Model.PageConfig;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;
import moe.imiku.guoi.Util.DebugUtil;

import static moe.imiku.guoi.Util.FileUtil.getBitmapFromAsset;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DebugUtil.isApkDebugable(this);
        init();
    }

    private void init () {
        PageConfigProvider provider = new PageConfigProvider(this);
        ArrayList<PageConfig> configs = provider.getPageConfigs();

        ArrayList<PageLoader> view_list = new ArrayList<PageLoader>() {{
            try {
                for (PageConfig config : configs) {
                    Constructor constructor = Class.forName("moe.imiku.guoi.Page." + config.getClass_name())
                            .getConstructor(Context.class);
                    add((PageLoader) constructor.newInstance(MainActivity.this));
                }
            } catch (ClassNotFoundException
                    | NoSuchMethodException
                    | InvocationTargetException
                    | IllegalAccessException
                    | InstantiationException e) {
                e.printStackTrace();
            }
        }};

        ArrayList<String> title_list = new ArrayList<String>() {{
            for (PageConfig config : configs) {
                add(config.getTitle());
            }
        }};

        ArrayList<View> nav_list = new ArrayList<View>() {{
            for (int i = 0; i < configs.size(); i++){
                PageConfig config = configs.get(i);

                View view = View.inflate(MainActivity.this, R.layout.item_nav, null);
                TextView textView = view.findViewById(R.id.title);
                textView.setText(config.getTitle());
                ImageView image = view.findViewById(R.id.image);
                image.setImageBitmap(getBitmapFromAsset(getAssets(), config.getIcon()));
                final int fi = i;
                view.setOnClickListener(v ->
                        ((ViewPager)MainActivity.this.findViewById(R.id.pager)).setCurrentItem(fi));
                add(view);
            }
        }};

        ((ViewPager)super.findViewById(R.id.pager)).setAdapter(new MyViewPagerAdapter(view_list, title_list));

        GridView gridView = super.findViewById(R.id.nav);
        gridView.setNumColumns(configs.size());
        gridView.setAdapter(new MyGridViewAdapter(nav_list));
    }

    public void toDetail(String id) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
