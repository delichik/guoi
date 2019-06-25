package moe.imiku.guoi.Model;

public class User {
    public final static String DEFAULT_AVATAR = "https://cf.hamreus.com/images/user-avatar.png";
    public final static String DEFAULT_NAME = "未登录";

    private String name;
    private String avatar;
    private double money = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        if (avatar == null || avatar.equals(""))
            return DEFAULT_AVATAR;
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

}
