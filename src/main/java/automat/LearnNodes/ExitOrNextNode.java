package automat.LearnNodes;

import automat.HandlerNode;
import common.Command;
import common.Tuple;
import common.User;
import common.Word;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ExitOrNextNode extends HandlerNode {
    public ExitOrNextNode() {

    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query,
                                                  User user,
                                                  List<String> namesVocabularies,
                                                  Hashtable<String, ArrayList<Word>> dictionaries) {
        boolean condition = query.equals("да");
        List<Word> dict = dictionaries.get(user.stateLearn.getKey());

        String word = condition
                ? dict.get(user.getNextIdWord(dict.size())).en
                : user.getName();

        if (!condition)
            user.stateDialog = new Tuple<>(Command.HELP, null);

        return move(condition).action(word);
    }
}
