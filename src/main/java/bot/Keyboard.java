package bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    public static SendMessage addKeyboard(int countOfRows,
                                          SendMessage sendMessage,
                                          List<String> args) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        int counter = 0;
        for (int i = 0; i < countOfRows; i++) {
            KeyboardRow row = new KeyboardRow();

            int countOfWords = args.size() / countOfRows;
            for (int j = counter; j < countOfWords + counter; j++)
                if (j < args.size())
                    row.add(args.get(j));

            counter += countOfWords;
            keyboard.add(row);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        return sendMessage;
    }
}