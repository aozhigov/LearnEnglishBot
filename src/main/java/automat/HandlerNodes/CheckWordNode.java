package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import user.User;
import vocabulary.Selection;

public class CheckWordNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word = user.getName();

        if (query.contains("закончить"))
            return move(Event.EXIT).action(word);

        Event event = Event.TRY;
        Selection vocabulary = user.getMyVocabularies().get(user.getStateLearn().getKey());

        if (query.contains("подсказка")) {
            word = user.getCurrentLearnWord().createHint();
            event = Event.HINT;
        } else if (vocabulary.checkTranslate(query, user)) {
            word = getFirstWord(query, user);
            event = Event.FIRST_EN_WORD;
        }

        return move(event).action(word);
    }
}
