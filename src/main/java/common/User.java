package common;

import automat.HandlerNode;
import vocabulary.Selection;
import vocabulary.Word;

import java.util.HashMap;

public class User {
    private final String userName;
    private final Long id;
    private Tuple<Event, HandlerNode> stateDialog;
    private Tuple<String, Word> stateLearn;
    private HashMap<String, Selection> myVocabularies;
    private Tuple<String, Integer> stateAddVocabulary;
    private Integer countWordsVocabulary;

    public User(HashMap<String, Selection> startVocabularies) {
        userName = "";
        id = (long) -1;
        this.myVocabularies = startVocabularies;
        stateAddVocabulary = new Tuple<>("", -1);
    }

    public User(String name, Long id, HashMap<String, Selection> startVocabularies) {
        userName = name;
        stateDialog = new Tuple<>(Event.FIRST_START, null);
        stateLearn = new Tuple<>("", null);
        stateAddVocabulary = new Tuple<>("", -1);
        this.id = id;
        this.myVocabularies = startVocabularies;
    }

    public String getName() {
        return userName;
    }

    public Long getId() {
        return this.id;
    }

    public Tuple<Event, HandlerNode> getStateDialog() {
        return stateDialog;
    }

    public void setStateDialog(Event event) {
        stateDialog.setKey(event);
    }

    public void setStateDialog(HandlerNode handler) {
        stateDialog.setValue(handler);
    }

    public Tuple<String, Word> getStateLearn() {
        return stateLearn;
    }

    public void setStateLearn(String name) {
        stateLearn.setKey(name);
    }

    public void setStateLearn(Word word) {
        stateLearn.setValue(word);
    }

    public HashMap<String, Selection> getMyVocabularies() {
        return myVocabularies;
    }

    public void addVocabularies(String name, Selection selection) {
        stateAddVocabulary.setTuple(name, 0);
        myVocabularies.put(name, selection);
    }

    public String getNextWord(boolean isKnow) {
        if (!isKnow)
            countWordsVocabulary--;
        stateAddVocabulary.setValue(stateAddVocabulary.getValue() + 1);
        if (stateAddVocabulary.getValue() >= myVocabularies.get(stateAddVocabulary.getKey()).getSize()
                || countWordsVocabulary == 0){
            myVocabularies.get(stateAddVocabulary.getKey())
                    .delAllStartIdx(stateAddVocabulary.getValue() - 1);
            return null;
        }

        return myVocabularies.get(stateAddVocabulary.getKey())
                .getWithoutStat(stateAddVocabulary.getValue() - 1);
    }

    public void setLastAddMyVocabularies(String name) {
        Selection temp = myVocabularies.get(stateAddVocabulary.getKey());
        myVocabularies.remove(stateAddVocabulary.getKey());
        myVocabularies.put(name, temp);
        stateAddVocabulary.setTuple("", -1);
    }

    public void delWord() {
        myVocabularies.get(stateAddVocabulary.getKey())
                .delWord(stateAddVocabulary.getValue());
    }

    public void setCountWordsVocabulary(int count){
        countWordsVocabulary = count > 0
                ? count
                : 20;
    }
}
