package moe.imiku.guoi.Model;

public class Fruit {
    private String id;
    private String name;
    private double price;
    private String image = "image/default.jpg";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        if (image.equals(""))
            return;
        this.image = image;
    }
}
