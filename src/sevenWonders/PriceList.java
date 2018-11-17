package sevenWonders;

/**
 * Created by Jan on 2017-01-23.
 */

/** Class for holding the materials that are exportable by one neighbour, and the
 * prices the current player have to pay for each item.
 */


public class PriceList {
    private Player exportingPlayer;
    private Player owner;

    public PriceList(Player owner, Player exportingPlayer) {
        this.owner = owner;
        this.exportingPlayer = exportingPlayer;
    }

    public int canExport(RawMaterial.type mtrl) {
        return exportingPlayer.getExportableGoods().getAmountOfMaterial(mtrl);
    }

    private int checkPrice(RawMaterial.type type) {
        if (canExport(type) > 0 && owner.getTradeList(exportingPlayer).getAmountOfMaterial(type) > 0) {
            return 1;
        } else {
            return 2;
        }
    }
}
