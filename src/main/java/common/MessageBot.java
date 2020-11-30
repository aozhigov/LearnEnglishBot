package common;

import automat.HandlerNode;

import java.util.ArrayList;
import java.util.List;

public class MessageBot {
    private final HandlerNode handler;
    private final KeyboardBot keyboard;
    private final String text;

    public MessageBot(String text,
                      KeyboardBot keyboard,
                      HandlerNode handler) {
        this.handler = handler;
        this.keyboard = keyboard;
        this.text = text;
    }

    public MessageBot(String text,
                      HandlerNode handler) {
        this.handler = handler;
        this.keyboard = new KeyboardBot(0, new ArrayList<>());
        this.text = text;
    }

    private MessageBot(String text,
                       KeyboardBot keyboard) {
        this.handler = null;
        this.keyboard = keyboard;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public KeyboardBot getKeyboard() {
        return keyboard;
    }

    public HandlerNode getHandler() {
        return handler;
    }

    public MessageBot getMessageWithoutHandler() {
        return new MessageBot(text, keyboard);
    }
}
