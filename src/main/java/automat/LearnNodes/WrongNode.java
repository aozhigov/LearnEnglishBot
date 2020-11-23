package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;


public class WrongNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        Event temp = checkCommand(query);
        String word = user.getName();

        if (temp != Event.NONE)
            return move(temp).action(word);

        if (query.equals("да")) {
            if (user.getStateLearn().getValue() != null){
                word = user.getStateLearn().getValue().getEn();
                return move(Event.FIRST_EN_WORD).action(word);
            }

            if (user.getStateLearn().getKey().equals(""))
                return move(Event.CHANGE_TOPIC).action(word);
        }

        return move(Event.EXIT).action(word);
    }
}
