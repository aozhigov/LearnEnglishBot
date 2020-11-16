package common;

import automat.HandlerNode;
import vocabulary.Word;

public class User {
    public Tuple<Event, HandlerNode> stateDialog;
    public Tuple<String, Word> stateLearn;
    private final String userName;
    private final Long id;


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
}
