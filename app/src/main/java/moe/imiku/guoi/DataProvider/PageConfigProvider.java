package moe.imiku.guoi.DataProvider;

import android.content.Context;

import java.util.ArrayList;

import moe.imiku.guoi.Model.PageConfig;
import moe.imiku.guoi.Util.DebugUtil;
import moe.imiku.guoi.Util.FileUtil;

public class PageConfigProvider {
    private Context context;

    public PageConfigProvider (Context context) {
        this.context = context;
    }

    public ArrayList<PageConfig> getPageConfigs() {
        ArrayList<PageConfig> list = new ArrayList<>();
        String str = FileUtil.getTextFromAssets(context.getAssets(), "page.conf");
        if (str == null) return null;
        String[] lines = str.split("\n");
        for (String line : lines) {
            if (line.startsWith("#"))
                continue;
            String t[] = line.split(", ?");
            DebugUtil.printLog(t);
            PageConfig config = new PageConfig();
            config.setClass_name(t[0]);
            config.setTitle(t[1]);
            if (t.length > 2) config.setIcon(t[2]);
            list.add(config);
        }
        return list;
    }
}
