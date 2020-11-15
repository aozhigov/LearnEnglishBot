package common;

import java.util.HashMap;

public class Word {
    public String en;
    public String ru;
    public String example;
    public int frequency;
    public HashMap<User, Tuple<Integer, Integer>> dictionary;

    public Word(int freq, String en, String ru, String example){
        frequency = freq;
        this.en = en;
        this.ru = ru;
        this.example = example;
        this.dictionary = new HashMap<>();
    }

    public Boolean checkFromRuToEn(User user, String query){
        if (!this.dictionary.containsKey(user))
            this.dictionary.put(user, new Tuple<>(0, 1));
        if(query.equals(this.ru)){
            calculateCoefficient(user, true);
            return true;
        }
        calculateCoefficient(user,false);
        return false;
    }

    private void calculateCoefficient(User user, Boolean bool){
        int correct = this.dictionary.get(user).getKey();
        int incorrect = this.dictionary.get(user).getValue();
        if (bool) correct += 1;
        else incorrect += 1;
        this.dictionary.remove(user);
        this.dictionary.put(user, new Tuple<>(correct, incorrect));
    }

    public double getCoefficient(User user){
        Tuple<Integer, Integer> stat = this.dictionary.get(user);
        return stat.getKey() * 1.0 / stat.getValue() * 100.0;
    }

    public int getIncorrectAnswerStatistic(User user){
        return this.dictionary.get(user).getValue();
    }
}