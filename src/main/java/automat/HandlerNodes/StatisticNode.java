package automat.HandlerNodes;

import automat.HandlerNode;
import common.Event;
import common.KeyboardBot;
import common.MessageBot;
import common.User;

import java.util.ArrayList;

public class StatisticNode extends HandlerNode {

    @Override
    public MessageBot action(String query, User user) {
        Event event = checkCommand(query, user);
        String word = user.getName();

        if (event == Event.CHANGE_TOPIC){
            return move(event).action(word,
                    new KeyboardBot(new ArrayList<>(user.getMyVocabularies().keySet())));
        }

        if (event != Event.NONE)
            return move(event).action(word);

        event = Event.WRONG;

        if (query.contains("тема")) {
            String[] arr = query.split(" ");

            if (arr.length >= 2 && user.getMyVocabularies().containsKey(arr[1])) {
                word = user.getMyVocabularies().get(arr[1])
                        .getSelectionStatistic(user);
                word = arr[1] + " - " + word;
            } else if (user.getMyVocabularies().containsKey(user.getStateLearn().getKey())) {
                word = user.getMyVocabularies().get(user.getStateLearn().getKey())
                        .getSelectionStatistic(user);
                word = user.getStateLearn().getKey() + " - " + word;
            } else
                word = "тут пока пусто";

            event = Event.STAT_STR;
        } else if (query.startsWith("слова")) {
            if (user.getMyVocabularies().containsKey(user.getStateLearn().getKey()))
                word = user.getMyVocabularies().get(user.getStateLearn().getKey())
                        .getWordsStatistic(user, 5);
            else
                word = "тут пока пусто";

            event = Event.STAT_STR;
        }

        return move(event).action(word);
    }
}