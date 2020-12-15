package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import User.User;


public class TranslateEditorNode extends HandlerNode {
    @Override
    public MessageBot action(String query, User user) {
        user.setTranslateWord(query);
        String word = user.getNextWordForAdd(true);
        Event event = Event.ADD_WORD_VOCABULARY;

        if (word.equals("") || query.equals("закончить")) {
            word = user.getName();
            event = Event.END_VOCABULARY;
        }

        return move(event).action(word);
    }
}
