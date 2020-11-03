package parser;

import common.Command;

public class Parser {
    public static Boolean isCommand(String query) {
        return query.startsWith("/");
    }

    public static Command getCommand(String query) {
        query = query.toLowerCase().trim();
        if (query.startsWith("/start"))
            return Command.START;
        if (query.startsWith("/stop"))
            return Command.STOP;
        if (query.startsWith("/learn"))
            return Command.LEARN;
        if (query.startsWith("/help"))
            return Command.HELP;
//        if (query.startsWith("/add"))
//            return Command.ADD;
        return Command.NONE;
    }
}
