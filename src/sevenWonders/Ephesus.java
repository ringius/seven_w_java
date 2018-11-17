package sevenWonders;

/**
 * Created by Jan on 2017-02-01.
 */
public class Ephesus extends MetaWonder {
    private static Ephesus theInstance = null;

    public static Ephesus getInstance() {
        if (theInstance == null)
            theInstance = new Ephesus();
        return theInstance;
    }

    protected Ephesus() {
        super("Ephesus");
    }

    protected Wonder generateA() {
        Wonder w = new Wonder(name, true, 3, baseA());
        CardList l = new CardList();
        l.add(a1());
        l.add(a2());
        l.add(a3());
        w.addLevels(l);
        return w;
    }

    protected Wonder generateB() {
        Wonder w = new Wonder(name, true, 3, baseB());
        CardList l = new CardList();
        l.add(b1());
        l.add(b2());
        l.add(b3());
        w.addLevels(l);
        return w;
    }

    private PlayCard baseA() {
        PlayCard p = new PlayCard("Ephesus Base A", PlayCard.CardType.Wonder, 0, 0);
        ResourceList l = new ResourceList();
        l.addResource(RawMaterial.type.Papyrus, 1);
        p.setResourceProduction(l);
        return p;
    }

    private PlayCard baseB() {
        PlayCard p = new PlayCard("Ephesus Base A", PlayCard.CardType.Wonder, 0, 0);
        ResourceList l = new ResourceList();
        l.addResource(RawMaterial.type.Papyrus, 1);
        p.setResourceProduction(l);
        return p;
    }

    private PlayCard a1() {
        PlayCard p = new PlayCard("Ephesus 1A", PlayCard.CardType.Wonder, 0, 0);
        ResourceList c = new ResourceList();
        c.addResource(RawMaterial.type.Stone, 2);
        p.setResourceCost(c);
        p.addAction(new ActionAddVictoryPoints(3));
        return p;
    }

    private PlayCard a2() {
        PlayCard p = new PlayCard("Ephesus 2A", PlayCard.CardType.Wonder, 0, 0);
        ResourceList c = new ResourceList();
        c.addResource(RawMaterial.type.Wood, 2);
        p.setResourceCost(c);
        p.addAction(new ActionAddMoney(9));
        return p;
    }

    private PlayCard a3() {
        PlayCard p = new PlayCard("Ephesus 3A", PlayCard.CardType.Wonder, 0, 0);
        ResourceList c = new ResourceList();
        c.addResource(RawMaterial.type.Papyrus, 2);
        p.setResourceCost(c);
        p.addAction(new ActionAddVictoryPoints(7));
        return p;
    }

    private PlayCard b1() {
        PlayCard p = new PlayCard("Ephesus 1B", PlayCard.CardType.Wonder, 0, 0);
        ResourceList c = new ResourceList();
        c.addResource(RawMaterial.type.Stone, 2);
        p.setResourceCost(c);
        p.addAction(new ActionAddVictoryPoints(2));
        p.addAction(new ActionAddMoney(4));
        return p;
    }

    private PlayCard b2() {
        PlayCard p = new PlayCard("Ephesus 2B", PlayCard.CardType.Wonder, 0, 0);
        ResourceList c = new ResourceList();
        c.addResource(RawMaterial.type.Wood, 2);
        p.setResourceCost(c);
        p.addAction(new ActionAddVictoryPoints(3));
        p.addAction(new ActionAddMoney(4));
        return p;
    }

    private PlayCard b3() {
        PlayCard p = new PlayCard("Ephesus 3B", PlayCard.CardType.Wonder, 0, 0);
        ResourceList c = new ResourceList();
        c.addResource(RawMaterial.type.Papyrus, 1);
        c.addResource(RawMaterial.type.Glass, 1);
        c.addResource(RawMaterial.type.Cloth, 1);
        p.setResourceCost(c);
        p.addAction(new ActionAddVictoryPoints(5));
        p.addAction(new ActionAddMoney(4));
        return p;
    }
}
