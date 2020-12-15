package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import User.User;

import java.util.ArrayList;

public class SetNameVocabulariesNode extends HandlerNode {

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

        if (query.equals("удалить")
                || (word.equals("") && user.getSizeAddVocabulary() == 0)) {
            user.delUserVocabulary();
            event = Event.HELP;

            if (user.getStateLearn().getValue() != -1) {
                word = user.getCurrentLearnWord().getEn();
                event = Event.FIRST_EN_WORD;
            }

            if (user.getStateLearn().getKey().equals("")) {
                event = Event.CHANGE_TOPIC;
                return move(event).action(user.getName(),
                        new ArrayList<>(user.getUserVocabularies().keySet()));
            }
            return move(event).action(word);
        }

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
