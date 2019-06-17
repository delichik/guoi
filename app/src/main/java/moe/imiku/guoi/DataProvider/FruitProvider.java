package moe.imiku.guoi.DataProvider;

import android.content.res.AssetManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import moe.imiku.guoi.Model.Fruit;

import static moe.imiku.guoi.Util.FileUtil.getTextFromAssets;

public class FruitProvider {
    private AssetManager am;
    public FruitProvider(AssetManager am){
        this.am = am;
    }
    public ArrayList<String> getClasses(){
        Document doc = Jsoup.parse(getTextFromAssets(am,"classification_header.xml"));
        Element root = doc.getElementsByTag("root").get(0);
        Elements classes = root.getElementsByTag("class");
        ArrayList<String> frclass = new ArrayList<>();
        for (Element element : classes){
            frclass.add(element.attr("name"));
        }
        return frclass;
    }
    public ArrayList<Fruit> getFruitsByClass(String frclass){
        Document doc = Jsoup.parse(getTextFromAssets(am,"classification_header.xml"));
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
                    fruit.setPrice(Double.parseDouble(item.attr("price")));
                    fruit.setImage(item.attr("image"));
                    fruits.add(fruit);
                }
            }
        }
        return fruits;
    }
}
