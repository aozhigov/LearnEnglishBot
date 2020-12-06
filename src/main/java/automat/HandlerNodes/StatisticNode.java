package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.MessageBot;
import common.User;

public class StatisticNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        String word = user.getName();

        Event event = Event.WRONG;

        if (query.contains("тема")) {
            String[] arr = query.split(" ");

            if (arr.length >= 2 && user.getUserVocabularies().containsKey(arr[1])) {
                word = user.getUserVocabularies().get(arr[1])
                        .getSelectionStatistic(user);
                word = arr[1] + " - " + word;
            } else if (user.getUserVocabularies().containsKey(user.getStateLearn().getKey())) {
                word = user.getUserVocabularies().get(user.getStateLearn().getKey())
                        .getSelectionStatistic(user);
                word = user.getStateLearn().getKey() + " - " + word;
            } else
                word = "тут пока пусто";

            event = Event.STAT_STR;
        } else if (query.startsWith("слова")) {
            if (user.getUserVocabularies().containsKey(user.getStateLearn().getKey()))
                word = user.getUserVocabularies().get(user.getStateLearn().getKey())
                        .getWordsStatistic(user, 5);
            else
                word = "тут пока пусто";

            event = Event.STAT_STR;
        }

        return move(event).action(word);
    }
}