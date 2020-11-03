package handler;


import common.Command;
import common.User;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import parser.Parser;

import java.io.IOException;
import java.util.Hashtable;

import static bot.WorkWithMessage.createMessage;
import static bot.WorkWithMessage.setChatId;

public class MainLogic {
    private final Hashtable<Long, User> users;
    private final Hashtable<Command, AbstractHandler> handlers;

    public MainLogic(String botName) throws IOException, ParseException {
        users = new Hashtable<>();
        handlers = new Hashtable<>();
        handlers.put(Command.LEARN, new Learn(botName));
        handlers.put(Command.START, new Start(botName));
        handlers.put(Command.HELP, new Help(botName));
        handlers.put(Command.NONE, new Help(botName));
    }

    public SendMessage operate(Long chatId, String query, String userName) {
        Command command = Parser.getCommand(query);

        if (command == Command.NONE)
            command = getCurrentCommandUser(chatId);

        if (!users.containsKey(chatId))
            users.put(chatId, new User(userName));

        if (command == Command.STOP) {
            String name = users.get(chatId).getName();
            users.remove(chatId);
            return setChatId(createMessage("Пока, " + name + "!"), chatId);
        }

        return setChatId(handlers.get(command).operate(query.trim().toLowerCase(),
                users.get(chatId)), chatId);
    }

    private Command getCurrentCommandUser(Long chatId) {
        User user = users.get(chatId);
        return user != null
                ? user.stateDialog.getKey()
                : Command.NONE;
    }
}
