package moe.imiku.guoi.Model;

import android.os.Parcel;
import android.os.Parcelable;

import moe.imiku.guoi.Config;

public class Fruit implements Parcelable {
    private String id;
    private String name;
    private double price;
    private String image = "image/default.jpg";

    public Fruit () {}

    protected Fruit(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readDouble();
        image = in.readString();
    }

    public static final Creator<Fruit> CREATOR = new Creator<Fruit>() {
        @Override
        public Fruit createFromParcel(Parcel in) {
            return new Fruit(in);
        }

        @Override
        public Fruit[] newArray(int size) {
            return new Fruit[size];
        }
    };

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
        return Config.BASE_INFO_URL + image;
    }

    public void setImage(String image) {
        if (image.equals(""))
            return;
        this.image = image;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(image);
    }
}
