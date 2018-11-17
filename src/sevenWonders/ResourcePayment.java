package sevenWonders;

/**
 * Created by Jan on 2017-01-23.
 */
public class ResourcePayment {
    private RawMaterial.type material;
    private int noOfItems;
    private PlayCard card;

    public ResourcePayment(RawMaterial.type material, int no, PlayCard payment) {
        this.material = material;
        this.noOfItems = no;
        this.card = payment;
    }

    public PlayCard getCard() {
        return card;
    }

    public int getAmount() {
        return noOfItems;
    }

    public RawMaterial.type getMaterialType() {
        return material;
    }

    public boolean equals(ResourcePayment o) {
        return (material == o.material && noOfItems == o.noOfItems && card == o.card);
    }

    public String toString() {
        return RawMaterial.string(material) + "(" + noOfItems + ") from " + card.getName();
    }
}
