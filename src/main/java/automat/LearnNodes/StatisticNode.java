package automat.LearnNodes;

import automat.HandlerNode;
import common.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;

public class StatisticNode extends HandlerNode {
    private Hashtable<String, Selection> vocabularies;

    public StatisticNode(Hashtable<String, Selection> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query, User user) {
        return null;
    }
}
