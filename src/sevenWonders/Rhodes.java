package sevenWonders;

/**
 * Created by Jan on 2017-02-01.
 */
public class Rhodes extends MetaWonder {
    private static Rhodes theInstance = null;

    public static Rhodes getInstance() {
            theInstance = new Rhodes();
        return theInstance;
    }

    protected Rhodes() {
        super("Rhodes");
    }

    protected Wonder generateA() {
        Wonder w = new Wonder(this.name, true, 3, base());
        CardList l = new CardList();
        l.add(a1());
        l.add(a2());
        l.add(a3());
        w.addLevels(l);
        return w;
    }

    protected Wonder generateB() {
        Wonder w = new Wonder(this.name, false, 2, base());
        CardList l  = new CardList();
        l.add(b1());
        l.add(b2());
        return w;

    }

    protected PlayCard base() {
        PlayCard p = new PlayCard("Rhodes Base ", PlayCard.CardType.Wonder, 0, 0);
        ResourceList prod = new ResourceList();
        prod.addResource(RawMaterial.type.Ore, 1);
        p.setResourceProduction(prod);
        return p;
    }

    private PlayCard b1() {
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Stone, 3);
        PlayCard p = new PlayCard("Rhodes 1B", PlayCard.CardType.Wonder, 0, 0);
        p.setResourceCost(cost);
        p.addAction(new ActionAddVictoryPoints(3));
        p.addAction(new ActionAddMoney(3));
        return p;
    }

    private PlayCard b2() {
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Ore, 4);
        PlayCard p = new PlayCard("Rhodes 2B", PlayCard.CardType.Wonder, 0, 0);
        p.setResourceCost(cost);
        p.addAction(new ActionAddMilitaryStrength(1));
        p.addAction(new ActionAddVictoryPoints(4));
        p.addAction(new ActionAddMoney(4));
        return p;
    }

    private PlayCard a1() {
        PlayCard p = new PlayCard("Rhodes 1A", PlayCard.CardType.CivilianStructure, 0, 0);
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Wood, 2);
        p.setResourceCost(cost);
        p.addAction(new ActionAddVictoryPoints(3));
        return p;
    }

    private PlayCard a2() {
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Clay, 3);
        PlayCard p = new PlayCard("Rhodes 2A", PlayCard.CardType.Wonder, 0, 0);
        p.addAction(new ActionAddMilitaryStrength(2));
        return p;
    }

    private PlayCard a3() {
        PlayCard p = new PlayCard("Rhodes 3A", PlayCard.CardType.CivilianStructure, 0, 0);
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Ore, 4);
        p.setResourceCost(cost);
        p.addAction(new ActionAddVictoryPoints(7));
        return p;
    }
}
