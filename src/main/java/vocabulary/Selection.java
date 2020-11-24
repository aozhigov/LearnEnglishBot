package vocabulary;

import common.Tuple;
import common.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Selection {
    private final ArrayList<Word> words;
    private final HashMap<Long, Tuple<Integer, Integer>> usersStat;
    private final Random rnd;

    public Selection() {
        words = new ArrayList<>();
        usersStat = new HashMap<>();
        rnd = new Random();
    }

    public Selection(ArrayList<Word> words) {
        this.words = words;
        this.usersStat = new HashMap<>();
        rnd = new Random();
    }

    public Word getEnWord(User user) {
        ArrayList<Word> rightWords = new ArrayList<>();
        double minimalIncStat = searchMin(user.getId());

        for (Word lookingWord : this.words) {
            if (Math.abs(lookingWord.getCoefficient(user.getId())
                    - minimalIncStat) < 10.0)
                rightWords.add(lookingWord);
        }
        return getRandomWord(rightWords);
    }

    private double searchMin(long userId) {
        double min = Double.MAX_VALUE;

        for (Word lookingWord : this.words)
            if (lookingWord.getCoefficient(userId) < min)
                min = lookingWord.getCoefficient(userId);
        return min;
    }

    private Word getRandomWord(ArrayList<Word> rightWords) {
        int position = rnd.nextInt(rightWords.size());
        return rightWords.get(position);
    }

    public Boolean checkTranslate(String query, User user) {
        boolean answer = user.getStateLearn().getValue()
                .checkFromRuToEn(user.getId(), query);

        if (!this.usersStat.containsKey(user.getId()))
            this.usersStat.put(user.getId(), new Tuple<>(0, 1));

        Tuple<Integer, Integer> userStat = this.usersStat.get(user.getId());

        if (answer)
            userStat.setKey(userStat.getKey() + 1);
        else
            userStat.setValue(userStat.getValue() + 1);

        return answer;
    }

    public String getSelectionStatistic(User user) {
        if (!this.usersStat.containsKey(user.getId()))
            this.usersStat.put(user.getId(), new Tuple<>(0, 1));

        int badLearnedWords = 0;
        int goodLearnedWords = 0;
        int excellentLearnedWord = 0;
        double selectionStatistic = 0.0;

        for (Word word : this.words) {
            selectionStatistic += word.getCoefficient(user.getId());
            double wordCoefficient = word.getCoefficient(user.getId());
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

    public String getWordsStatistic(User user, int countWords) {
        StringBuilder response = new StringBuilder("Статистика слов:\n");
        quickSort(words, 0, words.size() - 1, user.getId());
        for (Word lookingWord : this.words) {
            if (countWords-- <= 0)
                break;
            String wordStat = "\t" + lookingWord.getEn() + "\t-\t"
                    + String.format("%.2f", lookingWord.getCoefficient(user.getId()))
                    + "%\n";
            response.append(wordStat);
        }
        return response.toString();
    }

    public void quickSort(ArrayList<Word> array, int low, int high, long userId) {
        if (array.size() == 0)
            return;

        if (low >= high)
            return;

        int middle = low + (high - low) / 2;
        Word item = array.get(middle);

        int i = low, j = high;
        while (i <= j) {
            while (array.get(i).compareTo(item, userId) < 0)
                i++;

            while (array.get(j).compareTo(item, userId) > 0)
                j--;

            if (i <= j) {
                Word temp = array.get(i);
                array.set(i++, array.get(j));
                array.set(j--, temp);
            }
        }

        if (low < j)
            quickSort(array, low, j, userId);

        if (high > i)
            quickSort(array, i, high, userId);
    }
}