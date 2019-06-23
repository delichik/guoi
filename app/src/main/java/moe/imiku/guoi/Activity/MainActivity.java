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

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.imiku.guoi.Adaptar.MyGridViewAdapter;
import moe.imiku.guoi.Adaptar.MyViewPagerAdapter;
import moe.imiku.guoi.DataProvider.PageConfigProvider;
import moe.imiku.guoi.Model.Fruit;
import moe.imiku.guoi.Model.PageConfig;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;
import moe.imiku.guoi.Util.DebugUtil;

import static moe.imiku.guoi.Util.FileUtil.getBitmapFromAsset;

public class MainActivity extends Activity {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.nav)
    GridView nav;
    private ArrayList<View> nav_list;
    private int cart_page_position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DebugUtil.isApkDebugable(this);
        init();
    }

    private void init() {
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

        nav_list = new ArrayList<View>() {{
            for (int i = 0; i < configs.size(); i++) {
                PageConfig config = configs.get(i);

                View view = View.inflate(MainActivity.this, R.layout.item_nav, null);
                TextView textView = view.findViewById(R.id.title);
                textView.setText(config.getTitle());
                ImageView image = view.findViewById(R.id.image);
                image.setImageBitmap(getBitmapFromAsset(getAssets(), config.getIcon()));
                if (config.getTitle().contains("购物车"))
                    cart_page_position = i;
                final int fi = i;
                view.setOnClickListener(v -> {
//                    for (int t = 0; t < nav_list.size(); t++)
//                        nav_list.get(t).setBackgroundColor(getResources().getColor(R.color.background_nav));
//                    nav_list.get(fi).setBackgroundColor(getResources().getColor(R.color.background_nav_dark));
                    ((ViewPager) MainActivity.this.findViewById(R.id.pager)).setCurrentItem(fi);
                });
                if (i == 0)
                    view.setBackgroundColor(getResources().getColor(R.color.background_nav_dark));
                add(view);
            }
        }};

        pager.setAdapter(new MyViewPagerAdapter(view_list, title_list));

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {
                for (int t = 0; t < nav_list.size(); t++)
                    nav_list.get(t).setBackgroundColor(getResources().getColor(R.color.background_nav));
                nav_list.get(pager.getCurrentItem()).setBackgroundColor(getResources().getColor(R.color.background_nav_dark));
            }
        });
        nav.setNumColumns(configs.size());
        nav.setAdapter(new MyGridViewAdapter(nav_list));
    }

    public void toDetail(Fruit fruit) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("fruit", fruit);
        startActivityForResult(intent, 23333);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 23333) {
            if (data.getBooleanExtra("toCart", false)
            && cart_page_position > 0 && cart_page_position < pager.getChildCount()) {
                pager.setCurrentItem(cart_page_position,false);
                for (int t = 0; t < nav_list.size(); t++)
                    nav_list.get(t).setBackgroundColor(getResources().getColor(R.color.background_nav));
                nav_list.get(pager.getCurrentItem()).setBackgroundColor(getResources().getColor(R.color.background_nav_dark));
            }
        }
    }
}
