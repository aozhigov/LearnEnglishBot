package automat.LearnNodes;

import automat.HandlerNode;
import common.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ExitOrNextNode extends HandlerNode {
    private Hashtable<String, ArrayList<Word>> vocabularies;

    public ExitOrNextNode(Hashtable<String, ArrayList<Word>> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query, User user) {
        List<Word> vocabulary = vocabularies.get(user.stateLearn.getKey());
        String word = vocabulary.get(user.getNextIdWord(vocabulary.size())).en;
        Event event = Event.FIRST_EN_WORD;

        if (!query.equals("да")){
            word = user.getName();
            event = Event.END;
            user.stateDialog = new Tuple<>(Command.HELP, null);
        }

        return move(event).action(word);
    }
}
