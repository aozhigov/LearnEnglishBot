package common;

import automat.HandlerNode;

import java.util.Random;

public class User {
    private String userName;
    public Tuple<Event, HandlerNode> stateDialog;
    public Tuple<String, Integer> stateLearn;
    private final Random rnd;

    public User(String name){
        userName = name;
        stateDialog = new Tuple<>(Event.FIRST_START, null);
        stateLearn = new Tuple<>("", -1);
        rnd = new Random();
    }

    public String getName(){
        return userName;
    }

    public int getNextIdWord(int size){
        stateLearn.setValue(rnd.nextInt(size));
        return stateLearn.getValue();
    }
}
