package sevenWonders;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jan on 2017-02-01.
 */
public class University {
    public enum ResearchType {None, Wheel, Plate, Divider, Universal}

    private HashMap<ResearchType, Integer> researchFocus;

    public University() {
        researchFocus = new HashMap<>();
        researchFocus.put(ResearchType.Plate, 0);
        researchFocus.put(ResearchType.Wheel, 0);
        researchFocus.put(ResearchType.Divider, 0);
        researchFocus.put(ResearchType.Universal, 0);
    }

    public void addResearch(ResearchType type, int amount) {
        if (type == ResearchType.None)
            return;

        int alreadyInProgress = researchFocus.get(type);
        researchFocus.replace(type, amount + alreadyInProgress);
    }

    public int getMaxVictoryPoints() {
        int vp;
        ArrayList<Integer> vars = new ArrayList<>(3);
        vars.add(researchFocus.get(ResearchType.Wheel));
        vars.add(researchFocus.get(ResearchType.Plate));
        vars.add(researchFocus.get(ResearchType.Divider));
        int uni = researchFocus.get(ResearchType.Universal);

        vars.sort((a,b)-> (a - b));

        if (uni > 0) {
            vp = findBestScore(uni, vars);
        } else {
            vp = calculateScore(vars);
        }
        return vp;
    }

    private int findBestScore(int uni, ArrayList<Integer> vars) {
        int score1, score2;
        if (uni == 0) {
            return calculateScore(vars);
        } else {
            uni--;
            int min = vars.get(0);
            min++;
            ArrayList<Integer> vn = new ArrayList(vars);
            vn.remove(0);
            vn.add(0, min);
            vn.sort((a,b)->(a.intValue() - b.intValue()));
            score1 = findBestScore(uni, vn);

            ArrayList<Integer> v2 = new ArrayList(vars);
            int max = v2.get(2);
            max++;
            v2.remove(2);
            v2.add(2, max);
            v2.sort((a,b)->(a.intValue() - b.intValue()));
            score2 = findBestScore(uni, v2);

            vars.clear();
            if (score1 >= score2) {
                vars.addAll(vn);
                return score1;
            } else {
                vars.addAll(v2);
                return score2;
            }
        }
    }

    private int calculateScore(ArrayList<Integer> vars) {
        vars.sort((a, b) -> (a.intValue() - b.intValue()));
        int score;
        score = vars.get(0) * 7;
        score += (vars.get(0) * vars.get(0) + vars.get(1)*vars.get(1)+vars.get(2)*vars.get(2));
        return score;
    }
}
