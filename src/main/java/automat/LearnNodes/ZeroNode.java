package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.Tuple;
import common.User;
import org.telegram.telegrambots.api.methods.send.SendMessage;

public class ZeroNode extends HandlerNode {
    public ZeroNode() {

    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query, User user) {
        return move(Event.START).action(user.getName());
    }
}
