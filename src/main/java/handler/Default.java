package handler;


import automat.HandlerNode;
import automat.LearnNodes.*;
import automat.PrintNode;
import common.*;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import java.io.IOException;
import java.util.*;

import static parser.JsonParser.getVocabulariesFromJson;

public class Default {
    private final Hashtable<Long, User> users;
    private final Hashtable<String, Selection> vocabularies;
    private final HandlerNode root;
    private final ArrayList<Tuple<Integer, List<String>>> keyboards;

    public Default() throws IOException, ParseException {
        users = new Hashtable<>();
        vocabularies = getVocabulariesFromJson();

        keyboards = new ArrayList<>();
        keyboards.add(new Tuple<>(2, Arrays.asList("linq", "string", "io-api", "collection-api")));
        keyboards.add(new Tuple<>(1, Arrays.asList("Да", "Нет")));
        keyboards.add(new Tuple<>(3, Arrays.asList("/learn", "/stop", "/help", "/hint")));

        root = initializationAutomat();
    }

    public SendMessage operate(Long chatId, String query, String userName) {
        if (!users.containsKey(chatId))
            users.put(chatId, new User(userName, (long) (users.size() + 1)));

        User user = users.get(chatId);

        query = query.trim().toLowerCase();

        if (user.stateDialog.getValue() == null && user.stateDialog.getKey() == Event.FIRST_START)
            user.stateDialog.setValue(root);

        Tuple<SendMessage, HandlerNode> answer = user.stateDialog.getValue().action(query, user);
        user.stateDialog.setValue(answer.getValue());

        return answer.getKey();
    }

    private HandlerNode initializationAutomat() {
        PrintNode startFirstStr = new PrintNode("Привет, {{WORD}}!\n" +
                "Я бот, который поможет тебе выучить английские слова!\n" +
                "Тебе надо будет писать перевод слов, которые я тебе отправлю!\n" +
                "В любой момент ты можешь попросить помощи по командам (/help).\n" +
                "Выбери одну из тем, предложенных ниже:",
                keyboards.get(0));
        PrintNode startSecondStr = new PrintNode("С возвращением, {{WORD}}!\n" +
                "В любой момент ты можешь попросить помощи по командам (/help).\n" +
                "Выбери одну из тем, предложенных ниже:",
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
        PrintNode hintStr = new PrintNode("Вот подсказка {{WORD}}.\n" +
                "Хочешь попробовать еще?",
                keyboards.get(1));
        PrintNode exitStr = new PrintNode("Пока, {{WORD}}!",
                null);
        PrintNode statisticStr = new PrintNode("Здесь ты можешь полуить статистику",
                null);
        PrintNode helpStr = new PrintNode("help\n напиши что нибудб чтобы продолжить", null);
        PrintNode topicStr = new PrintNode("Выбери одну из тем, предложенных ниже:",
                keyboards.get(0));

        ZeroNode zero = new ZeroNode();
        ChoseTopicNode start = new ChoseTopicNode(vocabularies);
        CheckWordNode checkWord = new CheckWordNode(vocabularies);
        YesNoNode yesNo = new YesNoNode(vocabularies);
        ExitOrNextNode exitOrNext = new ExitOrNextNode(vocabularies);
        HelpNode help = new HelpNode(vocabularies);
        StatisticNode statistic = new StatisticNode(vocabularies);

        startFirstStr.initLinks(start);
        startSecondStr.initLinks(start);
        firstEnWordStr.initLinks(checkWord);
        toTryTo.initLinks(yesNo);
        secondEnWordStr.initLinks(checkWord);
        ruWordStr.initLinks(exitOrNext);
        wrongTopicStr.initLinks(start);
        hintStr.initLinks(yesNo);
        exitStr.initLinks(zero);
        statisticStr.initLinks(statistic);
        helpStr.initLinks(help);
        topicStr.initLinks(start);

        zero.initLinks(getLinks(
                Arrays.asList(Event.FIRST_START, Event.SECOND_START, Event.HELP),
                Arrays.asList(startFirstStr, startSecondStr, helpStr)));
        start.initLinks(getLinks(
                Arrays.asList(Event.FIRST_EN_WORD, Event.WRONG_TOPIC,
                        Event.EXIT, Event.HELP, Event.STATISTIC),
                Arrays.asList(firstEnWordStr, wrongTopicStr, exitStr, helpStr, statisticStr)));
        checkWord.initLinks(getLinks(
                Arrays.asList(Event.FIRST_EN_WORD, Event.TRY, Event.HINT,
                        Event.EXIT, Event.HELP, Event.STATISTIC),
                Arrays.asList(firstEnWordStr, toTryTo, hintStr, exitStr, helpStr, statisticStr)));
        yesNo.initLinks(getLinks(
                Arrays.asList(Event.SECOND_EN_WORD, Event.RU_WORD,
                        Event.EXIT, Event.HELP, Event.STATISTIC),
                Arrays.asList(secondEnWordStr, ruWordStr, exitStr, helpStr, statisticStr)));
        exitOrNext.initLinks(getLinks(
                Arrays.asList(Event.FIRST_EN_WORD,
                        Event.EXIT, Event.HELP, Event.STATISTIC),
                Arrays.asList(firstEnWordStr, exitStr, helpStr, statisticStr)));
        help.initLinks(getLinks(
                Arrays.asList(Event.SECOND_EN_WORD, Event.SECOND_START,
                        Event.EXIT, Event.STATISTIC),
                Arrays.asList(secondEnWordStr, startSecondStr, exitStr, statisticStr)));
        statistic.initLinks(getLinks(
                Arrays.asList(Event.SECOND_EN_WORD, Event.EXIT, Event.HELP),
                Arrays.asList(secondEnWordStr,exitStr, helpStr)));

        return zero;
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
