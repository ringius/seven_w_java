package sevenWonders;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Created by Jan on 2017-02-10.
 */
//Class containing player UI and interaction.
// Hopefully possible to isolate from game logic and put in client

public class GameUI extends Application {
    private ServerProxy server;
    private boolean     running;
    private Player      player; //da player
    private CardDeck    currentCards;
    private UIBasePanel root;
    //private CardDisplay cardDisplay;
    private Button      nextButton;

    private PlayerDisplay playerDisplay;

    public GameUI() {
        //cardDisplay = new CardDisplay(this);

        nextButton = new Button("Test");
        System.out.println("ui created");
    }

    public void start(Stage primaryStage) throws Exception {
        server = new ServerProxy();
        Wonder alex = Alexandria.getInstance().pick(true);
        player = new Player("janne", alex);
        player.setUI(this);
        Player david = new Player("David", Ephesus.getInstance().pick(true) );
        Player daniel = new Player("Daniel", Rhodes.getInstance().pick(true));

        server.registerPlayer(david);
        server.registerPlayer(daniel);
        server.registerPlayer(player);

        server.startGame();

        System.out.println("Start");
        root = new UIBasePanel(this);

        primaryStage.setTitle("Seven Wonders");
        primaryStage.setScene(new Scene(root, 900, 600));
        //root.setPrefSize(900, 600);

        nextButton.setOnMousePressed((e) -> endTurn());
        root.setButton(nextButton);
        //root.setPlayerDisplay(playerDisplay);
        primaryStage.show();
        server.setRunState(true);
    }

    private void endTurn() {
        root.removeButton(nextButton);
        server.stepTurn();
        updateCards();
        updatePlayer();
    }

    private void updateCards() {
        currentCards = server.getCardDeck();
        if (currentCards != null) {
            root.cardDisplay.clear();
            root.cardDisplay.displayCards(currentCards.getCardsAsList());
        }
    }

    private void updatePlayer() {
        root.updatePlayer();
    }

    public Player getPlayer() {
        return player;
    }

    public ServerProxy getServer() {
        return server;
    }

    public void selectCard(PlayCard playCard) {
        Solutions s = new Solutions();
        player.solveFinance(playCard, s);
        s.sortOnTotalCost();
        if (s.hasAffordableSolution()) {
            player.buyCard(playCard, s.getFirstAffordableSolution());
        } else {
            player.sellCard(playCard);
        }
        endTurn();
        updateCards();
        updatePlayer();
        System.out.println("Player: " + player.getCards());
        System.out.println("East Player: " + player.getEastPlayer().getCards());
        System.out.println("West Player: " + player.getWestPlayer().getCards());

        //server.printDecks();
    }

    public static void main(String[] args) {
        launch(args);
    }

    //public void setDeck(CardDeck deck) {
    //    this.currentCards = deck;
    //}
}
