package moe.imiku.guoi.Model;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    private HashMap<String, String> cookie;

    public Cookie () {
        cookie = new HashMap<>();
    }

    public void add (String key, String value) {
        cookie.put(key, value);
    }

    public void removeAll () {
        cookie.clear();
    }

    public String getCookie () {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, String> entry : cookie.entrySet()) {
            str.append(entry.getKey());
            str.append("=");
            str.append(entry.getValue());
            str.append("; ");
        }
        return str.toString();
    }

    public void parseCookie(String c_str) {
        String[] c_strs = c_str.split("; ");
        try {
            for (String t : c_strs) {
                String[] t2 = t.split("=");
                cookie.put(t2[0], t2[1]);
            }
        } catch (Exception ignored) {}
    }

    public Map<String, String> getCookieMap() {
        return cookie;
    }
}
