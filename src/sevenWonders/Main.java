package sevenWonders;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sevenWonders.iml"));

        Pane root = new Pane();
        primaryStage.setTitle("Seven Wonders");
        primaryStage.setScene(new Scene(root, 1600, 1200));
        primaryStage.show();
        test();
        testGraphics(root);
    }

    public static void testGraphics(Pane root) {
    }

    public static void test() {
        Player janne = new Player("Janne", Ephesus.getInstance().pick(true));

        GameController ctrl = new GameController();
        ctrl.addPlayer(janne);
        try {
            ctrl.initializeGame();
            //ctrl.gameLoop();
        } catch(Exception e) {
            System.out.println("Exception " + e);
        }
        System.out.println("done");

        //System.exit(0);
    }

    private static void printResult(PlayCard card, boolean result, ResourceList r, Solutions s) {
        System.out.println("Result of canBuy (" + card.getName() + ") = " + result);
        System.out.println("Possible solutions (and non-solutions)\n" + s);
        System.out.println("Remaining Material: " + r);
    }

    public static void main(String[] args) {
        GameUI ui = new GameUI();
        ui.launch(args);
    }
}

//////////////////////////////
/*
        players.add(0, david);
        players.add(1, janne);
        players.add(2, daniel);

        PlayCard statue = PlayCard.statue();            //cost, ore:2 & wood:1
        PlayCard stonePit = PlayCard.stonePit();        //stone:2
        PlayCard treeFarm = PlayCard.treeFarm();        //wood:1 | stone:1
        PlayCard treeFarm2 = PlayCard.treeFarm2();        //wood:1 | stone:1
        PlayCard phoneyFarm = PlayCard.phoneyFarm();    //wood:1 | ore:1
        PlayCard foundry = PlayCard.foundry();          //ore:2
        PlayCard foundry2 = PlayCard.foundry();
        foundry2.setName("Davids ore");
        PlayCard sawMill = PlayCard.sawmill();          //wood:2
        PlayCard timber = PlayCard.sawmill();          //wood:2
        timber.setName("Davids trees");

        //PlayCard alex0a = PlayCard.alex0A();

        janne.addPendingCard(foundry);
        janne.addPendingCard(sawMill);
        sawMill.setName("kalle kula");
        david.addPendingCard(timber);
        david.addPendingCard(treeFarm2);
        david.addPendingCard(foundry2);

        daniel.addPendingCard(PlayCard.sawmill());
        //daniel.addPendingCard(PlayCard.sawmill());
        janne.addPendingCard(PlayCard.treeFarm());        //wood:1 | stone:1
        System.out.println(janne + "\n");
        System.out.println(david + "\n");
        System.out.println(daniel + "\n");

        /*
        boolean result;
        Solutions s = new Solutions();



        //janne.canBuy(statue, s);
        //janne.buyCard(wtp, null);
//        janne.buyCard(etp, null);

        result = janne.solveFinance(statue, s);


        //Let's try to buy solution number 3...
        //Solution toBuy = s.solutions.get(3);
        s.sortOnTotalCost();
        System.out.println("** * * Solutions\n" + s);
        Solution toBuy = s.solutions.get(0);
        System.out.println("Buy card using solution: " + toBuy);
        janne.buyCard(statue, toBuy);

        Solutions s2 = new Solutions();

        System.out.println(janne);
        System.out.println(david);
        System.out.println(daniel);
*/




