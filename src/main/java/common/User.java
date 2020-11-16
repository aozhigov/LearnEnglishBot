package common;

import automat.HandlerNode;

import java.util.Random;

public class User {
    private String userName;
    public Tuple<Event, HandlerNode> stateDialog;
    public Tuple<String, Word> stateLearn;
    private Long id;


    public User(String name, Long id){
        userName = name;
        stateDialog = new Tuple<>(Event.FIRST_START, null);
        stateLearn = new Tuple<>("", null);
        this.id = id;
    }

    public String getName(){
        return userName;
    }

//    public int getNextIdWord(int size){
//        stateLearn.setValue(rnd.nextInt(size));
//        return stateLearn.getValue();
//    }

    public Long getId(){
        return this.id;
    }
}
