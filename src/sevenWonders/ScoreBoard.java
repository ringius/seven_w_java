package sevenWonders;

/**
 * Created by Jan on 2017-02-05.
 */
public class ScoreBoard {
    private int militaryScore;
    private int monetaryScore;
    private int wonderScore;
    private int civilianScore;
    private int commercialScore;
    private int scienceScore;
    private int guildScore;

    public void addMilitaryScore(int totalScore) {
        militaryScore = totalScore;
    }

    public void addMonetaryScore(int score)   {
        monetaryScore = score;
    }

    public void addWonderScore(int i) {
        this.wonderScore = i;
    }

    public void addCivialianScore(Integer score) {
        civilianScore = score;
    }

    public void addCommercialScore(Integer score ) {
        commercialScore = score;
    }

    public void addScienceScore(int score) {
        scienceScore = score;
    }

    public void addGuildScore(int score) {
        guildScore = score;
    }
}
