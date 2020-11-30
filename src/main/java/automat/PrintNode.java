package automat;

import common.KeyboardBot;
import common.MessageBot;
import common.Tuple;

import java.util.ArrayList;
import java.util.List;

public class PrintNode {
    private final KeyboardBot keyboard;
    public String value;
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

    public HandlerNode move() {
        return handler;
    }

    public void initLinks(HandlerNode links) {
        this.handler = links;
    }
}
