package bot;

import common.KeyboardBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboard {
    public static SendMessage addKeyboard(SendMessage msg,
                                          KeyboardBot keyboard) {
        return msg.setReplyMarkup(addSimpleKeyboard(keyboard.getButtons()));
    }

    private static ReplyKeyboardMarkup addSimpleKeyboard(List<String> args) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        int countOfRows = (args.size() / 2) + (args.size() % 2);
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
        return replyKeyboardMarkup;
    }
}