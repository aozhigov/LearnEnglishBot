package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.Tuple;
import common.User;
import common.Word;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;

public class StatisticNode extends HandlerNode {
    private Hashtable<String, ArrayList<Word>> vocabularies;

    public StatisticNode(Hashtable<String, ArrayList<Word>> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query, User user) {
        return null;
    }
}
