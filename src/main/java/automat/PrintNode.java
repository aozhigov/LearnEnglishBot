package automat;

import common.MessageBot;

import java.util.ArrayList;
import java.util.List;

public class PrintNode {
    public String value;
    private List<String> keyboard;
    private HandlerNode handler;

    public PrintNode(String value,
                     List<String> keyboard) {
        this.value = value;
        this.keyboard = keyboard;
    }

    public PrintNode(String value) {
        this.value = value;
        this.keyboard = new ArrayList<>(0);
    }

    public MessageBot action(String word) {
        String text = value.replaceAll("\\{\\{WORD\\}\\}", word);
        return new MessageBot(text, keyboard, move());
    }

    public MessageBot action(String word, List<String> keyboard) {
        String text = value.replaceAll("\\{\\{WORD\\}\\}", word);
        return new MessageBot(text, keyboard, move());
    }

    public HandlerNode move() {
        return handler;
    }

    public void initLinks(HandlerNode links) {
        this.handler = links;
    }

    public void setKeyboard(List<String> keyboard) {
        this.keyboard = keyboard;
    }
}
