package sevenWonders;

/**
 * Created by Jan on 2017-01-09.
 */
public class RawMaterial {
    public enum type {Undefined, Wood, Stone, Clay, Ore, Cloth, Glass, Papyrus};

    public static String string(type t) {
        switch(t) {
            case Wood:
                return "Wood";
            case Stone:
                return "Stone";
            case Clay:
                return "Clay";
            case Ore:
                return "Ore";
            case Cloth:
                return "Cloth";
            case Glass:
                return "Glass";
            case Papyrus:
                return "Papyrus";
            case Undefined:
            default:
                return "Undefined";
        }
    }

    private RawMaterial.type t;

    public RawMaterial(RawMaterial.type t) {
        this.t = t;
    }

    public RawMaterial() {
        t = RawMaterial.type.Undefined;
    }

    public RawMaterial.type getType() {
        return this.t;
    }

    public void setType(RawMaterial.type t) {
        this.t = t;
    }
}
