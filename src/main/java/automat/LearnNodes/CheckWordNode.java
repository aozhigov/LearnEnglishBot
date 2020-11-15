package automat.LearnNodes;

import automat.HandlerNode;
import common.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class CheckWordNode extends HandlerNode {
    private Hashtable<String, ArrayList<Word>> vocabularies;

    public CheckWordNode(Hashtable<String, ArrayList<Word>> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query, User user) {
        Event event = checkCommand(query);
        if (event != Event.NONE)
            return move(event).action(user.getName());

        ArrayList<Word> vocabulary = vocabularies.get(user.stateLearn.getKey());
        String word = "";

        if (query.contains("/hint")){
            event = Event.HINT;
            word = "-hint-";
            return move(event).action(word);
        }


        if (checkTranslate(vocabulary.get(user.stateLearn.getValue()), query)){
            word = vocabulary.get(user.getNextIdWord(vocabulary.size())).en;
            event = Event.FIRST_EN_WORD;
            return move(event).action(word);
        }

        word = user.getName();
        event = Event.TRY;
        return move(event).action(word);
    }

    private boolean checkTranslate(Word word, String query) {
        return Arrays.asList(word.ru.split("\\|")).contains(query);
    }
}
