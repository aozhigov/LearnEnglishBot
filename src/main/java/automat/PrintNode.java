package automat;

import common.KeyboardBot;
import common.MessageBot;

import java.util.ArrayList;

public class PrintNode {
    public String value;
    private KeyboardBot keyboard;
    private HandlerNode handler;

    public PrintNode(String value,
                     KeyboardBot keyboard) {
        this.value = value;
        this.keyboard = keyboard;
    }

    public PrintNode(String value) {
        this.value = value;
        this.keyboard = new KeyboardBot(0, new ArrayList<>());
    }

    public MessageBot action(String word) {
        String text = value.replaceAll("\\{\\{WORD\\}\\}", word);
        return new MessageBot(text, keyboard, move());
    }

    public MessageBot action(String word, KeyboardBot keyboard) {
        String text = value.replaceAll("\\{\\{WORD\\}\\}", word);
        return new MessageBot(text, keyboard, move());
    }

    public HandlerNode move() {
        return handler;
    }

    public void initLinks(HandlerNode links) {
        this.handler = links;
    }

    public void setKeyboard(KeyboardBot keyboard) {
        this.keyboard = keyboard;
    }
}
