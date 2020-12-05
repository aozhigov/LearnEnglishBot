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
        Event event = checkCommand(query, user);
        String word = user.getName();

        if (event != Event.NONE) {
            user.delLastAddMyVocabularies();
            return move(event).action(word);
        }

        event = Event.WRONG;

        if (query.equals("знаю")) {
            user.delWord();
            event = Event.ADD_WORD_VOCABULARY;
            word = user.getNextWord(true);
        }

        if (query.equals("не уверен")) {
            event = Event.ADD_WORD_VOCABULARY;
            word = user.getNextWord(false);
        }

        if (word == null) {
            word = user.getName();
            event = Event.END_VOCABULARY;
        }

        if (event == Event.WRONG)
            user.delLastAddMyVocabularies();

        return move(event).action(word);
    }

}
