package handler;

import automat.HandlerNode;
import automat.LearnNodes.*;
import automat.PrintNode;
import common.*;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.io.IOException;
import java.util.*;

import static parser.JsonParser.getDictsFromJson;

public class Learn extends AbstractHandler {
    private final Hashtable<String, ArrayList<Word>> vocabularies;
    private final HandlerNode root;
    private final ArrayList<Tuple<Integer, List<String>>> keyboards;

    public Learn(String botName) throws IOException, ParseException {
        super(botName);
        vocabularies = getDictsFromJson();

        keyboards = new ArrayList<>();
        keyboards.add(new Tuple<>(2, Arrays.asList("linq", "string", "io-api", "collection-api")));
        keyboards.add(new Tuple<>(1, Arrays.asList("Да", "Нет")));
        keyboards.add(new Tuple<>(3, Arrays.asList("/learn", "/stop", "/help")));

        root = initializationAutomat();
    }

    private HandlerNode initializationAutomat() {
        PrintNode startStr = new PrintNode("Давай начнем, {{WORD}}!\n" +
                "Тебе надо написать перевод слова, которое я тебе отправлю.\n" +
                "Выбери тему из вариантов, предложенных ниже",
                keyboards.get(0));
        PrintNode firstEnWordStr = new PrintNode("Вот слово для перевода: {{WORD}}",
                null);
        PrintNode toTryTo = new PrintNode("{{WORD}}, хочешь попробовать еще?",
                keyboards.get(1));
        PrintNode secondEnWordStr = new PrintNode("Хорошо подумай и отвечай, слово: {{WORD}}",
                null);
        PrintNode ruWordStr = new PrintNode("Не расстраивайся, вот перевод: {{WORD}}.\n" +
                "Хочешь продолжить?",
                keyboards.get(1));
        PrintNode wrongTopicStr = new PrintNode("{{WORD}}, не правильно выбрана тема.\n" +
                "Есть только такие темы",
                keyboards.get(0));
        PrintNode endStr = new PrintNode("{{WORD}}, можешь выбрать одну из команд",
                keyboards.get(2));
        PrintNode hint = new PrintNode("Вот подсказка {{WORD}}.\n" +
                "Хочешь попробовать еще?",
                keyboards.get(1));

        ZeroNode zero = new ZeroNode();
        StartNode start = new StartNode(vocabularies);
        CheckWordNode checkWord = new CheckWordNode(vocabularies);
        YesNoNode yesNo = new YesNoNode(vocabularies);
        ExitOrNextNode exitOrNext = new ExitOrNextNode(vocabularies);

        startStr.initLinks(Collections.singletonList(start));
        firstEnWordStr.initLinks(Collections.singletonList(checkWord));
        toTryTo.initLinks(Collections.singletonList(yesNo));
        secondEnWordStr.initLinks(Collections.singletonList(checkWord));
        ruWordStr.initLinks(Collections.singletonList(exitOrNext));
        wrongTopicStr.initLinks(Collections.singletonList(start));
        endStr.initLinks(Collections.singletonList(zero));
        hint.initLinks(Collections.singletonList(yesNo));

        zero.initLinks(getLinks(
                Collections.singletonList(Event.START),
                Collections.singletonList(startStr)));
        start.initLinks(getLinks(
                Arrays.asList(Event.FIRST_EN_WORD, Event.WRONG_TOPIC, Event.END),
                Arrays.asList(firstEnWordStr, wrongTopicStr, endStr)));
        checkWord.initLinks(getLinks(
                Arrays.asList(Event.FIRST_EN_WORD, Event.TRY, Event.HINT, Event.END),
                Arrays.asList(firstEnWordStr, toTryTo, hint, endStr)));
        yesNo.initLinks(getLinks(
                Arrays.asList(Event.SECOND_EN_WORD, Event.RU_WORD, Event.END),
                Arrays.asList(secondEnWordStr, ruWordStr, endStr)));
        exitOrNext.initLinks(getLinks(
                Arrays.asList(Event.FIRST_EN_WORD, Event.END),
                Arrays.asList(firstEnWordStr, endStr)));

        return zero;
    }

    public SendMessage operate(String query, User user) {
        query = query.trim().toLowerCase();

        if (user.stateDialog.getKey() != Command.LEARN)
            user.stateDialog = new Tuple<>(Command.LEARN, root);

        Tuple<SendMessage, HandlerNode> answer = user.stateDialog.getValue().action(query, user);
        user.stateDialog.setValue(answer.getValue());

        return answer.getKey();
    }

    private <T> Hashtable<Event, T> getLinks(List<Event> events, List<T> nodes){
        if (events.size() != nodes.size())
            return null;

        Hashtable<Event, T> temp = new Hashtable<>();
        for (int i = 0; i < events.size(); i++)
            temp.put(events.get(i), nodes.get(i));

        return temp;
    }
}
