package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;
import vocabulary.Word;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class isAddTrueWord extends HandlerNode {
    public isAddTrueWord(){ }

    @Override
    public MessageBot action(String query, User user) throws IOException {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        if (query.equals("закончить"))
            event = Event.ADD_VOCABULARY;

        if (query.equals("знаю")) {
            event = Event.ADD_WORD_VOCABULARY;
            word = "";
        }

        return move(event).action(word);
    }

}
