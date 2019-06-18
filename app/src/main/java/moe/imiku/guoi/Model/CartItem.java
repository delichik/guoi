package moe.imiku.guoi.Model;

public class CartItem {
    private Fruit fruit;
    private int count;

    public Fruit getFruit() {
        return fruit;
    }

    public void setFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotalPrice () {
        return fruit.getPrice() * count;
    }
}
