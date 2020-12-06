package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;

public class ChoseTopicNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word = user.getName();
        Event event;

        if (user.getUserVocabularies().containsKey(query)) {
            word = getFirstWord(query, user);
            event = Event.FIRST_EN_WORD;
        } else
            event = Event.WRONG_TOPIC;

        return move(event).action(word);
    }
}
