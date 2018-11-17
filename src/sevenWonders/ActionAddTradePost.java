package sevenWonders;

/**
 * Created by Jan on 2017-02-02.
 */
public class ActionAddTradePost extends Action {
    private boolean tradeWest;
    private boolean tradeEast;
    private ResourceList cheaperGoods;

    public ActionAddTradePost() {
        tradeEast = false;
        tradeWest = false;
        cheaperGoods = null;
    }

    public ActionAddTradePost(boolean te, boolean tw, ResourceList cg) {
        super();
        this.tradeEast = te;
        this.tradeWest = tw;
        this.cheaperGoods = cg;
    }

    public void setTradeEast(boolean yes) {
        this.tradeEast = yes;
    }

    public void setTradeWest(boolean yes) {
        this.tradeWest = yes;
    }

    public boolean getTradeWest() {
        return tradeWest;
    }

    public boolean getTradeEast() {
        return tradeEast;
    }

    public void setCheaperGoods(ResourceList cg) {
        this.cheaperGoods = cg;
    }

    public ResourceList getCheaperGoods() {
            return cheaperGoods;
    }

    public boolean perform(Player player) {
        if (this.tradeEast) {
            Player ep = player.getEastPlayer();
            if (ep == null)
                return false;
            ResourceBank b = player.getTradeList(ep);
            for (RawMaterial.type t : this.cheaperGoods.getResources().keySet()) {
                b.addUnconditionalMaterial(t, 1);
            }
        }

        if (this.tradeWest) {
            Player wp = player.getWestPlayer();
            if (wp == null)
                return false;
            ResourceBank b = player.getTradeList(wp);
            for (RawMaterial.type t : this.cheaperGoods.getResources().keySet()) {
                b.addUnconditionalMaterial(t, 1);
            }
        }
        return true;
    }
}
