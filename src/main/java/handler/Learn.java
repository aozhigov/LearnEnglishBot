package handler;

import automat.HandlerNode;
import automat.LearnNodes.*;
import automat.PrintNode;
import common.Command;
import common.Tuple;
import common.User;
import common.Word;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.io.IOException;
import java.util.*;

import static parser.JsonParser.getDictsFromJson;

public class Learn extends AbstractHandler {
    private List<String> namesVocabularies;
    private Hashtable<String, ArrayList<Word>> dictionaries;
    private final HandlerNode root;

    public Learn(String botName) throws IOException, ParseException {
        super(botName);
        initializationDicts();
        root = initializationAutomat();
    }

    private HandlerNode initializationAutomat() {
        ArrayList<PrintNode> printNodes = new ArrayList<>();
        printNodes.add(new PrintNode("Давай начнем, {{WORD}}!\n" +
                "Тебе надо написать перевод слова, которое я тебе отправлю.\n" +
                "Выбери тему из вариантов, предложенных ниже", 0));
        printNodes.add(new PrintNode("Вот слово для перевода: {{WORD}}", -1));
        printNodes.add(new PrintNode("{{WORD}}, хочешь попробовать еще?", 1));
        printNodes.add(new PrintNode("Хорошо подумай и отвечай, слово: {{WORD}}", -1));
        printNodes.add(new PrintNode("Не расстраивайся, вот перевод: {{WORD}}.\n" +
                "Хочешь продолжить?", 1));
        printNodes.add(new PrintNode("{{WORD}}, не правильно выбрана тема.\n" +
                "Есть только такие темы", 0));
        printNodes.add(new PrintNode("Можешь выбрать одну из команд", 2));

        ArrayList<HandlerNode> handlerNodes = new ArrayList<>();
        handlerNodes.add(new ZeroNode());
        handlerNodes.add(new StartNode());
        handlerNodes.add(new CheckWordNode());
        handlerNodes.add(new YesNoNode());
        handlerNodes.add(new ExitOrNextNode());

        printNodes.get(0).initLinks(Collections.singletonList(handlerNodes.get(1)));
        printNodes.get(1).initLinks(Collections.singletonList(handlerNodes.get(2)));
        printNodes.get(2).initLinks(Collections.singletonList(handlerNodes.get(3)));
        printNodes.get(3).initLinks(Collections.singletonList(handlerNodes.get(2)));
        printNodes.get(4).initLinks(Collections.singletonList(handlerNodes.get(4)));
        printNodes.get(5).initLinks(Collections.singletonList(handlerNodes.get(1)));
        printNodes.get(6).initLinks(Collections.singletonList(handlerNodes.get(0)));

        handlerNodes.get(0).initLinks(Arrays.asList(printNodes.get(0), printNodes.get(0)));
        handlerNodes.get(1).initLinks(Arrays.asList(printNodes.get(1), printNodes.get(5)));
        handlerNodes.get(2).initLinks(Arrays.asList(printNodes.get(1), printNodes.get(2)));
        handlerNodes.get(3).initLinks(Arrays.asList(printNodes.get(3), printNodes.get(4)));
        handlerNodes.get(4).initLinks(Arrays.asList(printNodes.get(1), printNodes.get(6)));

        return handlerNodes.get(0);
    }

    private void initializationDicts() throws IOException, ParseException {
        namesVocabularies = Arrays.asList("linq", "string", "io-api", "collection-api");
        dictionaries = getDictsFromJson();
    }

    public SendMessage operate(String query, User user) {
        query = query.trim().toLowerCase();

        if (user.stateDialog.getKey() != Command.LEARN)
            user.stateDialog = new Tuple<>(Command.LEARN, root);

        Tuple<SendMessage, HandlerNode> answer = user.stateDialog.getValue().action(query,
                user, namesVocabularies, dictionaries);
        user.stateDialog.setValue(answer.getValue());

        return answer.getKey();
    }
}
