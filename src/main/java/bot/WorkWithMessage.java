package bot;

import common.Message;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;

import static bot.UnderMessageKeyboard.addKeyboard;

public class WorkWithMessage {
    public static SendMessage setChatId(SendMessage message, Long chatId) {
        message.setChatId(chatId);

        return message;
    }

    public static SendMessage createMessage(String text) {
        SendMessage msg = new SendMessage();
        msg.setText(text);
        return msg;
    }

    public static SendMessage createMsgWithKeyboard(Message answer) {
        SendMessage msg = createMessage(answer.getText());

        if (answer.getKeyboard() != null)
            return addKeyboard(answer.getKeyboard().getKey(), msg,
                    answer.getKeyboard().getValue());

        return addKeyboard(0, msg, new ArrayList<>());
    }
}
