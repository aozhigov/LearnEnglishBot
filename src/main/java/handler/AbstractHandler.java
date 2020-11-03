package handler;


import common.User;
import org.telegram.telegrambots.api.methods.send.SendMessage;

public abstract class AbstractHandler {
    String botName;

    AbstractHandler(String botName) {
        this.botName = botName;
    }

    public abstract SendMessage operate(String query, User user);

}

