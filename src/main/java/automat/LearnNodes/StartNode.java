package automat.LearnNodes;

import automat.HandlerNode;
import common.Tuple;
import common.User;
import common.Word;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class StartNode extends HandlerNode {
    public StartNode() {

    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query,
                                                  User user,
                                                  List<String> namesVocabularies,
                                                  Hashtable<String, ArrayList<Word>> dictionaries) {
        boolean condition = namesVocabularies.contains(query);

        if (condition)
            user.stateLearn.setKey(query);

        List<Word> dict = dictionaries.get(user.stateLearn.getKey());
        String word = condition
                ? dict.get(user.getNextIdWord(dict.size())).en
                : user.getName();

        return move(condition).action(word);
    }
}
