package vocabulary;

import common.Tuple;
import common.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Selection {
    private final ArrayList<Word> words;
    private final HashMap<Long, Tuple<Integer, Integer>> usersStat;

    public Selection() {
        words = new ArrayList<>();
        usersStat = new HashMap<>();
    }

    public Selection(ArrayList<Word> words) {
        this.words = words;
        this.usersStat = new HashMap<>();
    }

    public Integer getWordStatistic(User user, String word) {
        for (Word lookingWord : this.words) {
            if (lookingWord.en.equals(word))
                return lookingWord.getIncorrectAnswerStatistic(user.getId());
        }
        return 0;
    }

    public Word getEnWord(User user) {
        ArrayList<Word> rightWords = new ArrayList<>();
        rightWords.add(this.words.get(0));
        int minimalIncStat = this.words.get(0).getIncorrectAnswerStatistic(user.getId());

        for (Word lookingWord : this.words) {
            if (lookingWord.getIncorrectAnswerStatistic(user.getId()) == minimalIncStat) {
                rightWords.add(lookingWord);
            }
            else if (lookingWord.getIncorrectAnswerStatistic(user.getId()) < minimalIncStat){
                rightWords.clear();
                rightWords.add(lookingWord);
                minimalIncStat = lookingWord.getIncorrectAnswerStatistic(user.getId());
            }
        }
        return getRandomWord(rightWords);
    }

    private Word getRandomWord(ArrayList<Word> rightWords){
        Random random = new Random();
        int position = random.nextInt(rightWords.size() - 1);
        return rightWords.get(position);
    }

    public Boolean checkTranslate(String query, User user) {
        boolean answer = user.getStateLearn().getValue().checkFromRuToEn(user.getId(), query);
        if (!this.usersStat.containsKey(user.getId()))
            this.usersStat.put(user.getId(), new Tuple<>(0, 1));
        Tuple<Integer, Integer> userStat = this.usersStat.get(user.getId());
        if (answer)
            userStat.setKey(userStat.getKey() + 1);
        else
            userStat.setValue(userStat.getValue() + 1);
        return answer;
    }

    public double getSelectionStatistic(User user) {
        return this.usersStat.get(user.getId()).getKey() * 100.0
                / this.usersStat.get(user.getId()).getValue();
    }
}
