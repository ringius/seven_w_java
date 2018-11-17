package sevenWonders;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by Jan on 2017-02-01.
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,
              include = JsonTypeInfo.As.PROPERTY,
              property = "type")

@JsonSubTypes({
        @JsonSubTypes.Type(value = ActionAddVictoryPoints.class),
        @JsonSubTypes.Type(value = ActionAddMoney.class),
        @JsonSubTypes.Type(value = ActionAddMilitaryStrength.class),
        @JsonSubTypes.Type(value = ActionAddScienceEffort.class),
        @JsonSubTypes.Type(value = ActionAddTradePost.class),
        @JsonSubTypes.Type(value = ActionAddNotImplemented.class),
        @JsonSubTypes.Type(value = ActionCountAndAddMoney.class),
        @JsonSubTypes.Type(value = ActionCountAndAddVPs.class),
})

abstract public class Action {
    @JsonIgnore
    private PlayCard owner;

    public void setOwner(PlayCard owner) {
        this.owner = owner;
    }

    public PlayCard getOwner() {
        return owner;
    }

    public abstract boolean perform(Player p) throws Exception;
}


