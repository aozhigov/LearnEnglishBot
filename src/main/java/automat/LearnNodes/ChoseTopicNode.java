package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.Message;
import common.User;
import vocabulary.Selection;

import java.util.Hashtable;

public class ChoseTopicNode extends HandlerNode {
    private final Hashtable<String, Selection> vocabularies;

    public ChoseTopicNode(Hashtable<String, Selection> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Message action(String query, User user) {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        if (vocabularies.containsKey(query)) {
            user.stateLearn.setKey(query);
            Selection vocabulary = vocabularies.get(user.stateLearn.getKey());

            user.stateLearn.setValue(vocabulary.getEnWord(user));
            word = user.stateLearn.getValue().getEn();
            event = Event.FIRST_EN_WORD;
        } else
            event = Event.WRONG_TOPIC;

        return move(event).action(word);
    }
}
