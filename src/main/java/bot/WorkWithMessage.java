package bot;

import common.Tuple;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static bot.Keyboard.addKeyboard;

public class WorkWithMessage {
    public static SendMessage setChatId(SendMessage message, Long chatId) {
//        if (message == null)
//            throw
        message.setChatId(chatId);

        return message;
    }

    public static SendMessage createMessage(String text) {
        SendMessage msg = new SendMessage();
        msg.setText(text);
        return msg;
    }

    public static SendMessage createMsgWithKeyboard(String text, Tuple<Integer, List<String>> keyboard) {
        SendMessage msg = createMessage(text);

        if (keyboard != null)
            return addKeyboard(keyboard.getKey(), msg, keyboard.getValue());

        return addKeyboard(0, msg, new ArrayList<>());
    }
}
