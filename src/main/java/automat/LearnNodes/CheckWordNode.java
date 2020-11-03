package automat.LearnNodes;

import automat.HandlerNode;
import common.Tuple;
import common.User;
import common.Word;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class CheckWordNode extends HandlerNode {
    public CheckWordNode() {

    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query,
                                                  User user,
                                                  List<String> namesVocabularies,
                                                  Hashtable<String, ArrayList<Word>> dictionaries) {
        List<Word> dict = dictionaries.get(user.stateLearn.getKey());
        boolean condition = checkTranslate(dict.get(user.stateLearn.getValue()), query);
        String word = condition
                ? dict.get(user.getNextIdWord(dict.size())).en
                : user.getName();

        return move(condition).action(word);
    }

    private boolean checkTranslate(Word word, String query) {
        return Arrays.asList(word.ru.split("\\|")).contains(query);
    }
}
