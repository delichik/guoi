package moe.imiku.guoi.Util;

public class DoubleUtil {

    public static double getDouble(String v) {
        double out = 0;
        boolean haveNum = false;
        int point = 0;
        boolean negative = false;
        if (v != null) {
            for (int i = 0; i < v.length(); i++) {
                if (v.charAt(i) == '-')
                    negative = true;
                if (Character.isDigit(v.charAt(i))) {
                    haveNum = true;
                    out = out * 10 + Character.getNumericValue(v.charAt(i));
                }
            }
            if (haveNum) {
                if (v.contains("."))
                    for (int i = v.length() - 1; i >= 0; i--) {
                        if (Character.isDigit(v.charAt(i))) {
                            point++;
                        }
                        else if (v.charAt(i) == '.')
                            break;
                    }
                return (negative ? -1 : 1) * (out / Math.pow(10, point));
            }
        }
        return -100000;
    }
}
