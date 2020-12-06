package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;

public class IsAddTrueWord extends HandlerNode {
    public IsAddTrueWord() {
    }

    @Override
    public MessageBot action(String query, User user) {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word = user.getName();
        Event event = Event.WRONG;

        if (query.equals("знаю")) {
            user.delWordFromUserVocabulary();
            event = Event.ADD_WORD_VOCABULARY;
            word = user.getNextWordForAdd(true);
        }

        if (query.equals("не уверен")) {
            event = Event.ADD_WORD_VOCABULARY;
            word = user.getNextWordForAdd(false);
        }

        if (word == null) {
            word = user.getName();
            event = Event.END_VOCABULARY;
        }

        if (event == Event.WRONG)
            user.delUserVocabulary();

        return move(event).action(word);
    }

}
