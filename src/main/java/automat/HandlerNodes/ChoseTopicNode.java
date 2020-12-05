package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.KeyboardBot;
import common.MessageBot;
import common.User;
import vocabulary.Selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class ChoseTopicNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        Event event = checkCommand(query, user);
        String word = user.getName();

        if (event == Event.CHANGE_TOPIC){
            return move(event).action(word,
                    new KeyboardBot(new ArrayList<>(user.getMyVocabularies().keySet())));
        }

        if (event != Event.NONE)
            return move(event).action(word);

        if (user.getMyVocabularies().containsKey(query)) {
            user.setStateLearn(query);
            Selection vocabulary = user.getMyVocabularies().get(user.getStateLearn().getKey());
            user.setStateLearn(vocabulary.getEnWord(user));
            word = user.getStateLearn().getValue().getEn();
            event = Event.FIRST_EN_WORD;
        } else
            event = Event.WRONG_TOPIC;

        return move(event).action(word);
    }
}
