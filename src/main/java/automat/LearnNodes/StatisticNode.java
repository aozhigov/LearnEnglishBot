package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.Message;
import common.User;
import vocabulary.Selection;

import java.util.Hashtable;

public class StatisticNode extends HandlerNode {
    private final Hashtable<String, Selection> vocabularies;

    public StatisticNode(Hashtable<String, Selection> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public Message action(String query, User user) {
        Event temp = checkCommand(query);
        String word = user.getName();

        if (temp != Event.NONE)
            return move(temp).action(word);

        Selection vocabulary = vocabularies.get(user.stateLearn.getKey());

        if (query.startsWith("по теме")) {
            String[] arr = query.split(" ");

            if (arr.length > 2 && vocabularies.containsKey(arr[2])) {
                word = Double.toString(vocabularies.get(arr[2])
                        .getSelectionStatistic(user));
                word = arr[2] + " - " + word + "%";
            } else {
                word = Double.toString(vocabularies.get(user.stateLearn.getKey())
                        .getSelectionStatistic(user));
                word = user.stateLearn.getKey() + " - " + word + "%";
            }

            return move(Event.STAT_STR).action(word);
        }


        if (query.startsWith("по слову")) {
            String[] arr = query.split(" ");

            if (arr.length > 2) {
                word = Double.toString(vocabulary
                        .getWordStatistic(user, arr[2]) * 100);
                word = arr[2] + " - " + word + "%";
            } else {
                word = Double.toString(vocabulary
                        .getWordStatistic(user, user.stateLearn.getValue().en) * 100);
                word = user.stateLearn.getValue().en + " - " + word + "%";
            }

            return move(Event.STAT_STR).action(word);
        }

        return move(Event.WRONG).action(word);
    }
}
