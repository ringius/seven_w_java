package sevenWonders;

/**
 * Created by Jan on 2017-02-01.
 */
public class Alexandria extends MetaWonder {
    private static Alexandria theInstance;

    public static Alexandria getInstance() {
        if (theInstance == null)
            theInstance = new Alexandria();
        return theInstance;
    }

    protected Alexandria() {
        super("Alexandria");
        //a = generateA();
        //b = generateB();
    }

    protected Wonder generateA() {
        Wonder w = new Wonder(this.name, true, 3, base());
        CardList lCards = new CardList();
        lCards.add(alex1A());
        lCards.add(alex2A());
        lCards.add(alex3A());

        System.out.println(lCards);
        w.addLevels(lCards);
        return w;
    }

    protected Wonder generateB() {
        Wonder w = new Wonder(this.name, false, 3, base());
        CardList l = new CardList();
        l.add(alex1B());
        l.add(alex2B());
        l.add(alex3B());
        w.addLevels(l);
        return w;
    }

    private PlayCard base() {
        PlayCard p = new PlayCard("Alexandria Base", PlayCard.CardType.Wonder, 0, 0);
        ResourceList prod = new ResourceList();
        prod.addResource(RawMaterial.type.Glass, 1);
        //prod.addResource(RawMaterial.type.Stone, 2); //testing only
        prod.setConditionalSelection(false);
        p.setResourceProduction(prod);
        return p;
    }

    public PlayCard alex1A() {
        PlayCard p = new PlayCard("Alexandria A1", PlayCard.CardType.Wonder, 1, 0);
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Stone, 2);
        p.addAction(new ActionAddVictoryPoints(3));
        p.setResourceCost(cost);
        return p;
    }

    public PlayCard alex1B() {
        PlayCard p = new PlayCard("Alexandria B1", PlayCard.CardType.Wonder, 2, 0);
        ResourceList cost = new ResourceList();
        ResourceList prod = new ResourceList();
        cost.addResource(RawMaterial.type.Clay, 2);
        cost.setConditionalSelection(false);
        prod.addResource(RawMaterial.type.Clay, 1);
        prod.addResource(RawMaterial.type.Ore, 1);
        prod.addResource(RawMaterial.type.Wood, 1);
        prod.addResource(RawMaterial.type.Stone, 1);
        prod.setConditionalSelection(true);
        p.setResourceCost(cost);
        p.setResourceProduction(prod);
        return p;
    }


    public PlayCard alex2A() {
        PlayCard p = new PlayCard("AlexandriaA2", PlayCard.CardType.Wonder, 2, 0);
        ResourceList cost = new ResourceList();
        ResourceList prod = new ResourceList();
        cost.addResource(RawMaterial.type.Ore, 2);
        cost.setConditionalSelection(false);
        prod.addResource(RawMaterial.type.Clay, 1);
        prod.addResource(RawMaterial.type.Ore, 1);
        prod.addResource(RawMaterial.type.Wood, 1);
        prod.addResource(RawMaterial.type.Stone, 1);
        prod.setConditionalSelection(true);
        p.setResourceCost(cost);
        p.setResourceProduction(prod);
        return p;
    }

    public PlayCard alex2B() {
        PlayCard p = new PlayCard("Alexandria B2", PlayCard.CardType.Wonder, 2, 0);
        ResourceList cost = new ResourceList();
        ResourceList prod = new ResourceList();
        cost.addResource(RawMaterial.type.Wood, 2);
        cost.setConditionalSelection(false);
        prod.addResource(RawMaterial.type.Glass, 1);
        prod.addResource(RawMaterial.type.Cloth, 1);
        prod.addResource(RawMaterial.type.Papyrus, 1);
        prod.setConditionalSelection(true);
        p.setResourceCost(cost);
        p.setResourceProduction(prod);
        return p;
    }

    public static PlayCard alex3A() {
        PlayCard p = new PlayCard("Alexandria A3", PlayCard.CardType.Wonder, 2, 0);
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Glass, 2);
        cost.setConditionalSelection(false);
        p.addAction(new ActionAddVictoryPoints(7));
        p.setResourceCost(cost);
        return p;
    }

    public static PlayCard alex3B() {
        PlayCard p = new PlayCard("Alexandria B3", PlayCard.CardType.Wonder, 2, 0);
        ResourceList cost = new ResourceList();
        cost.addResource(RawMaterial.type.Stone, 3);
        cost.setConditionalSelection(false);
        p.addAction(new ActionAddVictoryPoints(7));
        p.setResourceCost(cost);
        return p;
    }
}
