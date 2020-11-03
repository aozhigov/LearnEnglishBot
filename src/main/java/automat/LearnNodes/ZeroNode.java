package automat.LearnNodes;

import automat.HandlerNode;
import common.Tuple;
import common.User;
import common.Word;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ZeroNode extends HandlerNode {
    public ZeroNode() {

    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query,
                                                  User user,
                                                  List<String> namesVocabularies,
                                                  Hashtable<String, ArrayList<Word>> dictionaries) {
        return move(true).action(user.getName());
    }
}
