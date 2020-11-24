package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;
import vocabulary.Selection;
import vocabulary.Word;

import java.util.Hashtable;

public class CheckWordNode extends HandlerNode {
    private final Hashtable<String, Selection> vocabularies;

    public CheckWordNode(Hashtable<String, Selection> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public MessageBot action(String query, User user) {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        if (query.contains("закончить")) {
            return move(Event.EXIT).action(word);
        }

        event = Event.TRY;
        Selection vocabulary = vocabularies.get(user.getStateLearn().getKey());

        if (query.contains("подсказка")) {
            word = user.getStateLearn().getValue().createHint();
            event = Event.HINT;
        }
        else if (vocabulary.checkTranslate(query, user)) {
            Word temp = vocabulary.getEnWord(user);
            user.setStateLearn(temp);
            word = user.getStateLearn().getValue().getEn();
            event = Event.FIRST_EN_WORD;
        }

        return move(event).action(word);
    }
}
