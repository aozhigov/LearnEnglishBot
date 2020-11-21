package automat;

import common.Event;
import common.MessageBot;
import common.User;

import java.util.Hashtable;

public abstract class HandlerNode {
    private final Hashtable<String, Event> commands;
    public Hashtable<Event, PrintNode> links;

    public HandlerNode() {
        commands = new Hashtable<>();
        commands.put("/stat", Event.STATISTIC);
        commands.put("/topic", Event.CHANGE_TOPIC);
        commands.put("/help", Event.HELP);
        commands.put("/exit", Event.EXIT);
    }

    public abstract MessageBot action(
            String query, User user);

    public PrintNode move(Event event) {
        return links.get(event);
    }

    public void initLinks(Hashtable<Event, PrintNode> links) {
        this.links = links;
    }

    public Event checkCommand(String query) {
        return query.startsWith("/") && !query.equals("/hint")
                ? commands.get(query.split(" ", 1)[0])
                : Event.NONE;
    }
}
