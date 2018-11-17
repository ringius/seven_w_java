package sevenWonders;

/**
 * Created by Jan on 2017-02-02.
 */
public class CostItem {
    private RawMaterial.type mtrl;
    private int cost;

    public CostItem(RawMaterial.type t, int c) {
        mtrl = t;
        cost = c;
    }

    public CostItem() {
        mtrl = RawMaterial.type.Undefined;
        cost = 0;
    }

    public void setMtrl(RawMaterial.type t) {
        mtrl = t;
    }

    public void setCost(int c) {
        cost = c;
    }

    public RawMaterial.type getMtrl() {
        return mtrl;
    }

    public int getCost() {
        return cost;
    }
}
