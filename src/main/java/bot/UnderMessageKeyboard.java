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
                                          ArrayList<Tuple<String, String>> args) {
        // В args в качестве ключа передаётся строка, которую будет видеть user
        // В качестве значения будет строка, которая при нажатии на кнопку пойдёт на сервер
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        int counter = 0;
        for (int i = 0; i < countOfRows; i++) {
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

            int countOfWords = args.size() / countOfRows;
            for (int j = counter; j < countOfWords + counter; j++)
                if (j < args.size()){
                    InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                    inlineKeyboardButton.setText(args.get(j).getKey());// То, что видит user
                    inlineKeyboardButton.setCallbackData(args.get(j).getValue());// То, что придёт на сервер
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