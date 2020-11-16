package bot;

import common.Tuple;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class UnderMessageKeyboard {
    public static SendMessage addKeyboard(int countOfRows,
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