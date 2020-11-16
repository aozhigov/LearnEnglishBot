package automat.LearnNodes;

import automat.HandlerNode;
import common.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;

public class HelpNode extends HandlerNode {
    private Hashtable<String, Selection> vocabularies;

    public HelpNode(Hashtable<String, Selection> vocabularies) {
        this.vocabularies = vocabularies;
    }
    @Override
    public Tuple<SendMessage, HandlerNode> action(String query, User user) {
        Event temp = checkCommand(query);
        if (temp != Event.NONE)
            return move(temp).action(user.getName());
        if (user.stateLearn.getValue() != null){
            Selection vocabulary = vocabularies.get(user.stateLearn.getKey());
            String word = user.stateLearn.getValue().getEn();//vocabulary.get(user.getNextIdWord(vocabulary.size())).en;
            return move(Event.SECOND_EN_WORD).action(word);
        }

        return move(Event.SECOND_START).action(user.getName());
    }
}
