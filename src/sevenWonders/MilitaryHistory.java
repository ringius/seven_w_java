package sevenWonders;

/**
 * Created by Jan on 2017-02-01.
 */
public class MilitaryHistory {
    private static int EAST = 0;
    private static int WEST = 1;
    private int militaryStrength;
    private int age;
    private int totalScore;
    private int noOfDefeats;
    private int[][] scores;

    public MilitaryHistory() {
        totalScore = 0;
        noOfDefeats = 0;
        age = 0;
        scores = new int[2][4];
        militaryStrength = 0;
        resetScores();
    }

    private void resetScores() {
        for(int i=0; i < 2; i++)
            for (int j=0;j<4;j++)
                scores[i][j] = 0;
    }

    public void clear() {
        age = 0;
        totalScore = 0;
        noOfDefeats = 0;
        militaryStrength = 0;
        resetScores();

    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getNoOfDefats() {
        return noOfDefeats;
    }

    public void addStrength(int amount) {
        militaryStrength+=amount;
    }

    public int getMilitaryStrength() {
        return militaryStrength;
    }

    private int updateScore(int age, int col, int s) {
        int score = 0;
        int ts = 0;
        int winScore = (age) * 2 + 1; //age 0 => 1, age 1 => 2+1=3, age 2 => 2*2 + 1 = 4 + 1 = 5
        int loseScore = -1;

        if (s < militaryStrength) {
            score += winScore;
            ts = winScore;
        }
        else if (s > militaryStrength) {
            score += loseScore;
            ts = loseScore;
            noOfDefeats++;
        }
        scores[col][age] = ts;
        return score;
    }

    public int resolveCombat(int age, int westStrenth, int eastStrength) {
        int score = (updateScore(age, WEST, westStrenth) +  updateScore(age, EAST, eastStrength));
        totalScore += score;
        return score;
    }
}
