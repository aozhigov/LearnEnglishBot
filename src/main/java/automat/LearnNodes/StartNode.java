package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.Tuple;
import common.User;
import common.Word;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class StartNode extends HandlerNode {
    private Hashtable<String, ArrayList<Word>> vocabularies;

    public StartNode(Hashtable<String, ArrayList<Word>> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query, User user) {
        Event event = Event.END; // or help
        String word = user.getName();

        if (query.contains("выход"))
            return move(event).action(word);

        if (vocabularies.containsKey(query)){
            user.stateLearn.setKey(query);
            List<Word> vocabulary = vocabularies.get(user.stateLearn.getKey());

            word = vocabulary.get(user.getNextIdWord(vocabulary.size())).en;
            event = Event.FIRST_EN_WORD;
        }
        else
            event = Event.WRONG_TOPIC;

        return move(event).action(word);
    }
}
