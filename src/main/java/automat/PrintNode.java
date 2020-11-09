package automat;

import common.Tuple;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.List;

import static bot.WorkWithMessage.createMsgWithKeyboard;

public class PrintNode {
    public String value;
    public List<HandlerNode> links;
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
        return links.size() > 0
                ? links.get(0)
                : null;
    }

    public void initLinks(List<HandlerNode> links) {
        this.links = links;
    }
}
