package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;


public class WrongNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        if (query.equals("да")) {
            if (user.getStateLearn().getValue() != null){
                word = user.getStateLearn().getValue().getEn();
                event = Event.FIRST_EN_WORD;
            }

            if (user.getStateLearn().getKey().equals(""))
               event = Event.CHANGE_TOPIC;

            return move(event).action(word);
        }

        return move(Event.EXIT).action(word);
    }
}
