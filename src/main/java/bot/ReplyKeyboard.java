package bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboard {
    public static SendMessage addKeyboard(SendMessage msg,
                                          List<String> keyboard) {
        return msg.setReplyMarkup(addSimpleKeyboard(keyboard));
    }

    private static ReplyKeyboardMarkup addSimpleKeyboard(List<String> args) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        if (args == null || args.size() == 0)
            return replyKeyboardMarkup;

        List<KeyboardRow> keyboard = new ArrayList<>();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        int countOfRows = (args.size() / 2) + (args.size() % 2);
        int counter = 0;

        for (int i = 0; i < countOfRows; i++) {
            KeyboardRow row = new KeyboardRow();

            int countOfWords = 2;
            for (int j = counter; j < countOfWords + counter; j++)
                if (j < args.size())
                    row.add(args.get(j));

            counter += countOfWords;
            keyboard.add(row);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}