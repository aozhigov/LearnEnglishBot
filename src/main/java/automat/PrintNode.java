package automat;

import common.Tuple;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.List;

import static bot.WorkWithMessage.createMsgWithKeyboard;

public class PrintNode {
    public String value;
    private HandlerNode handler;
    private final Tuple<Integer, List<String>> keyboard;

    public PrintNode(String value, Tuple<Integer, List<String>> keyboard) {
        this.value = value;
        this.keyboard = keyboard;
    }

    public Tuple<SendMessage, HandlerNode> action(String word) {
        String text = value.replaceAll("\\{\\{WORD\\}\\}", word);
        return new Tuple<>(createMsgWithKeyboard(text, keyboard), move());
    }

    public HandlerNode move() {
        return handler;
    }

    public void initLinks(HandlerNode links) {
        this.handler = links;
    }
}
