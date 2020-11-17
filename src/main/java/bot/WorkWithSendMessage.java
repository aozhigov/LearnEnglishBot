package bot;

import common.MessageBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;

import static bot.Keyboard.addUnderMsgKeyboard;

public class WorkWithSendMessage {
    public static SendMessage setChatId(SendMessage message, Long chatId) {
        message.setChatId(chatId);

        return message;
    }

    public static SendMessage createMessage(String text) {
        SendMessage msg = new SendMessage();
        msg.setText(text);
        return msg;
    }

    public static SendMessage createMsgWithKeyboard(MessageBot answer) {
        SendMessage msg = createMessage(answer.getText());

        if (answer.getKeyboard() != null)
            return addUnderMsgKeyboard(answer.getKeyboard().getKey(), msg,
                    answer.getKeyboard().getValue());

        return addUnderMsgKeyboard(0, msg, new ArrayList<>());
    }
}
