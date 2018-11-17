package sevenWonders;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Jan on 2017-02-24.
 */
public class ResourceDisplay extends GridPane {
    //Initially only display non-conditionals

    ResourceDisplayRow wood;
    ResourceDisplayRow ore;
    ResourceDisplayRow clay;
    ResourceDisplayRow stone;
    ResourceDisplayRow glass;
    ResourceDisplayRow papyrus;
    ResourceDisplayRow cloth;

    HashMap<RawMaterial.type, ResourceDisplayRow> rows;
    private HashMap<Integer, ArrayList<Label>> dataMatrix;

    public ResourceDisplay() {
        rows = new HashMap<>();
        dataMatrix = new HashMap();

        wood = new ResourceDisplayRow(this, RawMaterial.type.Wood,       "Wood\t    ", 0);
        ore = new ResourceDisplayRow(this, RawMaterial.type.Ore,         "Ore\t     ", 1);
        clay = new ResourceDisplayRow(this, RawMaterial.type.Clay,       "Clay\t    ", 2);
        stone = new ResourceDisplayRow(this, RawMaterial.type.Stone,     "Stone\t   ", 3);
        glass = new ResourceDisplayRow(this, RawMaterial.type.Glass,     "Glass\t   ", 4);
        papyrus = new ResourceDisplayRow(this, RawMaterial.type.Papyrus, "Papyrus ", 5);
        cloth = new ResourceDisplayRow(this, RawMaterial.type.Cloth,     "Cloth\t   ", 6);
        rows.put(RawMaterial.type.Wood, wood);
        rows.put(RawMaterial.type.Ore, ore);
        rows.put(RawMaterial.type.Clay, clay);
        rows.put(RawMaterial.type.Stone, stone);
        rows.put(RawMaterial.type.Glass, glass);
        rows.put(RawMaterial.type.Papyrus, papyrus);
        rows.put(RawMaterial.type.Cloth, cloth);

        for (ResourceDisplayRow r:rows.values())
            r.addToGrid();

        update(null);
    }

    public void update(Player p) {
        if (p != null) {
            for (ResourceDisplayRow d : rows.values()) {
                d.updatePublic(p.getRawMaterialsPublic());
                d.updatePrivate(p.getRawMaterialsPrivate());
            }

            //For now, clump private and public cards together - needs to be changed...
            //HashMap<RawMaterial.type, ArrayList<PlayCard>> sTemp = new HashMap<>();
            HashSet<PlayCard> sTemp = new HashSet<>();

            Collection<ArrayList<PlayCard>> c = p.getPublicConditionalCards().values();
            for (ArrayList<PlayCard> a : c)
                for (PlayCard card: a)
                    sTemp.add(card);

            System.out.println("public conditional = " + sTemp);


            c = p.getPrivateConditionalCards().values();
            for (ArrayList<PlayCard> a : c)
                for (PlayCard card: a)
                    sTemp.add(card);

            System.out.println("all conditional = " + sTemp);

            HashSet<PlayCard> s1 = new HashSet<>();
            for (PlayCard  card : sTemp) {
                    s1.add(card);
            }
            System.out.println("Display following cards:\n" + s1);
            int col = 3, index = 0;
            for (PlayCard card : s1) {
                clearColumn(col);
                for (RawMaterial.type t : card.getResourceProduction().getResources().keySet()) {
                    setOne(t, col);
                }
                col++;
            }
        }
    }

    private void setOne(RawMaterial.type type, int col) {
        rows.get(type).setOne(col);
    }

    private void clearColumn(int colIndex) {
        for (ResourceDisplayRow r : rows.values())
            r.clearColumn(colIndex);
    }
}