package moe.imiku.guoi.Adaptar;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

import moe.imiku.guoi.Config;
import moe.imiku.guoi.PageLoader;
import moe.imiku.guoi.Util.ThreadPool;

public class MyViewPagerAdapter extends PagerAdapter {
    private ArrayList<PageLoader> loaderList;
    private ArrayList<String> title_list;

    public MyViewPagerAdapter(ArrayList<PageLoader> loaderList, ArrayList<String> title_list) {
        this(loaderList);
        this.title_list = title_list;
    }

    public MyViewPagerAdapter(ArrayList<PageLoader> loaderList) {
        super();
        this.loaderList = loaderList;
        Handler handler = new Handler();
        ThreadPool threadPool = new ThreadPool(Config.THREAD_COUNT_FAST);
        for (PageLoader loader : loaderList) {
            threadPool.addNewThread(() -> handler.post(loader::load));
        }
    }

    @Override
    public int getCount() {
        return loaderList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (title_list == null || position > title_list.size()) return "";
        return title_list.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(loaderList.get(position).load().getView());
        return loaderList.get(position).getView();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(loaderList.get(position).getView());
    }
}
