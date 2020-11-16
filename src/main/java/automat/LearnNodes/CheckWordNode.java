package automat.LearnNodes;

import automat.HandlerNode;
import common.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class CheckWordNode extends HandlerNode {
    private final Hashtable<String, Selection> vocabularies;

    public CheckWordNode(Hashtable<String, Selection> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query, User user) {
        Event event = checkCommand(query);
        if (event != Event.NONE)
            return move(event).action(user.getName());

        Selection vocabulary = vocabularies.get(user.stateLearn.getKey());
        String word = "";

        if (query.contains("/hint")){
            event = Event.HINT;
            word = "-hint-";
            return move(event).action(word);
        }


        if (vocabulary.checkTranslate(query, user)){
            user.stateLearn.setValue(vocabulary.getEnWord(user));//.get(user.getNextIdWord(vocabulary.size())).en;
            word = user.stateLearn.getValue().getEn();
            event = Event.FIRST_EN_WORD;
            return move(event).action(word);
        }

        word = user.getName();
        event = Event.TRY;
        return move(event).action(word);
    }
}
