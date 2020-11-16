package automat;


import automat.HandlerNode;
import automat.LearnNodes.*;
import automat.PrintNode;
import common.*;
import org.json.simple.parser.ParseException;
import vocabulary.Selection;

import java.io.IOException;
import java.util.*;

import static parser.JsonParser.getVocabulariesFromJson;

public class MainLogic {
    private final Hashtable<Long, User> users;
    private final Hashtable<String, Selection> vocabularies;
    private final HandlerNode root;
    private final ArrayList<Tuple<Integer, List<String>>> keyboards;

    public MainLogic() throws IOException, ParseException {
        users = new Hashtable<>();
        vocabularies = getVocabulariesFromJson();

        keyboards = new ArrayList<>();
        keyboards.add(new Tuple<>(2, Arrays.asList("linq", "string", "io-api", "collection-api")));
        keyboards.add(new Tuple<>(1, Arrays.asList("Да", "Нет")));

        root = initializationAutomat();
    }

    public Message operate(Long chatId, String query, String userName) {
        if (!users.containsKey(chatId))
            users.put(chatId, new User(userName, (long) (users.size() + 1)));

        User user = users.get(chatId);

        query = query.trim().toLowerCase();

        if (user.stateDialog.getValue() == null && user.stateDialog.getKey() == Event.FIRST_START)
            user.stateDialog.setValue(root);

        Message answer = user.stateDialog.getValue().action(query, user);
        user.stateDialog.setValue(answer.getHandler());

        return answer.getMessageWithoutHandler();
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
        PrintNode hintStr = new PrintNode("Вот подсказка: {{WORD}}, а теперь отвечай",
                keyboards.get(1));
        PrintNode exitStr = new PrintNode("Пока, {{WORD}}!",
                null);
        PrintNode statisticStr = new PrintNode("{{WORD}}, здесь ты можешь получить свою статистику, команды:\n" +
                "'по теме [тема]' - узнать процент знания текущщей [указанной] темы\n" +
                "'по слову [слово]' - узнать отношение успешных попыток к неуспешным текущего [указанного] слова",
                null);
        PrintNode statStr = new PrintNode("Вот твоя статистика: {{WORD}}\n" +
                "Продолжим?",
                keyboards.get(1));
        PrintNode helpStr = new PrintNode("Бот, который поможет увеличить твой словарный запас.\n" +
                "Следующие команды могут быть вызваны из любого места диалога:\n" +
                "/exit - выход из бота (пауза)\n" +
                "/topic - сменить тему слов\n" +
                "/help - вызвать справку\n" +
                "/stat - показать статистику\n" +
                "Перевод необходимо напечатать самому, \n" +
                "в остальном бот предлагает клавиатуру вариантов ответа.\n" +
                "Продолжим?",
                keyboards.get(1));
        PrintNode topicStr = new PrintNode("Выбери одну из тем, предложенных ниже:",
                keyboards.get(0));
        PrintNode wrongStr = new PrintNode("Не понимаю тебя, {{WORD}}\n" +
                "Продолжим?",
                keyboards.get(1));

        ZeroNode zero = new ZeroNode();
        ChoseTopicNode choseTopic = new ChoseTopicNode(vocabularies);
        CheckWordNode checkWord = new CheckWordNode(vocabularies);
        YesNoNode yesNo = new YesNoNode();
        ExitOrNextNode exitOrNext = new ExitOrNextNode(vocabularies);
        WrongNode wrong = new WrongNode();
        StatisticNode statistic = new StatisticNode(vocabularies);

        startFirstStr.initLinks(choseTopic);
        startSecondStr.initLinks(choseTopic);
        firstEnWordStr.initLinks(checkWord);
        toTryTo.initLinks(yesNo);
        secondEnWordStr.initLinks(checkWord);
        ruWordStr.initLinks(exitOrNext);
        wrongTopicStr.initLinks(choseTopic);
        hintStr.initLinks(checkWord);
        exitStr.initLinks(zero);
        statisticStr.initLinks(statistic);
        statStr.initLinks(wrong);
        helpStr.initLinks(wrong);
        topicStr.initLinks(choseTopic);
        wrongStr.initLinks(wrong);

        zero.initLinks(getLinks(
                Arrays.asList(Event.STATISTIC, Event.EXIT, Event.HELP,
                        Event.CHANGE_TOPIC, Event.FIRST_START, Event.SECOND_START),
                Arrays.asList(statisticStr, exitStr, helpStr, topicStr,
                        startFirstStr, startSecondStr)));
        choseTopic.initLinks(getLinks(
                Arrays.asList(Event.STATISTIC, Event.EXIT, Event.HELP,
                        Event.CHANGE_TOPIC, Event.FIRST_EN_WORD, Event.WRONG_TOPIC),
                Arrays.asList(statisticStr, exitStr, helpStr,
                        topicStr, firstEnWordStr, wrongTopicStr)));
        checkWord.initLinks(getLinks(
                Arrays.asList(Event.STATISTIC, Event.EXIT, Event.HELP,
                        Event.CHANGE_TOPIC, Event.FIRST_EN_WORD, Event.TRY, Event.HINT),
                Arrays.asList(statisticStr, exitStr, helpStr,
                        topicStr, firstEnWordStr, toTryTo, hintStr)));
        yesNo.initLinks(getLinks(
                Arrays.asList(Event.STATISTIC, Event.EXIT, Event.HELP,
                        Event.CHANGE_TOPIC, Event.SECOND_EN_WORD, Event.RU_WORD),
                Arrays.asList(statisticStr, exitStr, helpStr,
                        topicStr, secondEnWordStr, ruWordStr)));
        exitOrNext.initLinks(getLinks(
                Arrays.asList(Event.STATISTIC, Event.EXIT, Event.HELP,
                        Event.CHANGE_TOPIC, Event.FIRST_EN_WORD),
                Arrays.asList(statisticStr, exitStr, helpStr,
                        topicStr, firstEnWordStr)));
        wrong.initLinks(getLinks(
                Arrays.asList(Event.STATISTIC, Event.EXIT, Event.HELP,
                        Event.CHANGE_TOPIC, Event.FIRST_EN_WORD),
                Arrays.asList(statisticStr, exitStr, helpStr,
                        topicStr, firstEnWordStr)));
        statistic.initLinks(getLinks(
                Arrays.asList(Event.STATISTIC, Event.EXIT, Event.HELP,
                        Event.CHANGE_TOPIC, Event.WRONG, Event.STAT_STR),
                Arrays.asList(statisticStr, exitStr, helpStr,
                        topicStr, wrongStr, statStr)));

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
