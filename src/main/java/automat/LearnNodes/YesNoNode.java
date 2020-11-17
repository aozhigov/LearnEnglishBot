package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;

public class YesNoNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        if (query.equals("да")) {
            word = user.getStateLearn().getValue().getEn();
            event = Event.SECOND_EN_WORD;
        } else {
            word = user.getStateLearn().getValue().getRu();
            event = Event.RU_WORD;
        }

        return move(event).action(word);
    }
}
