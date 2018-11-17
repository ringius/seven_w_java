package sevenWonders;

/**
 * Created by Jan on 2017-02-01.
 */

public class Babylon extends MetaWonder {
    private static Babylon theInstance = null;

    public static Babylon getInstance() {
        if (theInstance == null)
            theInstance = new Babylon();
        return theInstance;
    }

    protected Babylon() {
        super("Babylon");
    }

    protected Wonder generateA() {
        Wonder w = new Wonder(name, true, 3, base());
        CardList l = new CardList();
        l.add(a1());
        l.add(a2());
        l.add(a3());
        w.addLevels(l);
        return w;
    }

    protected Wonder generateB() {
        Wonder w = new Wonder(name, false, 3, base());
        CardList l = new CardList();
        l.add(a1());
        l.add(a2());
        l.add(a3());
        w.addLevels(l);
        return w;
    }

    private PlayCard base() {
        PlayCard p = new PlayCard("Babylon Base ", PlayCard.CardType.Wonder, 0, 0);
        ResourceList prod = new ResourceList();
        prod.addResource(RawMaterial.type.Clay, 1);
        p.setResourceProduction(prod);
        return p;
    }

    private PlayCard a1() {
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Clay, 2);
        PlayCard p = new PlayCard("Babylon 1A", PlayCard.CardType.Wonder, 0, 0);
        p.setResourceCost(cost);
        p.addAction(new ActionAddVictoryPoints(3));
        return p;
    }

    private PlayCard a2() {
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Wood, 3);
        PlayCard p = new PlayCard("Babylon 2A", PlayCard.CardType.Wonder, 0, 0);
        p.addAction(new ActionAddScienceEffort(University.ResearchType.Universal, 1));
        return p;
    }


    private PlayCard a3() {
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Clay, 4);
        PlayCard p = new PlayCard("Babylon 3A", PlayCard.CardType.Wonder, 0, 0);
        p.setResourceCost(cost);
        p.addAction(new ActionAddVictoryPoints(7));
        return p;
    }
}
