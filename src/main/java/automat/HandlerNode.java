package automat;

import common.Event;
import common.Tuple;
import common.User;
import common.Word;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public abstract class HandlerNode {
    public Hashtable<Event, PrintNode> links;

    public HandlerNode() {
    }

    public abstract Tuple<SendMessage, HandlerNode> action(String query, User user);

//    public PrintNode move(boolean condition) {
//        return links.size() > 1
//                ? condition
//                    ? links.get(0)
//                    : links.get(1)
//                : null;
//    }

    public PrintNode move(Event event) {
        return links.get(event);
    }

    public void initLinks(Hashtable<Event, PrintNode> links) {
        this.links = links;
    }
}
