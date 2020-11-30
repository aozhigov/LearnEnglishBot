package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;

public class ZeroNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        if (links.containsKey(user.getStateDialog().getKey())) {
            Event temp = user.getStateDialog().getKey();
            user.setStateDialog(Event.SECOND_START);

            return move(temp).action(user.getName());
        }

        return move(Event.HELP).action(user.getName());
    }
}
