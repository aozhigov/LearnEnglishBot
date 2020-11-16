package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.Message;
import common.User;
import vocabulary.Selection;

import java.util.Hashtable;

public class WrongNode extends HandlerNode {

    public WrongNode() {
    }

    @Override
    public Message action(String query, User user) {
        Event temp = checkCommand(query);
        String word = user.getName();

        if (temp != Event.NONE)
            return move(temp).action(word);

        if (query.equals("да") && user.stateLearn.getValue() != null) {
            word = user.stateLearn.getValue().getEn();
            return move(Event.FIRST_EN_WORD).action(word);
        }

        return move(Event.EXIT).action(word);
    }
}
