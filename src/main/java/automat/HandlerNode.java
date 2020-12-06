package automat;

import common.Event;
import common.MessageBot;
import common.User;
import org.json.simple.parser.ParseException;
import vocabulary.Selection;
import vocabulary.Word;

import java.io.IOException;
import java.util.ArrayList;
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

    public MessageBot checkCommand(String query, User user) {
        Event event = commands.get(query.split(" ")[0]);

        if (event == Event.CHANGE_TOPIC)
            return move(event).action(user.getName(),
                    new ArrayList<>(user.getUserVocabularies().keySet()));

        if (event == Event.ADD_VOCABULARY){
            String[] temp = query.split(" ");
            try {
                if (temp.length > 1)
                    user.setCountWordsForAdd(Integer.parseInt(temp[1]));
                else
                    user.setCountWordsForAdd(20);
            }
            catch (NumberFormatException e){
                e.printStackTrace();
                user.setCountWordsForAdd(20);
            }
        }

        return event != null
                ? move(event).action(user.getName())
                : null;
    }

    protected String getFirstWord(String query, User user) {
        if (user.getStateLearn().getKey().equals(""))
            user.setStateLearn(query);

        Selection vocabulary = user.getUserVocabularies().get(user.getStateLearn().getKey());
        Word temp = vocabulary.getEnWord(user);
        user.setStateLearn(temp);

        return user.getStateLearn().getValue().getEn();
    }
}
