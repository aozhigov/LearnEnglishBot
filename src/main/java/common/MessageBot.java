package common;

import automat.HandlerNode;

import java.util.List;

public class MessageBot {
    private final HandlerNode handler;
    private final Tuple<Integer, List<String>> keyboard;
    private final String text;

    public MessageBot(String text,
                      Tuple<Integer, List<String>> keyboard,
                      HandlerNode handler) {
        this.handler = handler;
        this.keyboard = keyboard;
        this.text = text;
    }

    private MessageBot(String text,
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

    public MessageBot getMessageWithoutHandler() {
        return new MessageBot(text, keyboard);
    }
}
