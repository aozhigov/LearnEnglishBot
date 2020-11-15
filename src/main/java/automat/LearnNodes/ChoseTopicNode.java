package automat.LearnNodes;

import automat.HandlerNode;
import common.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ChoseTopicNode extends HandlerNode {
    private Hashtable<String, ArrayList<Word>> vocabularies;

    public ChoseTopicNode(Hashtable<String, ArrayList<Word>> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query, User user) {
        String word = user.getName();
        Event event = checkCommand(query);

        if (event != Event.NONE)
            return move(event).action(user.getName());

        if (vocabularies.containsKey(query)){
            user.stateLearn.setKey(query);
            ArrayList<Word> vocabulary = vocabularies.get(user.stateLearn.getKey());

            word = vocabulary.get(user.getNextIdWord(vocabulary.size())).en;
            event = Event.FIRST_EN_WORD;
        }
        else
            event = Event.WRONG_TOPIC;

        return move(event).action(word);
    }
}
