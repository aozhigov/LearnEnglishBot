package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;
import vocabulary.Selection;

import java.util.Hashtable;

public class ExitOrNextNode extends HandlerNode {
    private final Hashtable<String, Selection> vocabularies;

    public ExitOrNextNode(Hashtable<String, Selection> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public MessageBot action(String query, User user) {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        if (!query.equals("да"))
            event = Event.EXIT;
        else if (user.getStateLearn().getKey().equals("")){
            event = Event.CHANGE_TOPIC;
        }
        else{
            Selection vocabulary = vocabularies.get(user.getStateLearn().getKey());
            user.setStateLearn(vocabulary.getEnWord(user));
            word = user.getStateLearn().getValue().getEn();
            event = Event.FIRST_EN_WORD;
        }

        return move(event).action(word);
    }
}
