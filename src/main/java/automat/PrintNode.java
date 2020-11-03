package automat;

import common.Tuple;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.List;

import static bot.WorkWithMessage.createMsgWithKeyboard;

public class PrintNode {
    public String value;
    public List<HandlerNode> links;
    private final int idxKeyboard;

    public PrintNode(String value, int idxKeyboard) {
        this.value = value;
        this.idxKeyboard = idxKeyboard;
    }

    public Tuple<SendMessage, HandlerNode> action(String word) {
        String text = value.replaceAll("\\{\\{WORD\\}\\}", word);
        return new Tuple<>(createMsgWithKeyboard(text, idxKeyboard), move());
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
