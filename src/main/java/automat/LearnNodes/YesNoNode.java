package automat.LearnNodes;

import automat.HandlerNode;
import common.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class YesNoNode extends HandlerNode {
    private final Hashtable<String, Selection> vocabularies;

    public YesNoNode(Hashtable<String, Selection> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Tuple<SendMessage, HandlerNode> action(String query,  User user) {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        if (query.equals("да")){
            word = user.stateLearn.getValue().getEn();//vocablurary.get(user.stateLearn.getValue()).en;
            event = Event.SECOND_EN_WORD;
        }
        else{
            word = user.stateLearn.getValue().getRu();//prepareTranslate(vocabulary..get(user.stateLearn.getValue()).ru);
            event = Event.RU_WORD;
        }

        return move(event).action(word);
    }
}
