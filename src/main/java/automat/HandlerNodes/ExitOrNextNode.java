package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import user.User;
import user.UserRepository;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class ExitOrNextNode extends HandlerNode {
    private final UserRepository db;

    public ExitOrNextNode(UserRepository db){
        this.db = db;
    }

    @Override
    public MessageBot action(String query, User user) throws IOException, ParseException {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word = user.getName();
        Event event;

        if (!query.equals("да")) {
            db.dellUser(user.getId());
            event = Event.EXIT;
        }
        else if (user.getStateLearn().getKey().equals("")) {
            event = Event.CHANGE_TOPIC;
        } else {
            word = getFirstWord(query, user);
            event = Event.FIRST_EN_WORD;
        }

        return move(event).action(word);
    }
}
