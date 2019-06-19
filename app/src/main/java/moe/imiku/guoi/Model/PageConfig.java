package moe.imiku.guoi.Model;

public class PageConfig {
    String class_name;
    String title;
    String icon = "icon/unknown.png";

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        icon = icon.replaceAll("[\n \r]", "");
        this.icon = icon;
    }
}
