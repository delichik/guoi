package moe.imiku.guoi.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import moe.imiku.guoi.Adaptar.MyViewPagerAdapter;
import moe.imiku.guoi.DataProvider.PageConfigProvider;
import moe.imiku.guoi.Model.PageConfig;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;
import moe.imiku.guoi.Util.DebugUtil;

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
//            add(new Home(MainActivity.this));
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
//            add("主页");
        }};
        ((ViewPager)super.findViewById(R.id.pager)).setAdapter(new MyViewPagerAdapter(view_list, title_list));
    }
}
