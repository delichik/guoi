package moe.imiku.guoi.DataProvider;

import android.content.res.AssetManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import moe.imiku.guoi.Model.Fruit;
import moe.imiku.guoi.Util.DoubleUtil;

import static moe.imiku.guoi.Util.FileUtil.getTextFromAssets;

public class FruitProvider {

    private AssetManager am;
    private Document doc;

    public FruitProvider(AssetManager am){
        this.am = am;
        this.doc = Jsoup.parse(getTextFromAssets(am,"classification_header.xml"));
    }

    public ArrayList<String> getClasses(){
        Element root = doc.getElementsByTag("root").get(0);
        Elements classes = root.getElementsByTag("class");
        ArrayList<String> frclass = new ArrayList<>();
        for (Element element : classes){
            frclass.add(element.attr("name"));
        }
        return frclass;
    }

    public ArrayList<Fruit> getFruitsByClass(String frclass){
        Element root = doc.getElementsByTag("root").get(0);
        Elements classes = root.getElementsByTag("class");
        ArrayList<Fruit> fruits = new ArrayList<>();
        for (Element element : classes){
            if (element.attr("name").equals(frclass)){
                Elements items = element.getElementsByTag("item");
                for (Element item : items){
                    Fruit fruit = new Fruit();
                    fruit.setId(item.attr("id"));
                    fruit.setName(item.attr("name"));
                    fruit.setPrice(DoubleUtil.getDouble(item.attr("price")));
                    fruit.setImage(item.attr("image"));
                    fruits.add(fruit);
                }
            }
        }
        return fruits;
    }

    public Fruit getFruitById (String id){
        Element root = doc.getElementsByTag("root").get(0);
        Elements classes = root.getElementsByAttributeValue("id",id);
        Fruit fruit = new Fruit();
        fruit.setId(classes.get(0).attr("id"));
        fruit.setName(classes.get(0).attr("name"));
        fruit.setPrice(DoubleUtil.getDouble(classes.get(0).attr("price")));
        fruit.setImage(classes.get(0).attr("image"));

        return fruit;
    }
}
