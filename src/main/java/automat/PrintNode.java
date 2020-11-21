package automat;

import bot.KeyboardType;
import common.MessageBot;
import common.Tuple;

import java.util.ArrayList;
import java.util.List;

public class PrintNode {
    private final Tuple<Integer, List<String>> keyboard;
    private final KeyboardType keyboardType;
    public String value;
    private HandlerNode handler;

    public PrintNode(String value,
                     KeyboardType type,
                     Tuple<Integer, List<String>> keyboard) {
        this.value = value;
        this.keyboard = keyboard;
        this.keyboardType = type;
    }

    public PrintNode(String value) {
        this.value = value;
        this.keyboard = new Tuple<>(0, new ArrayList<>());
        this.keyboardType = KeyboardType.NONE;
    }

    public MessageBot action(String word) {
        String text = value.replaceAll("\\{\\{WORD\\}\\}", word);
        return new MessageBot(text, keyboardType, keyboard, move());
    }

    public HandlerNode move() {
        return handler;
    }

    public void initLinks(HandlerNode links) {
        this.handler = links;
    }
}
