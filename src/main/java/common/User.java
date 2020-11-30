package common;

import automat.HandlerNode;
import vocabulary.Selection;
import vocabulary.Word;

import java.util.HashMap;
import java.util.List;

public class User {
    private final String userName;
    private final Long id;
    private Tuple<Event, HandlerNode> stateDialog;
    private Tuple<String, Word> stateLearn;
    private HashMap<String, Selection> myVocabularies;
    public Integer currIdx;
    public String currVocab;

    public User(HashMap<String, Selection> startVocabularies) {
        userName = "";
        id = (long) -1;
        this.myVocabularies = startVocabularies;
    }

    public User(String name, Long id) {
        userName = name;
        stateDialog = new Tuple<>(Event.FIRST_START, null);
        stateLearn = new Tuple<>("", null);
        this.id = id;
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

    public HashMap<String, Selection> getMyVocabularies(){
        return myVocabularies;
    }

    public void addVocabularies(String name, Selection selection){
        myVocabularies.put(name, selection);
    }

    //TODO сократить remove до О(1)
    //TODO обавить состояния добавления слов и возвращать их в методах

    public String getNextWord(){
        return myVocabularies.get(currVocab).getWithoutStat(currIdx);
    }

    public void setLastAddMyVocabularies(String name){

    }

    public void delWord(){
        //TODO удалить последнее слово которое проверяли
    }
}
