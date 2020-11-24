package vocabulary;

import common.Tuple;

import java.util.Arrays;
import java.util.HashMap;

public class Word {
    public String en;
    public String ru;
    public String example;
    public int frequency;
    public HashMap<Long, Tuple<Integer, Integer>> dictionary;

    public Word(int freq, String en, String ru, String example) {
        frequency = freq;
        this.en = en;
        this.ru = ru;
        this.example = example;
        this.dictionary = new HashMap<>();
    }

    public Boolean checkFromRuToEn(Long userId, String query) {
        containsUser(userId);

        if (Arrays.asList(ru.split("\\|")).contains(query)) {
            updateStatistic(userId, true);
            return true;
        }

        updateStatistic(userId, false);
        return false;
    }

    private void containsUser(Long userId) {
        if (!this.dictionary.containsKey(userId))
            this.dictionary.put(userId, new Tuple<>(0, 0));
    }

    private void updateStatistic(Long userId, Boolean bool) {
        int correct = this.dictionary.get(userId).getKey();
        int trying = this.dictionary.get(userId).getValue();

        if (bool)
            correct += 1;

        trying += 1;

        this.dictionary.replace(userId, new Tuple<>(correct, trying));
    }

    public double getCoefficient(Long userId) {
        containsUser(userId);

        Tuple<Integer, Integer> stat = this.dictionary.get(userId);

        return stat.getValue() != 0
                ? Math.min(stat.getKey() * 100.0 / stat.getValue(), 100.0)
                : 0.0;
    }

    public String createHint() {
        String translate = getRu().split(" ")[0];
        return translate.substring(0, translate.length() / 2);
    }

    public String getEn() {
        return en;
    }

    public String getRu() {
        return prepareTranslate(ru);
    }

    private String prepareTranslate(String word) {
        return word.replaceAll("\\|", " или ");
    }

    public int compareTo(Object o, long userId) {
        if (o instanceof Word){
            if (((Word) o).getCoefficient(userId) == this.getCoefficient(userId))
                return this.en.compareTo(((Word) o).getEn());
            else
                return (int) (-((Word) o).getCoefficient(userId)
                        + this.getCoefficient(userId));
        }
        else
            return -1;
    }
}