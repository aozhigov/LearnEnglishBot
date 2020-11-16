package common;

import java.util.ArrayList;
import java.util.HashMap;

public class Selection {
    private final ArrayList<Word> words;
    private HashMap<Long, Tuple<Integer, Integer>> usersStat;

    public Selection(ArrayList<Word> words){
        this.words = words;
    }

    public Selection(){
        this.words = new ArrayList<>();
    }

    public void sort(int number){
        // Если 1, то сортируется по возрастанию значения incorrect в классе Word
        // Если 0, то сортируется по убыванию значения incorrect в классе Word
    }

    public Integer getWordStatistic(User user, String word){
        // При создании incorrect в Word по умолчанию ставится 1
        for (Word lookingWord : this.words) {
            if (lookingWord.en.equals(word))
                return lookingWord.getIncorrectAnswerStatistic(user.getId());
        }
        return 0;
    }

    public Word getEnWord(User user){
        Word returningWord = this.words.get(0);
        int minimalIncStat = returningWord.getIncorrectAnswerStatistic(user.getId());
        for (Word lookingWord : this.words)
        {
            if (lookingWord.getIncorrectAnswerStatistic(user.getId()) < minimalIncStat) {
                minimalIncStat = lookingWord.getIncorrectAnswerStatistic(user.getId());
                returningWord = lookingWord;
            }
        }
        return returningWord;
    }

    public Boolean checkTranslate(String query, User user){
        boolean answer = user.stateLearn.getValue().checkFromRuToEn(user.getId(), query);
        if (!this.usersStat.containsKey(user.getId()))
            this.usersStat.put(user.getId(), new Tuple<>(0, 1));
        Tuple<Integer, Integer> userStat = this.usersStat.get(user.getId());
        if (answer)
            userStat.setKey(userStat.getKey() + 1);
        else
            userStat.setValue(userStat.getValue() + 1);
        return answer;
    }
    /*
    private Word getWordClass(String enWord){
        Word word = null;
        for (Word lookingWord : this.words)
        {
            if (lookingWord.en.equals(enWord)) {
                word = lookingWord;
            }
        }
        return word;
    }*/

    public double getSelectionStatistic(User user){
        return this.usersStat.get(user.getId()).getKey() * 100.0
                / this.usersStat.get(user.getId()).getValue();
    }

    public void addWord(int freq, String en, String ru, String example){
        Word word = new Word(freq, en, ru, example);
        this.words.add(word);
    }

    public void addWord(Word word){
        this.words.add(word);
    }
}
