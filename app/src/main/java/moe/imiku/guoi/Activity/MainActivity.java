package moe.imiku.guoi.Activity;

import android.os.Bundle;
import android.widget.GridView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import moe.imiku.guoi.Adaptar.MyViewPagerAdapter;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.R;

public class MainActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.nav)
    GridView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void init () {
        ArrayList<PageLoader> view_list = new ArrayList<PageLoader>() {{
//            add(new Home(MainActivity.this));
        }};

        ArrayList<String> title_list = new ArrayList<String>() {{
//            add("主页");
        }};
        pager.setAdapter(new MyViewPagerAdapter(view_list, title_list));
        hideWaitCover();
    }
}
