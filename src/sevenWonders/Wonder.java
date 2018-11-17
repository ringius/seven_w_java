package sevenWonders;

/**
 * Created by Jan on 2017-01-08.
 */
public class Wonder {
    private final boolean A_side;
    private int currentLevel;
    private String name;
    private int noOfLevels;
    private PlayCard startCard;
    private CardList levelCards;
    private CardList stashedCards;

    public Wonder(String name, boolean A_side, int noOfLevels, PlayCard startCard) {
        this.currentLevel = 0;
        this.name = name;
        this.A_side = A_side;
        this.noOfLevels = noOfLevels;
        this.startCard = startCard;
        levelCards = null;
        stashedCards = new CardList();
    }

    public PlayCard upgrade(PlayCard stashedCard) {
        if (currentLevel == noOfLevels) {
            return null;
        }
        stashedCards.add(currentLevel, stashedCard);
        currentLevel++;
        return levelCards.get(currentLevel - 1);
    }

    public void addLevels(CardList levelCards) {
        this.levelCards = levelCards;
    }

    void updatePlayer(Player p) throws Exception {
        startCard.updatePlayer(p); //Need to change to updatePlayer(player, boolean:isPrivate);
    }

    public String toString() {
        String res = new String(name);
        if (A_side)
            res += " (A)";
        else
            res += " (B)";
        return res;
    }

    public PlayCard getStartCard() {
        return startCard;
    }

    public String getFileName() {
        if (this.A_side)
            return name+"_a.png";
        else
            return name +="_b.png";
    }

}
