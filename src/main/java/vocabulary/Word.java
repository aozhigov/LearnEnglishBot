package vocabulary;

import common.Tuple;

import java.util.Arrays;

public class Word {
    public String en;
    public String ru;
    public Tuple<Integer, Integer> statistic;

    public Word(String en, String ru) {
        this.en = en;
        this.ru = ru;
        this.statistic = new Tuple<>(0, 0);
    }

    public Word(String en, String ru, Tuple<Integer, Integer> statistic) {
        this.en = en;
        this.ru = ru;
        this.statistic = statistic;
    }

    public Boolean checkFromRuToEn(String query) {
        if (Arrays.asList(ru.split("\\|")).contains(query)) {
            updateStatistic(true);
            return true;
        }

        updateStatistic(false);
        return false;
    }

    private void updateStatistic(Boolean bool) {
        int correct = statistic.getKey();
        int trying = statistic.getValue();

        if (bool)
            correct += 1;

        trying += 1;

        statistic.setTuple(correct, trying);
    }

    public double getCoefficient() {
        return statistic.getValue() != 0
                ? Math.min(statistic.getKey() * 100.0 / statistic.getValue(), 100.0)
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

    public void setRu(String translate){
        this.ru = translate;
    }

    private String prepareTranslate(String word) {
        return word.replaceAll("\\|", " или ");
    }

    public int compareTo(Object o) {
        if (o instanceof Word){
            if (((Word) o).getCoefficient() == this.getCoefficient())
                return this.en.compareTo(((Word) o).getEn());
            else
                return (int) (-((Word) o).getCoefficient()
                        + this.getCoefficient());
        }
        else
            return -1;
    }
}