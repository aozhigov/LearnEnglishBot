package handler;

import common.Tuple;
import common.User;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.Arrays;

import static bot.WorkWithMessage.createMsgWithKeyboard;

public class Start extends AbstractHandler {

    public Start(String botName) {
        super(botName);
    }

    public SendMessage operate(String query, User user) {
        return createMsgWithKeyboard("Привет, " + user.getName() + ", меня зовут " + this.botName
                + ", и я помогу тебе выучить английские слова!\n" +
                "Теперь можешь выбрать одну из команд",
                new Tuple<>(3, Arrays.asList("/learn", "/stop", "/help")));
    }
}
