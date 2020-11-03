package automat.LearnNodes;

import automat.HandlerNode;
import common.Tuple;
import common.User;
import common.Word;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class YesNoNode extends HandlerNode {
    public YesNoNode() {

    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query,
                                                  User user,
                                                  List<String> namesVocabularies,
                                                  Hashtable<String, ArrayList<Word>> dictionaries) {
        boolean condition = query.equals("да");
        List<Word> dict = dictionaries.get(user.stateLearn.getKey());

        String word = condition
                ? dict.get(user.stateLearn.getValue()).en
                : prepareTranslate(dict.get(user.stateLearn.getValue()).ru);

        return move(condition).action(word);
    }

    private String prepareTranslate(String word){
        return word.replaceAll("\\|", " | ");
    }
}
