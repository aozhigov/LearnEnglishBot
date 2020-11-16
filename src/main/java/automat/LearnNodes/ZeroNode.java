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
        if (links.containsKey(user.stateDialog.getKey())){
            Event temp = user.stateDialog.getKey();
            user.stateDialog.setKey(Event.SECOND_START);
            return move(temp).action(user.getName());
        }

        return move(Event.HELP).action(user.getName());
    }
}
