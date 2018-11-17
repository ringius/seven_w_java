package sevenWonders;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jan on 2017-01-11.
 */
public class ConditionalResourcesBank extends ArrayList<ResourceList> {

   public ConditionalResourcesBank () {
   }

   private ConditionalResourcesBank copyMinus(int indexToRemove) {
       ConditionalResourcesBank res = new ConditionalResourcesBank();
       for (int i=0;i<size();i++) {
           if (i != indexToRemove) {
               res.add(get(i));
           }
       }
       return res;
   }

   public void r2(ConditionalResourcesBank bank, Solutions sol) {
       if (bank.isEmpty()){
           return;
       }
       for (Solution s: sol.solutions) {
           if (!s.isAffordable()) {
               Solutions solutionsToAdd = new Solutions();
               ResourceList cost = s.getRemainingCost();
               for (RawMaterial.type t : cost.resources.keySet()) {
                   int amount = cost.resources.get(t);
                   for (int i = 0; i < bank.size(); i++) {
                       int availableItems = bank.get(i).getResourceAmount(t);
                       if (availableItems > 0) {
                           int noOfSols = Math.min(amount, availableItems);
                           while (noOfSols > 0) {
                               PlayCard c = bank.get(i).getCard();
                               ResourcePayment newPay = new ResourcePayment(t, noOfSols, c);
                               ResourcePayments rp = new ResourcePayments(s.getPayment());
                               rp.add(newPay);
                               ResourceList newCost = cost.copyMinus(t, noOfSols--);
                               solutionsToAdd.addSolution(new Solution(newCost, rp, newCost.isZero()));
                           }
                           r2(bank.copyMinus(i), solutionsToAdd);
                       }
                   }
               }
               if (!solutionsToAdd.empty()) {
                   s.setCascadedSolutions(solutionsToAdd);
               }
           }
       }
       sol.flatten();
   }

   public void getAllSolutions(PlayCard card, Solutions SolutionSet) {
//       for (Solution s: solutions.solutions) {
           //Solutions newS = new Solutions();
           if (SolutionSet.solutions.isEmpty())
            SolutionSet.addSolution(new Solution(card.getResourceCost(), new ResourcePayments(), false));
           //r_(s.getRemainingCost(), this,s.getPayment(), newS);
           r2(this, SolutionSet);
//       }
       SolutionSet.flatten();
   }
}
