package automat;

import common.Event;
import common.MessageBot;
import common.User;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

public abstract class HandlerNode {
    private final Hashtable<String, Event> commands;
    public HashMap<Event, PrintNode> links;

    public HandlerNode() {
        commands = new Hashtable<>();
        commands.put("/stat", Event.STATISTIC);
        commands.put("/topic", Event.CHANGE_TOPIC);
        commands.put("/help", Event.HELP);
        commands.put("/exit", Event.EXIT);
        commands.put("/add", Event.ADD_VOCABULARY);
    }

    public abstract MessageBot action(
            String query, User user) throws IOException, ParseException;

    public PrintNode move(Event event) {
        return links.get(event);
    }

    public void initLinks(HashMap<Event, PrintNode> links) {
        this.links = links;
    }

    public Event checkCommand(String query) {
        return query.startsWith("/")
                ? commands.get(query.split(" ", 1)[0])
                : Event.NONE;
    }
}
