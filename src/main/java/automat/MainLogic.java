package automat;


import automat.HandlerNodes.*;
import common.*;
import org.json.simple.parser.ParseException;
import vocabulary.Selection;

import java.io.IOException;
import java.util.*;

import static parser.JsonParser.getVocabulariesFromJson;

public class MainLogic {
    private final HashMap<String, Selection> startVocabularies;
    private final HandlerNode root;
    private final Hashtable<Long, User> users;

    public MainLogic() throws IOException, ParseException {
        users = new Hashtable<>();
        startVocabularies = getVocabulariesFromJson(
                System.getProperty("user.dir")
                        + "/src/main/resources/dictionaries.json");

        root = initializationAutomat();
    }

    public MessageBot operate(Long chatId,
                              String query,
                              String userName) throws IOException, ParseException {
        if (!users.containsKey(chatId))
            users.put(chatId, new User(userName, (long) (users.size() + 1)));

        User user = users.get(chatId);

        query = query.trim().toLowerCase();

        if (user.getStateDialog().getValue() == null
                && user.getStateDialog().getKey() == Event.FIRST_START)
            user.setStateDialog(root);

        MessageBot answer = user.getStateDialog().getValue().action(query, user);
        user.setStateDialog(answer.getHandler());

        return answer.getMessageWithoutHandler();
    }

