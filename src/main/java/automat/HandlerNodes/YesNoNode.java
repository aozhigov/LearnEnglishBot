package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import User.User;

public class YesNoNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word;
        Event event;

        if (query.contains("подсказка")) {
            word = user.getCurrentLearnWord().createHint();
            event = Event.HINT;
        } else if (query.equals("да") || query.equals("еще попытка")) {
            word = user.getCurrentLearnWord().getEn();
            event = Event.SECOND_EN_WORD;
        } else {
            word = user.getCurrentLearnWord().getRu();
            event = Event.RU_WORD;
        }

        return move(event).action(word);
    }
}
