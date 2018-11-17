package sevenWonders;

import sevenWonders.RawMaterial.type;

import java.util.ArrayList;

/**
 * Created by Jan on 2017-02-02.
 */
public class FileCardEntry {
    int id;
    private String name;
    private int age;
    private PlayCard.CardType cardType;
    private ArrayList<type> prodMtrls;
    //private ArrayList<Integer> prodCosts;
    private ArrayList<CostItem> prodCosts;


    public FileCardEntry() {
    }
    public int getId() {
        return id;
    }

    public FileCardEntry(int id, String name, int age, PlayCard.CardType t) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.cardType = t;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setId(int id) {
        this.id =id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCardType(PlayCard.CardType t) {
        this.cardType = t;
    }

    public PlayCard.CardType getCardType() {
        return cardType;
    }

    public void setProdMtrls(ArrayList<type> p) {
        prodMtrls = p;
    }
    public ArrayList<type> getProdMtrls() {
        return prodMtrls;
    }

    public void setProdCost(ArrayList<CostItem> c) {
        prodCosts = c;
    }

    public ArrayList<CostItem> getProdCosts() {
        return prodCosts;
    }
}
