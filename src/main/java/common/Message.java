package common;

import automat.HandlerNode;

import java.util.List;

public class Message {
    private final HandlerNode handler;
    private final Tuple<Integer, List<String>> keyboard;
    private final String text;

    public Message(String text,
                   Tuple<Integer, List<String>> keyboard,
                   HandlerNode handler) {
        this.handler = handler;
        this.keyboard = keyboard;
        this.text = text;
    }

    private Message(String text,
                    Tuple<Integer, List<String>> keyboard) {
        this.handler = null;
        this.keyboard = keyboard;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Tuple<Integer, List<String>> getKeyboard() {
        return keyboard;
    }

    public HandlerNode getHandler() {
        return handler;
    }

    public Message getMessageWithoutHandler() {
        return new Message(text, keyboard);
    }
}
