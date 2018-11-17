package sevenWonders;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Jan on 2017-02-01.
 */
public class GameController {
    private int currentAge;
    private int numberOfPlayers;

    private ArrayList<Player> players;
    //private ArrayList<GameUI> playerUi;
    CardReader reader;
    private CardDeck firstAge;
    private CardDeck secondAge;
    private CardDeck thirdAge;

    HashSet<PlayCard> scrapHeap;
    private ArrayList<CardDeck> currentDecks;
    private int firstPlayersCardDeck;
    private int turn;
    private boolean runstate;

    public GameController() {
        currentAge = 0;
        numberOfPlayers = 0;
        players = new ArrayList<>();
        firstAge = new CardDeck();
        secondAge = new CardDeck();
        thirdAge = new CardDeck();
        scrapHeap = new HashSet<>();
    }

    public boolean sellCard(Player player, PlayCard card) {
        if (removeCardFromDeck(card)) {
            scrapHeap.add(card);
            player.deposit(3);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean removeCardFromDeck(PlayCard card) {
        for (CardDeck d : currentDecks) {
            if (d.getCardsAsList().remove(card))
                return true;
        }
        return false;
    }
    
    public void addPlayer(Player p) {
        players.add(p);
        p.addController(this);
        p.initialize();
    }

    public boolean initializeGame() throws Exception {
        numberOfPlayers = players.size();
        if (numberOfPlayers < 3) {
            System.out.println("Too few players. Minimum number is 3.");
            return false;
        } else {
            initPlayers();
        }

        initCards();
        return true;
    }

    private boolean initPlayers() throws Exception {
        for (int i = 1; i < numberOfPlayers - 1; i++) {
            players.get(i).setNeighbours(players.get(i - 1), players.get(i + 1));
        }
        players.get(0).setNeighbours(players.get(numberOfPlayers - 1), players.get(1));
        players.get(numberOfPlayers - 1).setNeighbours(players.get(numberOfPlayers - 2), players.get(0));

        boolean okToGo = true;
        for (int i = 0; i < numberOfPlayers; i++) {
            if (!players.get(i).initialize()) {
                System.out.println("Error initializing players\n");
                okToGo = false;
            }
        }

        /*
        for (Player p : players) {
            p.executeCard();
        }
        */
        return okToGo;
    }

    private void initCards() {
        reader = new CardReader();
        reader.initDecks(numberOfPlayers);
    }

    public void gameLoop() {
        //age 1:
        playTurn(1);
        endAge(1);
        playTurn(2);
        endAge(2);
        playTurn(3);
        endAge(3);
        calculateVictoryPoints();
    }

    private void playTurn(int age) {

        currentDecks = reader.getDeck(age).split(numberOfPlayers);

        int firstPlayersCardDeck = 0;
        int turn = 0;
        while (currentDecks.get(0).size() > 1) {
            System.out.println("deck.get(0).size() = " + currentDecks.get(0).size());
            System.out.println("Playing turn " + (++turn));
            for (int i = 0; i < numberOfPlayers; i++) {
                int deck = (firstPlayersCardDeck + i) % numberOfPlayers;
                System.out.println("card deck " + deck + " to " + players.get(i).getName());
                //Eventually, this needs to be threaded and sent to remove application...
                players.get(i).play(currentDecks.get(deck));
            }

            executeCards();
            handleDelayedCards();

            firstPlayersCardDeck++;
            firstPlayersCardDeck = firstPlayersCardDeck%numberOfPlayers;
        }
        System.out.println("Number of cards remaining " +currentDecks.get(0).size());
    }

    /*
    'init age:
         * Blanda korten och dela in i högar
         * Nollställ firstPlayerCardDeck
         * Om age == 2, sätt direction till -1, annars 1.

    Step turn
        * Om antalet kort > 1:
        * firstPlayerCardDeck ++ % numberOfPlayers.
        * Dela ut korten.
        * Vänta på spelarinput.
     */

    private void initAge(int age) {
        if (reader == null)
            System.out.println("reader is null");
        CardDeck deck = reader.getDeck(age);

        currentDecks = reader.getDeck(age).split(numberOfPlayers);
        firstPlayersCardDeck = 0;
        turn = 0;
    }

    public void stepTurn() {

        //For testing purposes only, remove first card each deck.
        if (currentAge == 0) {
            currentAge++;
            initAge(currentAge);
            return;
        }

        if (currentAge > 3) {
            System.out.println("Game is over");
            return;
        }

        for (Player p: players) {
            if (p.getName().equals("David") || p.getName().equals("Daniel"))
                p.ai_evaluate(getCardDeck(p));

            //if (currentDecks != null && p.getName().equals("janne"))
            //    p.ai_evaluate(getCardDeck(p)); //for now, change to something better...
        }

        executeCards();
        handleDelayedCards();
        handleSpecialCards(currentAge);

        if (currentDecks.get(0).size() == 1) {
            //endTurn();
            currentAge++;
            if (currentAge > 3) {
                runstate = false;
                endGame();
            } else {
                initAge(currentAge);
            }
        }

        firstPlayersCardDeck = (firstPlayersCardDeck + 1) % numberOfPlayers;
    }

    private void endGame() {
    }

    public boolean playCard(Player p, PlayCard card) {
        //p.buyCard(card, p);
        stepTurn();
        return true;
    }

    private void executeCards() {
        for (Player p : players) {
            try {
                p.executeCard();
            } catch (Exception e) {
                System.out.println("Exception caught during player.endTurn: " + e);
                e.printStackTrace();
            }
        }
    }

    public void handleDelayedCards() {
        for (Player p : players) {
            try {
                p.handleDelayedActions();
            } catch (Exception e) {
                System.out.println("Exception caught during player.handleDelayedActions: " + e);
            }
        }
    }

    private void endAge(int age) {
        handleSpecialCards(age);
        resolveCombat(age);
    }

    private void handleSpecialCards(int age) {
        for (Player p: players) {
            p.handleEndOfAgeCards(age);
        }
    }

    private void resolveCombat(int age) {
        for (Player p: players) {
            p.resolveCombat(age);
        }
    }

    private void calculateVictoryPoints() {
        for (Player p : players) {
            p.executeDeferredActions();
            ScoreBoard sb = p.calculateVictoryPoints();
        }
    }

    public boolean getRunstate() {
        return runstate;
    }

    public void setRunstate(boolean runstate) {
        this.runstate = runstate;
    }

    public CardDeck getCardDeck(Player player) {
        int id = players.indexOf(player);
        if(id == -1)
            return null;
        else {
            int index = (firstPlayersCardDeck + id) % numberOfPlayers;
            return currentDecks.get(index);
        }
    }

    //Currently this transaction is handled directly by the player object, which is kind of fine,
    //but when using client-server, this functionality should be moved to the server side and thereby the controller
    //(believe that a better solution is to make the player class much thinner than is the case today, at least client
    // side.

    public boolean buyCard(Player p, PlayCard c, Solution payment)
    {
        return removeCardFromDeck(c);
    }

    public void printDecks() {
        for (CardDeck d : currentDecks) {
            for (PlayCard c : d.getCardsAsList()) {
                System.out.print(c.getName() + ", ");
            }
            System.out.print("\n");
        }
    }
}




