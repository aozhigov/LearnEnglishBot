package common;

import automat.HandlerNode;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

import static bot.ReplyKeyboard.addKeyboard;

public class MessageBot {
    private final HandlerNode handler;
    private final List<String> keyboard;
    private final String text;

    public MessageBot(String text,
                      List<String> keyboard,
                      HandlerNode handler) {
        this.handler = handler;
        this.keyboard = keyboard;
        this.text = text;
    }

    public MessageBot(String text,
                      HandlerNode handler) {
        this.handler = handler;
        this.keyboard = new ArrayList<>(0);
        this.text = text;
    }

    private MessageBot(String text,
                       List<String> keyboard) {
        this.handler = null;
        this.keyboard = keyboard;
        this.text = text;
    }

    public HandlerNode getHandler() {
        return handler;
    }

    public MessageBot getMessageWithoutHandler() {
        return new MessageBot(text, keyboard);
    }

    public SendMessage getSendMessage() {
        SendMessage msg = new SendMessage();
        msg.setText(text);
        return addKeyboard(msg, keyboard);
    }
}
