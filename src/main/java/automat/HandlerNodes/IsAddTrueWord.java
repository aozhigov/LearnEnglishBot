package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;

import java.util.ArrayList;

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

        if (query.equals("закончить")
                || (word.equals("") && user.getSizeAddVocabulary() == 0)) {
            user.delUserVocabulary();
            event = Event.HELP;

            if (user.getStateLearn().getValue() != null) {
                word = user.getStateLearn().getValue().getEn();
                event = Event.FIRST_EN_WORD;
            }

            if (user.getStateLearn().getKey().equals("")) {
                event = Event.CHANGE_TOPIC;
                return move(event).action(user.getName(),
                        new ArrayList<>(user.getUserVocabularies().keySet()));
            }
        }

        if (word.equals("") || query.equals("сформировать словарь")) {
            word = user.getName();
            event = Event.END_VOCABULARY;
        }

        if (event == Event.WRONG)
            user.delUserVocabulary();

        return move(event).action(word);
    }

}
