package automat;

import common.Tuple;
import common.User;
import common.Word;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public abstract class HandlerNode {
    public List<PrintNode> links;

    public HandlerNode() {
    }

    public abstract Tuple<SendMessage, HandlerNode> action(String query,
                                                           User user,
                                                           List<String> namesVocabularies,
                                                           Hashtable<String, ArrayList<Word>> dictionaries);

    public PrintNode move(boolean condition) {
        return links.size() > 1
                ? condition
                    ? links.get(0)
                    : links.get(1)
                : null;
    }

    public void initLinks(List<PrintNode> links) {
        this.links = links;
    }
}
