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

public class ExitOrNextNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        Event event = checkCommand(query, user);
        String word = user.getName();

        if (event == Event.CHANGE_TOPIC){
            int count = user.getMyVocabularies().size() / 2;
            if (user.getMyVocabularies().size() % 2 != 0)
                count++;

            return move(event).action(word,
                    new KeyboardBot(count, new ArrayList<>(user.getMyVocabularies().keySet())));
        }

        if (event != Event.NONE)
            return move(event).action(word);

        if (!query.equals("да"))
            event = Event.EXIT;
        else if (user.getStateLearn().getKey().equals("")){
            event = Event.CHANGE_TOPIC;
        }
        else{
            Selection vocabulary = user.getMyVocabularies().get(user.getStateLearn().getKey());
            user.setStateLearn(vocabulary.getEnWord(user));
            word = user.getStateLearn().getValue().getEn();
            event = Event.FIRST_EN_WORD;
        }

        return move(event).action(word);
    }
}
