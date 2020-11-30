package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;
import org.json.simple.parser.ParseException;
import vocabulary.Selection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class SetNameVocabularies extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) throws IOException, ParseException {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        user.setLastAddMyVocabularies(query);

        if (user.getStateLearn().getKey().equals("")) {
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
