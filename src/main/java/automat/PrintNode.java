package automat;

import common.Message;
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

    public Message action(String word) {
        String text = value.replaceAll("\\{\\{WORD\\}\\}", word);
        return new Message(text, keyboard, move());
    }

    public HandlerNode move() {
        return handler;
    }

    public void initLinks(HandlerNode links) {
        this.handler = links;
    }
}
