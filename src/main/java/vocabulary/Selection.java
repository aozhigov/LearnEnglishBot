package vocabulary;

import User.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Selection {
    private final Random rnd;
    private ArrayList<Word> words;

    public Selection(ArrayList<Word> words) {
        this.words = words;
        rnd = new Random();
    }

    public int getEnWord() {
        ArrayList<Integer> rightWords = new ArrayList<>();
        double minimalIncStat = searchMin();

        for (int i = 0; i < this.words.size(); i++) {
            Word lookingWord = this.words.get(i);
            if (Math.abs(lookingWord.getCoefficient()
                    - minimalIncStat) < 10.0)
                rightWords.add(i);
        }
        return getRandomWord(rightWords);
    }

    private double searchMin() {
        double min = Double.MAX_VALUE;

        for (Word lookingWord : this.words)
            if (lookingWord.getCoefficient() < min)
                min = lookingWord.getCoefficient();
        return min;
    }

    public Word getWord(int idx) {
        return words.get(idx);
    }

    private int getRandomWord(ArrayList<Integer> rightWords) {
        return rnd.nextInt(rightWords.size());
    }

    public Boolean checkTranslate(String query, User user) {
        return user.getCurrentLearnWord()
                .checkFromRuToEn(query);
    }

    public String getSelectionStatistic() {
        int badLearnedWords = 0;
        int goodLearnedWords = 0;
        int excellentLearnedWord = 0;
        double selectionStatistic = 0.0;

        for (Word word : this.words) {
            selectionStatistic += word.getCoefficient();
            double wordCoefficient = word.getCoefficient();
            if (wordCoefficient <= 40.0)
                badLearnedWords += 1;
            else if (wordCoefficient <= 80.0)
                goodLearnedWords += 1;
            else
                excellentLearnedWord += 1;
        }

        return String.format("%.2f", selectionStatistic /
                (badLearnedWords + goodLearnedWords + excellentLearnedWord))
                + "%:\n\tПлохо изучил: " + badLearnedWords
                + " слов\n\tХорошо изучил: " + goodLearnedWords
                + " слов\n\tОтлично изучил: " + excellentLearnedWord
                + " слов";
    }

    public String getWordsStatistic(int countWords) {
        StringBuilder response = new StringBuilder("Статистика слов:\n");
        quickSort(words, 0, words.size() - 1);
        response.append("Топ худших:\n");
        response.append(generateStringStat(0, countWords,
                1, false));
        response.append("\nТоп лучших:\n");
        response.append(generateStringStat(words.size() - 1, words.size() - 1 - countWords,
                -1, true));
        return response.toString();
    }

    private String generateStringStat(int start, int finish, int step, boolean isMore) {
        StringBuilder response = new StringBuilder();
        for (int i = start; i >= 0 && i < words.size()
                && ((i < finish && !isMore) || (isMore && i > finish)); i += step) {
            double coefficient = words.get(i).getCoefficient();

            if (isMore && coefficient == 0)
                continue;

            response.append("\t").
                    append(words.get(i).getEn())
                    .append("\t-\t")
                    .append(String.format("%.2f", coefficient))
                    .append("%\n");
        }

        return response.toString();
    }

    public void quickSort(ArrayList<Word> array, int low, int high) {
        if (array.size() == 0)
            return;

        if (low >= high)
            return;

        int middle = low + (high - low) / 2;
        Word item = array.get(middle);

        int i = low, j = high;
        while (i <= j) {
            while (array.get(i).compareTo(item) < 0)
                i++;

            while (array.get(j).compareTo(item) > 0)
                j--;

            if (i <= j) {
                Word temp = array.get(i);
                array.set(i++, array.get(j));
                array.set(j--, temp);
            }
        }

        if (low < j)
            quickSort(array, low, j);

        if (high > i)
            quickSort(array, i, high);
    }

    public void delWord(Integer idx) {
        ArrayList<Word> temp = new ArrayList<>();
        for (int i = 0; i < words.size(); i++)
            if (i != idx + 1)
                temp.add(words.get(i));

        words = temp;
    }

    public void setTranslateWord(Integer idx, String translate) {
        words.get(idx).setRu(translate);
    }

    public String getWithoutStat(int idx) {
        return "\n" + words.get(idx).en + "\nПеревод: " + words.get(idx).ru;
    }

    public int getSize() {
        return words.size();
    }

    public void delAllStartIdx(int idx) {
        ArrayList<Word> temp = new ArrayList<>();
        if (idx <= words.size() && idx >= 0)
            for (int i = 0; i < idx; i++)
                temp.add(words.get(i));
        words = temp;
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (Word word : words)
            jsonArray.add(word.getJson());

        jsonObject.put("words", jsonArray);
        return jsonObject;
    }
}