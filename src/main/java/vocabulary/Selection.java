package vocabulary;

import common.Tuple;
import common.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Selection {
    private final ArrayList<Word> words;
    private final HashMap<Long, Tuple<Integer, Integer>> usersStat;
    private final HashMap<Long, ArrayList<Tuple<String, Double>>> badWords = new HashMap<>();
    private ArrayList<Word> temporaryArray;

    public Selection() {
        words = new ArrayList<>();
        usersStat = new HashMap<>();
    }

    public Selection(ArrayList<Word> words) {
        this.words = words;
        this.usersStat = new HashMap<>();
        this.MergeSort(this.words);
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
        this.currentUserBadWordStatistic(user, user.getStateLearn().getValue());
        return answer;
    }

    public String getSelectionStatistic(User user) {
        if (!this.usersStat.containsKey(user.getId()))
            this.usersStat.put(user.getId(), new Tuple<>(0, 1));
        int badLearnedWords = 0;
        int goodLearnedWords = 0;
        int excellentLearnedWord = 0;
        double selectionStatistic = 0.0;
        for (Word lookingWord: this.words) {
            selectionStatistic += lookingWord.getCoefficient(user.getId());
            double wordCoefficient = lookingWord.getCoefficient(user.getId());
            if (wordCoefficient <= 40.0) badLearnedWords += 1;
            else if (wordCoefficient <= 80.0) goodLearnedWords += 1;
            else excellentLearnedWord += 1;
        }
        String response = "";
        response += String.format("%.2f", selectionStatistic /
                (badLearnedWords + goodLearnedWords + excellentLearnedWord)) + "%";
        response += ":\n";
        response += "\tПлохо изучил: " + badLearnedWords
                + " слов\n\tХорошо изучил: " + goodLearnedWords
                + " слов\n\tОтлично изучил: " + excellentLearnedWord + " слов";
        return response + this.getBadLearnWordsStatistic(user);
    }

    public StringBuilder getWordsStatistic(User user){
        StringBuilder response = new StringBuilder("Статистика слов:\n");
        for (Word lookingWord: this.words){
            String wordStat = "\t" + lookingWord.getEn() + "\t-\t"
                    + String.format("%.2f", lookingWord.getCoefficient(user.getId())) + "\n";
            response.append(wordStat);
        }
        return response;
    }

    public void currentUserBadWordStatistic(User user, Word word){
        if (!this.badWords.containsKey(user.getId())
                ||this.badWords.get(user.getId()).size() == 0
                || !this.badWords.containsKey(user.getId())){
            ArrayList<Tuple<String, Double>> arr = new ArrayList<>();
            for(int i = 0; i < 5; i++){
                String nullWord = this.words.get(i).getEn();
                double coefficient = this.words.get(i).getCoefficient(user.getId());
                arr.add(new Tuple<>(nullWord, coefficient));
            }
            this.badWords.put(user.getId(), arr);
        }
        String nullEnWord = word.getEn();
        String temporaryNullEnWord;
        Double nullCoefficient = word.getCoefficient(user.getId());
        Double temporaryNullCoefficient;
        for (int i = 0; i < 5; i++){
            Tuple<String, Double> current = this.badWords.get(user.getId()).get(i);
            if (!current.getKey().equals(nullEnWord)
                    && current.getValue() > nullCoefficient){
                temporaryNullEnWord = current.getKey();
                temporaryNullCoefficient = current.getValue();
                this.badWords.get(user.getId()).set(i, new Tuple<>(nullEnWord, nullCoefficient));
                nullEnWord = temporaryNullEnWord;
                nullCoefficient = temporaryNullCoefficient;
            }
        }
    }

    private String getBadLearnWordsStatistic(User user){
        if (this.badWords.get(user.getId()).size() == 0
                || !this.badWords.containsKey(user.getId())){
            ArrayList<Tuple<String, Double>> arr = new ArrayList<>();
            for(int i = 0; i < 5; i++){
                String nullWord = this.words.get(i).getEn();
                double coefficient = this.words.get(i).getCoefficient(user.getId());
                arr.add(new Tuple<>(nullWord, coefficient));
            }
            this.badWords.put(user.getId(), arr);
        }
        StringBuilder response = new StringBuilder();
        response.append("\n\nХуже всего выучил(-ла):\n");
        for (Tuple<String, Double> current:
                this.badWords.get(user.getId())) {
            String partOfResponse = "\t" + current.getKey()  + " - "+ current.getValue() + "%\n";
            response.append(partOfResponse);
        }
        return response.toString();
    }

    private void Merge(ArrayList<Word> array, int start, int middle, int end)
    {
        int leftPtr = start;
        int rightPtr = middle + 1;
        int length = end - start + 1;
        for (int i = 0; i < length; i++)
        {
            if (rightPtr > end || (leftPtr <= middle
                    && array.get(leftPtr).getEn().compareTo(array.get(rightPtr).getEn()) < 0))
            {
                this.temporaryArray.add(i, array.get(leftPtr));
                leftPtr++;
            }
            else
            {
                this.temporaryArray.add(i, array.get(rightPtr));
                rightPtr++;
            }
        }
        for (int i = 0; i < length; i++)
            array.set(i + start, temporaryArray.get(i));
    }

    private void MergeSort(ArrayList<Word> array, int start, int end)
    {
        if (start == end) return;
        int middle = (start + end) / 2;
        MergeSort(array, start, middle);
        MergeSort(array, middle + 1, end);
        Merge(array, start, middle, end);

    }

    private void MergeSort(ArrayList<Word> array){
        this.temporaryArray = new ArrayList<>(array.size());
        MergeSort(array, 0, array.size() - 1);
    }
}