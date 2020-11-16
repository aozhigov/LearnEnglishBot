package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.Message;
import common.User;
import vocabulary.Selection;

import java.util.Hashtable;

public class YesNoNode extends HandlerNode {

    public YesNoNode() {
    }

    @Override
    public Message action(String query, User user) {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        if (query.equals("да")) {
            word = user.stateLearn.getValue().getEn();
            event = Event.SECOND_EN_WORD;
        } else {
            word = user.stateLearn.getValue().getRu();
            event = Event.RU_WORD;
        }

        return move(event).action(word);
    }
}
