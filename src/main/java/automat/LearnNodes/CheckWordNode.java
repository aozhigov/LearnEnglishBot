package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.Message;
import common.User;
import vocabulary.Selection;

import java.util.Hashtable;

public class CheckWordNode extends HandlerNode {
    private final Hashtable<String, Selection> vocabularies;

    public CheckWordNode(Hashtable<String, Selection> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Message action(String query, User user) {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        Selection vocabulary = vocabularies.get(user.stateLearn.getKey());

        if (query.contains("/hint")) {
            word = user.stateLearn.getValue().createHint();
            return move(Event.HINT).action(word);
        }


        if (vocabulary.checkTranslate(query, user)) {
            user.stateLearn.setValue(vocabulary.getEnWord(user));
            word = user.stateLearn.getValue().getEn();
            return move(Event.FIRST_EN_WORD).action(word);
        }

        return move(Event.TRY).action(word);
    }
}
