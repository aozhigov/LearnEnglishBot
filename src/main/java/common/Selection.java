package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Selection {
    private final ArrayList<Word> words;
//    public final String name;
    private HashMap<User, Tuple<Integer, Integer>> usersStat;

    public Selection(/*String name, */ArrayList<Word> words){
//        this.name = name;
        this.words = words;

    }

    public void sort(int number){
        // Если 1, то сортируется по возрастанию значения incorrect в классе Word
        // Если 0, то сортируется по убыванию значения incorrect в классе Word
    }

    public Integer getWordStatistic(User user, String word){
        // При создании incorrect в Word по умолчанию ставится 1
        for (Word lookingWord : this.words) {
            if (lookingWord.en.equals(word))
                return lookingWord.getIncorrectAnswerStatistic(user);
        }
        return 0;
    }

    public String getEnWord(User user){
        Word returningWord = this.words.get(0);
        int minimalIncStat = returningWord.getIncorrectAnswerStatistic(user);
        for (Word lookingWord : this.words)
        {
            if (lookingWord.getIncorrectAnswerStatistic(user) < minimalIncStat) {
                minimalIncStat = lookingWord.getIncorrectAnswerStatistic(user);
                returningWord = lookingWord;
            }
        }
        return returningWord.en;
    }

    public Boolean checkTranslate(String enWord, String ruWord, User user){
        assert getWordClass(enWord) != null;
        boolean answer = getWordClass(enWord).checkFromRuToEn(user, ruWord);
        if (!this.usersStat.containsKey(user))
            this.usersStat.put(user, new Tuple<>(0, 1));
        Tuple<Integer, Integer> userStat = this.usersStat.get(user);
        if (answer)
            userStat.setKey(userStat.getKey() + 1);
        else
            userStat.setValue(userStat.getValue() + 1);
        return answer;
    }

    private Word getWordClass(String enWord){
        Word word = null;
        for (Word lookingWord : this.words)
        {
            if (lookingWord.en.equals(enWord)) {
                word = lookingWord;
            }
        }
        return word;
    }

    public double getSelectionStatistic(User user){
        return this.usersStat.get(user).getKey() * 100.0 / this.usersStat.get(user).getValue();
    }
}

