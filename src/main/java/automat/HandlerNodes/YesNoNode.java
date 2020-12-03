package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.KeyboardBot;
import common.MessageBot;
import common.User;

import java.util.ArrayList;

public class YesNoNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        Event event = checkCommand(query, user);
        String word = user.getName();

        if (event == Event.CHANGE_TOPIC){
            int count = user.getMyVocabularies().size() / 2;
            if (user.getMyVocabularies().size() % 2 != 0)
                count++;
            return move(event).action(word,
                    new KeyboardBot(count, new ArrayList<>(user.getMyVocabularies().keySet())));
        }

        if (event != Event.NONE)
            return move(event).action(word);

        if (query.contains("подсказка")) {
            word = user.getStateLearn().getValue().createHint();
            event = Event.HINT;
        }
        else if (query.equals("да") || query.equals("еще попытка")) {
            word = user.getStateLearn().getValue().getEn();
            event = Event.SECOND_EN_WORD;
        } else {
            word = user.getStateLearn().getValue().getRu();
            event = Event.RU_WORD;
        }

        return move(event).action(word);
    }
}
