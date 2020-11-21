package automat.LearnNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;
import vocabulary.Selection;

import java.util.Hashtable;

public class StatisticNode extends HandlerNode {
    private final Hashtable<String, Selection> vocabularies;

    public StatisticNode(Hashtable<String, Selection> vocabularies) {
        this.vocabularies = vocabularies;
    }

    @Override
    public MessageBot action(String query, User user) {
        Event temp = checkCommand(query);
        String word = user.getName();

        if (temp != Event.NONE)
            return move(temp).action(word);

        if (query.startsWith("по теме")) {
            String[] arr = query.split(" ");

            if (arr.length > 2 && vocabularies.containsKey(arr[2])) {
                word = vocabularies.get(arr[2])
                        .getSelectionStatistic(user);
                word = arr[2] + " - " + word + "%";
            } else {
                word = vocabularies.get(user.getStateLearn().getKey())
                        .getSelectionStatistic(user);
                word = user.getStateLearn().getKey() + " - " + word;
            }

            return move(Event.STAT_STR).action(word);
        }

        if (query.startsWith("по словам в")) {
            String[] arr = query.split(" ");

            if (arr.length > 3 && vocabularies.containsKey(arr[3])) {
                word = vocabularies.get(arr[3]).getWordsStatistic(user).toString();
            } else {
                word = vocabularies.get(user.getStateLearn().getKey()).getWordsStatistic(user).toString();
            }

            return move(Event.STAT_STR).action(word);
        }

        return move(Event.WRONG).action(word);
    }
}