package sevenWonders;

/**
 * Created by Jan on 2017-02-10.
 */

//For now fake just to check gui
public class ServerProxy {

    private GameController controller;
    private Player player;

    public ServerProxy() {
        player = null;
        controller = new GameController();
    }

    public void startGame() {
        try {
            controller.initializeGame();
        } catch (Exception e) {
            System.out.println("tough shit");
            e.printStackTrace();
        }
    }

    public CardDeck getCardDeck() {
        return controller.getCardDeck(player);
    }

    public void printDecks() {
        controller.printDecks();
    }

    public void registerPlayer(Player player) {
        this.player = player;
        controller.addPlayer(player);
    }

    public boolean getRunstate() {
        return controller.getRunstate();
    }

    public void setRunState(boolean newState) {
        controller.setRunstate(newState);
    }

    public void stepTurn() {
        controller.stepTurn();
    }

    public boolean sellCard(Player p, PlayCard c) {
        return controller.sellCard(p, c);
    }



    public boolean buyCard(Player p, PlayCard c, Solution payment) {
        return controller.buyCard(p, c, payment);
    }

    public void removeCardFromDeck(PlayCard card) {
        controller.removeCardFromDeck(card);
    }
}