    private HandlerNode initializationAutomat() throws IOException, ParseException {
        KeyboardBot vocabulariesNamesKeyboard = new KeyboardBot(2,
                Arrays.asList("linq", "string",
                        "io-api", "collection-api"));
        KeyboardBot yesNoKeyboard = new KeyboardBot(1,
                Arrays.asList("Да", "Нет"));
        KeyboardBot hintEndKeyboard = new KeyboardBot(2,
                Arrays.asList("Подсказка", "Закончить"));
        KeyboardBot hintNotKnowKeyboard = new KeyboardBot(3,
                Arrays.asList("Подсказка", "Еще попытка", "Не знаю"));
        KeyboardBot statKeyboard = new KeyboardBot(3,
                Arrays.asList("Текущая тема", "Слова",
                        "Тема: linq", "Тема: string",
                        "Тема: io-api", "Тема: collection-api"));
        KeyboardBot addWordKeyboard = new KeyboardBot(2,
                Arrays.asList("Знаю", "Не уверен", "Закончить"));

        PrintNode startFirstStr = new PrintNode("Привет, {{WORD}}!\n" +
                "Я бот, который поможет тебе выучить английские слова!\n" +
                "Тебе надо будет писать перевод слов, которые я тебе отправлю!\n" +
                "В любой момент ты можешь попросить помощи по командам (/help).\n" +
                "Выбери одну из тем, предложенных ниже:",
                vocabulariesNamesKeyboard);
        PrintNode startSecondStr = new PrintNode("С возвращением, {{WORD}}!\n" +
                "В любой момент ты можешь попросить помощи по командам (/help).\n" +
                "Выбери одну из тем, предложенных ниже:",
                vocabulariesNamesKeyboard);
        PrintNode firstEnWordStr = new PrintNode("Отлично! Вот слово для перевода: {{WORD}}",
                hintEndKeyboard);
        PrintNode toTryTo = new PrintNode("Неправильно, {{WORD}}!",
                hintNotKnowKeyboard);
        PrintNode secondEnWordStr = new PrintNode("Хорошо подумай и отвечай, слово: {{WORD}}",
                hintEndKeyboard);
        PrintNode ruWordStr = new PrintNode("Не расстраивайся, вот перевод: {{WORD}}.\n" +
                "Хочешь продолжить?",
                yesNoKeyboard);
        PrintNode wrongTopicStr = new PrintNode("{{WORD}}, не правильно выбрана тема.\n" +
                "Есть только такие темы",
                vocabulariesNamesKeyboard);
        PrintNode hintStr = new PrintNode("Держи подсказку: {{WORD}}, а теперь отвечай");
        PrintNode exitStr = new PrintNode("Пока, {{WORD}}!");
        PrintNode statisticStr = new PrintNode("{{WORD}}, здесь ты можешь получить свою статистику.\n" +
                "Выбери какую:",
                statKeyboard);
        PrintNode statStr = new PrintNode("Вот твоя статистика:\n{{WORD}}\n" +
                "Продолжим?",
                yesNoKeyboard);
        PrintNode helpStr = new PrintNode("Бот, который поможет увеличить твой словарный запас.\n" +
                "Следующие команды могут быть вызваны из любого места диалога:\n" +
                "/exit - выход из бота (пауза)\n" +
                "/topic - сменить тему слов\n" +
                "/help - вызвать справку\n" +
                "/stat - показать статистику\n" +
                "Перевод необходимо напечатать самому, \n" +
                "в остальном бот предлагает клавиатуру вариантов ответа.\n" +
                "Продолжим?",
                yesNoKeyboard);
        PrintNode topicStr = new PrintNode("Выбери одну из тем, предложенных ниже:",
                vocabulariesNamesKeyboard);
        PrintNode wrongStr = new PrintNode("Не понимаю тебя, {{WORD}}\n" +
                "Продолжим?",
                yesNoKeyboard);
        PrintNode addVocabularyStr = new PrintNode("Введи текст для " +
                "выделения нечасто употребляемых слов", null);
        PrintNode addWordVocabularyStr = new PrintNode("Вот следующее слово:{{WORD}}.\n" +
                "Знаешь его?", addWordKeyboard);
        PrintNode endAddVocabularyStr = new PrintNode("Теперь введи имя своего словаря",
                null);

        ZeroNode zero = new ZeroNode();
        ChoseTopicNode choseTopic = new ChoseTopicNode();
        CheckWordNode checkWord = new CheckWordNode();
        YesNoNode yesNo = new YesNoNode();
        ExitOrNextNode exitOrNext = new ExitOrNextNode();
        WrongNode wrong = new WrongNode();
        StatisticNode statistic = new StatisticNode();
        AddVocabularyNode addVocabulary = new AddVocabularyNode();
        IsAddTrueWord isAddTrueWord = new IsAddTrueWord();
        SetNameVocabularies setNameVocabularies = new SetNameVocabularies();

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
        addVocabularyStr.initLinks(addVocabulary);
        addWordVocabularyStr.initLinks(isAddTrueWord);
        endAddVocabularyStr.initLinks(setNameVocabularies);

        zero.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr); put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr); put(Event.CHANGE_TOPIC, topicStr);
            put(Event.FIRST_START, startFirstStr); put(Event.SECOND_START, startSecondStr);}});
        choseTopic.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr); put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr); put(Event.CHANGE_TOPIC, topicStr);
            put(Event.FIRST_START, startFirstStr); put(Event.WRONG_TOPIC, wrongTopicStr);}});
        checkWord.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr); put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr); put(Event.CHANGE_TOPIC, topicStr);
            put(Event.FIRST_START, startFirstStr); put(Event.TRY, toTryTo);
            put(Event.HINT, hintStr);}});
        yesNo.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr); put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr); put(Event.CHANGE_TOPIC, topicStr);
            put(Event.SECOND_EN_WORD, secondEnWordStr); put(Event.RU_WORD, ruWordStr);
            put(Event.HINT, hintStr);}});
        exitOrNext.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr); put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr); put(Event.CHANGE_TOPIC, topicStr);
            put(Event.FIRST_EN_WORD, firstEnWordStr); }});
        wrong.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr); put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr); put(Event.CHANGE_TOPIC, topicStr);
            put(Event.FIRST_EN_WORD, firstEnWordStr); }});
        statistic.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr); put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr); put(Event.CHANGE_TOPIC, topicStr);
            put(Event.WRONG, wrongStr); put(Event.STAT_STR, statStr);}});
        addVocabulary.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr); put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr); put(Event.ADD_WORD_VOCABULARY, addWordVocabularyStr); }});
        isAddTrueWord.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr); put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.END_VOCABULARY, endAddVocabularyStr); }});
        setNameVocabularies.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr); put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr); put(Event.CHANGE_TOPIC, topicStr);
            put(Event.FIRST_EN_WORD, firstEnWordStr); }});

        return zero;
    }
}
