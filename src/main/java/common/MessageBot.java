package common;

import automat.HandlerNode;
import bot.KeyboardType;

import java.util.ArrayList;
import java.util.List;

public class MessageBot {
    private final HandlerNode handler;
    private final Tuple<Integer, List<String>> keyboard;
    private final String text;
    private final KeyboardType typeKeyboard;

    public MessageBot(String text,
                      KeyboardType type,
                      Tuple<Integer, List<String>> keyboard,
                      HandlerNode handler) {
        this.handler = handler;
        this.keyboard = keyboard;
        this.text = text;
        this.typeKeyboard = type;
    }

    public MessageBot(String text,
                      HandlerNode handler) {
        this.handler = handler;
        this.keyboard = new Tuple<>(0, new ArrayList<>());
        this.text = text;
        this.typeKeyboard = KeyboardType.NONE;
    }

    private MessageBot(String text,
                       KeyboardType type,
                       Tuple<Integer, List<String>> keyboard) {
        this.handler = null;
        this.keyboard = keyboard;
        this.text = text;
        this.typeKeyboard = type;
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
        return new MessageBot(text, typeKeyboard, keyboard);
    }

    public KeyboardType getKeyboardType() {
        return typeKeyboard;
    }
}
