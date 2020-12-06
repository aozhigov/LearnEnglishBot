package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;

public class ExitOrNextNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word = user.getName();
        Event event;

        if (!query.equals("да"))
            event = Event.EXIT;
        else if (user.getStateLearn().getKey().equals("")) {
            event = Event.CHANGE_TOPIC;
        } else {
            word = getFirstWord(query, user);
            event = Event.FIRST_EN_WORD;
        }

        return move(event).action(word);
    }
}
