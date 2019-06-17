package moe.imiku.guoi.Util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.ArrayList;

public class DebugUtil {
    private static boolean isDebugable = false;

    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
            isDebugable = ((info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
            return isDebugable;
        } catch (Exception e) {}
        return false;
    }

    public static void printLog (int log) {
        if (isDebugable)
            System.out.println(log);
    }

    public static void printLog (String log) {
        if (isDebugable)
            System.out.println(log);
    }

    public static void printLog (boolean log) {
        if (isDebugable)
            System.out.println(log);
    }

    public static void printLog (ArrayList<String> log) {
        if (isDebugable && log != null) {
            for (String t : log) {
                System.out.print(t);
                System.out.print(", ");
            }
            System.out.println();
        }
    }

    public static void printLogLn (ArrayList<String> log) {
        if (isDebugable && log != null) {
            for (String t : log) {
                System.out.print(t);
                System.out.println();
            }
        }
    }

    public static void printLog (String[] log) {
        if (isDebugable && log != null) {
            for (String t : log) {
                System.out.print(t);
                System.out.print(", ");
            }
            System.out.println();
        }
    }

    public static void printLog (String log, int times) {
        if (isDebugable) {
            for (int i = 0; i < times; ++i) {
                System.out.println(log);
            }
        }
    }
}
