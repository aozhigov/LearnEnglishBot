package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.Message;
import common.User;

public class ZeroNode extends HandlerNode {
    public ZeroNode() {

    }

    @Override
    public Message action(String query, User user) {
        if (links.containsKey(user.stateDialog.getKey())) {
            Event temp = user.stateDialog.getKey();
            user.stateDialog.setKey(Event.SECOND_START);
            return move(temp).action(user.getName());
        }

        return move(Event.HELP).action(user.getName());
    }
}
