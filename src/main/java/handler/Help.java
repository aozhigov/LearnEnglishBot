package handler;

import common.User;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import static bot.WorkWithMessage.createMessage;

public class Help extends AbstractHandler {

    Help(String botName) {
        super(botName);
    }

    @Override
    public SendMessage operate(String query, User user) {
        String help = "/start - начинает общение\n" +
                "/learn - запускает редим обучения\n" +
                "/help - выводит справку\n" +
                "/stop - удаляет все данные пользователя\n";

        return createMessage(help);
    }
}
