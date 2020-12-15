package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import User.User;


public class WordEditionVocabularyNode extends HandlerNode {
    public WordEditionVocabularyNode() {
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

        if (query.equals("редактировать перевод")){
            event = Event.EDIT_TRANSLATE;
            word = user.getCurrentWord();
        }

        if (query.equals("не уверен")) {
            event = Event.ADD_WORD_VOCABULARY;
            word = user.getNextWordForAdd(false);
        }

        if (word.equals("") || query.equals("закончить")) {
            word = user.getName();
            user.delRemainingWord();
            event = Event.END_VOCABULARY;
        }

        if (event == Event.WRONG)
            user.delUserVocabulary();

        return move(event).action(word);
    }

}
