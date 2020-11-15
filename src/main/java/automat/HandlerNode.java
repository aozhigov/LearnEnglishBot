package automat;

import common.Event;
import common.Tuple;
import common.User;
import common.Word;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public abstract class HandlerNode {
    public Hashtable<Event, PrintNode> links;
    private final Hashtable<String, Event> commands;

    public HandlerNode() {
        commands = new Hashtable<>();
        commands.put("/stat", Event.STATISTIC);
        commands.put("/topic", Event.WRONG_TOPIC);
        commands.put("/stop", Event.STOP);
        commands.put("/help", Event.HELP);
        commands.put("/exit", Event.EXIT);
    }

    public abstract Tuple<SendMessage, HandlerNode> action(String query, User user);

//    public PrintNode move(boolean condition) {
//        return links.size() > 1
//                ? condition
//                    ? links.get(0)
//                    : links.get(1)
//                : null;
//    }

    public PrintNode move(Event event) {
        return links.get(event);
    }

    public void initLinks(Hashtable<Event, PrintNode> links) {
        this.links = links;
    }

    public Event checkCommand(String query){
        return query.startsWith("/")
                ? commands.get(query.split(" ", 1)[0])
                : Event.NONE;
    }
}
