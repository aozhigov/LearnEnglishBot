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
        Event event = checkCommand(query);
        if (event != Event.NONE)
            return move(event).action(user.getName());
        ArrayList<Word> vocabulary = vocabularies.get(user.stateLearn.getKey());
        String word = vocabulary.get(user.getNextIdWord(vocabulary.size())).en;
        event = Event.FIRST_EN_WORD;

        if (!query.equals("да")){
            word = user.getName();
            event = Event.EXIT;
            //user.stateDialog.setKey();
        }

        return move(event).action(word);
    }
}
