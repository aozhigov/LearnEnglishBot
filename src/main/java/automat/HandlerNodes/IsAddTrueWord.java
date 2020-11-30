package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;
import vocabulary.Word;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class IsAddTrueWord extends HandlerNode {
    public IsAddTrueWord(){ }

    @Override
    public MessageBot action(String query, User user) throws IOException {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        if (query.equals("знаю")) {
            event = Event.ADD_WORD_VOCABULARY;
            word = user.getNextWord();
            //TODO оработать ограниченное количество слов,
            // добавить оставшиесяя, не добавлять оставшиеся,
        }

        if (query.equals("не знаю"))
            user.delWord();

        return move(event).action(word);
    }

}
