package automat;

import common.MessageBot;
import common.Tuple;

import java.util.List;

public class PrintNode {
    private final Tuple<Integer, List<String>> keyboard;
    public String value;
    private HandlerNode handler;

    public PrintNode(String value, Tuple<Integer, List<String>> keyboard) {
        this.value = value;
        this.keyboard = keyboard;
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
