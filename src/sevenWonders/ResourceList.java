package sevenWonders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jan on 2017-01-10.
 */
public class ResourceList {
    private boolean conditionalSelection;
    private PlayCard card;
    protected HashMap<RawMaterial.type, Integer> resources;

    public ResourceList() {
        conditionalSelection = false;
        card = null;
        resources = new HashMap<RawMaterial.type, Integer>();
    }

    public ResourceList(ResourceList orig) {
        this();
        conditionalSelection = orig.conditionalSelection;
        card = orig.card;
        resources.putAll(orig.resources);
    }

    @JsonIgnore
    public void setCard(PlayCard c) {
        card = c;
    }

    @JsonIgnore
    public PlayCard getCard() {
        return card;
    }

    public void setConditionalSelection(boolean value) {
        conditionalSelection = value;
    }

    public boolean getConditionalSelection() { return conditionalSelection;}

    public void setResources(HashMap<RawMaterial.type, Integer> map) {
        resources = map;
    }

    public HashMap<RawMaterial.type, Integer> getResources() {
        return resources;
    }

    public Iterator<RawMaterial.type> iterator() {
        return resources.keySet().iterator();
    }

    public void addResource(RawMaterial.type type, int amount) {
        if (!resources.containsKey(type)) {
            resources.put(type, amount);
        } else {
            resources.replace(type, resources.get(type) + amount);
        }
    }

    public void updateBank(ResourceBank rb, PlayCard card) {
        if (card.getResourceCost().getConditionalSelection()) {
            rb.addConditionalCard(card);
        } else {
            rb.addCard(card);
        }
        /*
        for (Map.Entry<RawMaterial.type, Integer> entry : resources.entrySet()) {
            rb.addMaterial(entry.getKey(), entry.getValue());
            rb.addCard(entry.getKey(), card);
        }
        */
    }

    @JsonIgnore
    public int getResourceAmount(RawMaterial.type m) {
        if (!resources.containsKey(m)) {
            return 0;
        } else {
            return resources.get(m);
        }
    }

    @JsonIgnore
    public boolean isZero() {
        return resources.isEmpty();
    }
/*
    public void updateOptionalProduction(ConditionalResourcesBank bank) {
        bank.add(this);
    }
*/

    public ResourceList copyMinus(RawMaterial.type t) {
        return copyMinus(t, resources.get(t).intValue());
    }

    public ResourceList copyMinus(RawMaterial.type t, int amount) {
        ResourceList res = new ResourceList(this);
        Integer inBank = resources.get(t);
        if (inBank != null) {
            if(inBank.intValue() > amount) {
                res.resources.replace(t, inBank.intValue()-amount);
            } else {
                res.resources.remove(t);
            }
        }
        return res;
    }

    public void clear() {
        this.resources.clear();
        this.card = null;
        this.conditionalSelection = false;

    }

    public boolean equals(ResourceList other) {
        if (resources.size() != other.resources.size())
            return false;
        if (conditionalSelection != other.conditionalSelection)
            return false;
        if (card != other.card)
            return false;
        for(RawMaterial.type t : resources.keySet()) {
            if(other.resources.get(t) != resources.get(t))
                return false;
        }
        return true;
    }

    public boolean isSubsetOf(ResourceList o) {
        if (resources.size() > o.resources.size()) {
            return false;
        } else {
            int i=0;
            for (RawMaterial.type t : resources.keySet()) {
                int amount = resources.get(t);
                Integer oAmount = o.resources.get(t);
                if ( oAmount == null || oAmount.intValue() == 0 || oAmount.intValue() < amount) {
                    return false;
                }
                i++;
            }

            if (i < o.resources.size()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void subtract(RawMaterial.type materialType, int amount) {
        int total = resources.get(materialType);
        total -= amount;
        if (total <= 0) {
            resources.remove(materialType);
        } else {
            resources.put(materialType, total);
        }
    }

    public ResourceList minus(RawMaterial.type t, int i) {
        ResourceList r = new ResourceList(this);
        r.subtract(t,i);
        return r;
    }

    public static ResourceList statue() {
        ResourceList s1 = new ResourceList();
        s1.addResource(RawMaterial.type.Ore, 2);
        s1.addResource(RawMaterial.type.Wood, 1);
        return s1;
    }
    public String toString() {
        String res = "[";

        if (conditionalSelection) {
            for (Map.Entry<RawMaterial.type, Integer> e : resources.entrySet()) {
                res += RawMaterial.string(e.getKey()) + "|";
            }
            int lastpos = res.length()-1;
            if (lastpos > 0) {
                String r = res.substring(0, lastpos);
                r += (']');
                return r;
            } else {
                return "[]";
            }
        }

        for (Map.Entry<RawMaterial.type, Integer> e : resources.entrySet()) {
            res += (RawMaterial.string(e.getKey()) + ": " + e.getValue() + ",");
        }
        String r = new String();
        if (res.compareTo("[") == 0) {
            return "[]";
        } else {
            int lastpos = res.length()-1;
            r = res.substring(0, lastpos);
            r += "]";
            return r;
        }
    }

    public boolean isConditional() {
        return conditionalSelection;
    }
}
