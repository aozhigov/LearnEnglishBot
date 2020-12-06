package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;
import vocabulary.Selection;

import java.util.ArrayList;

public class SetNameVocabularies extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word = user.getName();
        Event event;

        if (user.getUserVocabularies().containsKey(query))
            user.setUserVocabulary("My_" + user.getUserVocabularies() + "_" + query);
        else
            user.setUserVocabulary(query);

        if (user.getStateLearn().getKey().equals("")) {
            event = Event.CHANGE_TOPIC;
            return move(event).action(word,
                    new ArrayList<>(user.getUserVocabularies().keySet()));
        } else {
            word = getFirstWord(query, user);
            event = Event.FIRST_EN_WORD;
        }

        return move(event).action(word);
    }
}
