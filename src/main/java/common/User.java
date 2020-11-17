package common;

import automat.HandlerNode;
import vocabulary.Word;

public class User {
    private final String userName;
    private final Long id;
    private Tuple<Event, HandlerNode> stateDialog;
    private Tuple<String, Word> stateLearn;

    public User() {
        userName = "";
        id = (long) -1;
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
}
