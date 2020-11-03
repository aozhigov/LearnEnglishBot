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

    public static SendMessage createMsgWithKeyboard(String text, int idxKeyboard) {
        ArrayList<Tuple<Integer, List<String>>> keyboards = new ArrayList<>();
        keyboards.add(new Tuple<>(2, Arrays.asList("linq", "string", "io-api", "collection-api")));
        keyboards.add(new Tuple<>(1, Arrays.asList("Да", "Нет")));
        keyboards.add(new Tuple<>(3, Arrays.asList("/learn", "/stop", "/help")));

        SendMessage msg = createMessage(text);
        if (idxKeyboard >= 0) {
            Tuple<Integer, List<String>> keyboard = keyboards.get(idxKeyboard);
            return addKeyboard(keyboard.getKey(), msg, keyboard.getValue());
        }

        return addKeyboard(0, msg, new ArrayList<>());
    }
}
