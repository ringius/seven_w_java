package sevenWonders;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Jan on 2017-01-23.
 */
public class Solutions {
    boolean costCalculated;
    ArrayList<Solution> solutions;

    public Solutions() {
        solutions = new ArrayList<>();
        costCalculated = false;
    }

    public void addSolution(Solution solution) {
        solutions.add(solution);
    }

    public String toString() {
        String res = new String();
        for (Solution s: solutions) {
            res += s.toString() + "\n";
        }
        return res;
    }

    public boolean empty() {
        return solutions.size() == 0;
    }

    public boolean hasAffordableSolution() {
            return getFirstAffordableSolution() != null;
    }

    public Solution getFirstAffordableSolution() {
        for (Solution s : solutions) {
            if (s.isAffordable()) {
                return s;
            }
        }
        return null;
    }

    public void clear() {
        solutions.clear();
    }

    public Iterator<Solution> solutionIterator() {
        return solutions.iterator();
    }


    public void removeCopies() {
        ArrayList<Solution> toRemove = new ArrayList<>();
        for (int i=0;i<solutions.size()-1;i++) {
            for (int j = i+1; j < solutions.size(); j++) {
                if (solutions.get(i).equals(solutions.get(j))) {
                    toRemove.add(solutions.get(j));
                }
            }
        }
        solutions.removeAll(toRemove);
    }

    public void removeSupersets() {
        return;
        /*
        ArrayList<Solution> toRemove = new ArrayList<>();
        for (int i=0;i<solutions.size();i++) {
            for (int j=0; j< solutions.size(); j++) {
                if (i != j && solutions.get(i).isSubsetOf(solutions.get(j))) {
                    toRemove.add(solutions.get(j));
                }
            }
        }
        solutions.removeAll(toRemove);
        */
    }

    //Remove non-solutions if there is at least one affordable solution.
    public void stripNonSolutions() {
        return;
        /*
        if (getFirstAffordableSolution() != null) {
            ArrayList<Solution> toRemove = new ArrayList<>();
            for (Solution s : solutions)
                if (!s.isAffordable())
                    toRemove.add(s);
            solutions.removeAll(toRemove);
        }
        */
    }

    public void sortOnTotalCost() {

        solutions.sort((one, two)-> (one.getTotalCost() - two.getTotalCost()));
    }

    //Remove cascading by merging payments from upper levels downward, copying as you go...
    public void flatten() {
        for (int i=0;i<solutions.size();i++) {
            Solution s = solutions.get(i);
            if (s.isAffordable()) {
                //System.out.println("Affordable: " + s);
            } else {
              //  boolean removeOrg = false;
                Solutions sols = s.getCascadedSolutions();
                if (sols != null) {
                    sols.flatten();
                    for (Solution s2 : sols.solutions) {
                        addSolution(s2);
                    }
                }
            }
            s.setCascadedSolutions(null);
        }
    }

    public String textDescription() {
        String res = new String();
        for (Solution s: solutions) {
            res += s.textDescripton() + "\n";
        }
        return res;
    }

    public void cleanup() {
        removeCopies();
        removeSupersets();
        stripNonSolutions();
        sortOnTotalCost();
    }
}
