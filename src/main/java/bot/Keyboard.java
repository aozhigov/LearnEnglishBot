package bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
    public static SendMessage addSimpleKeyboard(int countOfRows,
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

    public static SendMessage addUnderMsgKeyboard(int countOfRows,
                                                  SendMessage sendMessage,
                                                  List</*Tuple<String, */String/*>*/> args) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        int counter = 0;
        for (int i = 0; i < countOfRows; i++) {
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

            int countOfWords = args.size() / countOfRows;
            for (int j = counter; j < countOfWords + counter; j++)
                if (j < args.size()) {
                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setText(args.get(j)/*.getKey()*/);
                    //inlineKeyboardButton.setCallbackData(args.get(j).getValue());
                    inlineKeyboardButton.setCallbackData(args.get(j));
                    inlineKeyboardButtons.add(inlineKeyboardButton);
                }

            counter += countOfWords;
            keyboard.add(inlineKeyboardButtons);
        }

        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }
}