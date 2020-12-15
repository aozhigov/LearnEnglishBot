package automat;


import User.User;
import User.UserRepository;
import automat.HandlerNodes.*;
import common.Event;
import common.MessageBot;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

import static parser.JsonParser.getVocabulariesFromJson;

public class MainLogic {
    private final HandlerNode root;
    private final Hashtable<String, User> users;
    private final UserRepository db;

    public MainLogic() throws IOException, ParseException {
        users = new Hashtable<>();
        db = new UserRepository(System.getProperty("user.dir")
                + "/src/main/resources/users.json");
        root = initializationAutomate();
    }

    public MessageBot operate(String chatId,
                              String query,
                              String userName) throws IOException, ParseException {
        if (!users.containsKey(chatId)) {
            User user = db.getUserById(chatId);
            if (user == null) {
                users.put(chatId, new User(userName,
                        chatId,
                        getVocabulariesFromJson(
                                System.getProperty("user.dir")
                                        + "/src/main/resources/dictionaries.json")));
                db.saveUser(chatId, users.get(chatId));
            } else
                users.put(chatId, user);
        }

        User user = users.get(chatId);

        query = query.trim().toLowerCase();

        if (user.getStateDialog().getValue() == null
                && (user.getStateDialog().getKey() == Event.FIRST_START
                || user.getStateDialog().getKey() == Event.SECOND_START))
            user.setStateDialog(root);

        MessageBot answer = user.getStateDialog().getValue().action(query, user);
        user.setStateDialog(answer.getHandler());

        db.saveUser(chatId, users.get(chatId));
        return answer.getMessageWithoutHandler();
    }

    private HandlerNode initializationAutomate() throws IOException, ParseException {
        List<String> yesNoKeyboard = Arrays.asList("Да", "Нет");
        List<String> hintEndKeyboard = Arrays.asList("Подсказка", "Закончить");
        List<String> hintNotKnowKeyboard = Arrays.asList("Подсказка", "Еще попытка", "Не знаю");
        List<String> addWordKeyboard = Arrays.asList("Знаю", "Не уверен",
                "Редактировать перевод", "Закончить");
        List<String> setNameVocabularyKeyboard = Collections.singletonList("Удалить");

        PrintNode startFirstStr = new PrintNode("Привет, {{WORD}}!\n" +
                "Я бот, который поможет тебе выучить английские слова!\n" +
                "Тебе надо будет писать перевод слов, которые я тебе отправлю!\n" +
                "В любой момент ты можешь попросить помощи по командам (/help).\n" +
                "Выбери одну из тем, предложенных ниже:",
                new ArrayList<>(0));
        PrintNode startSecondStr = new PrintNode("С возвращением, {{WORD}}!\n" +
                "В любой момент ты можешь попросить помощи по командам (/help).\n" +
                "Выбери одну из тем, предложенных ниже:",
                new ArrayList<>(0));
        PrintNode firstEnWordStr = new PrintNode("Отлично! Вот слово для перевода: ```{{WORD}}```",
                hintEndKeyboard);
        PrintNode toTryTo = new PrintNode("Неправильно, {{WORD}}! Попробуем еще?",
                hintNotKnowKeyboard);
        PrintNode secondEnWordStr = new PrintNode("Хорошо подумай и отвечай, слово: ```{{WORD}}```",
                hintEndKeyboard);
        PrintNode ruWordStr = new PrintNode("Не расстраивайся, вот перевод: ```{{WORD}}```.\n" +
                "Хочешь продолжить?",
                yesNoKeyboard);
        PrintNode wrongTopicStr = new PrintNode("{{WORD}}, не правильно выбрана тема.\n" +
                "Есть только такие темы",
                new ArrayList<>(0));
        PrintNode hintStr = new PrintNode("Держи подсказку: ```{{WORD}}```, а теперь отвечай");
        PrintNode exitStr = new PrintNode("Пока, {{WORD}}!");
        PrintNode statisticStr = new PrintNode("{{WORD}}, здесь ты можешь получить свою статистику.\n" +
                "Выбери какую:",
                new ArrayList<>(0));
        PrintNode statStr = new PrintNode("Вот твоя статистика:\n{{WORD}}\n" +
                "Продолжим?",
                yesNoKeyboard);
        PrintNode helpStr = new PrintNode("Бот, который поможет увеличить твой словарный запас.\n" +
                "Следующие команды могут быть вызваны из любого места диалога:\n" +
                "/exit - выход из бота (пауза)\n" +
                "/topic - сменить тему слов\n" +
                "/help - вызвать справку\n" +
                "/stat - показать статистику\n" +
                "/add ``` количество ``` - создать свой словарь из текста " +
                "с указанием количества слов (необязательно)\n" +
                "Перевод необходимо напечатать самому, \n" +
                "в остальном бот предлагает клавиатуру вариантов ответа.\n" +
                "Продолжим?",
                yesNoKeyboard);
        PrintNode topicStr = new PrintNode("{{WORD}}, выбери одну из тем, предложенных ниже:",
                new ArrayList<>(0));
        PrintNode wrongStr = new PrintNode("Не понимаю тебя, {{WORD}}\n" +
                "Продолжим?",
                yesNoKeyboard);
        PrintNode addVocabularyStr = new PrintNode("{{WORD}}, введи текст для " +
                "выделения нечасто употребляемых слов",
                new ArrayList<>(0));
        PrintNode addWordVocabularyStr = new PrintNode("Вот следующее слово:\n```{{WORD}}```.\n" +
                "Знаешь его?", addWordKeyboard);
        PrintNode endAddVocabularyStr = new PrintNode("{{WORD}}, теперь введи имя своего словаря",
                setNameVocabularyKeyboard);
        PrintNode editTranslateStr = new PrintNode("Введи свой перевод для ```{{WORD}}```",
                new ArrayList<>(0));

        ZeroNode zero = new ZeroNode();
        ChoseTopicNode choseTopic = new ChoseTopicNode();
        CheckWordNode checkWord = new CheckWordNode();
        YesNoNode yesNo = new YesNoNode();
        ExitOrNextNode exitOrNext = new ExitOrNextNode(db);
        WrongNode wrong = new WrongNode();
        StatisticNode statistic = new StatisticNode();
        AddVocabularyNode addVocabulary = new AddVocabularyNode();
        WordEditionVocabularyNode wordEditionVocabularyNode = new WordEditionVocabularyNode();
        SetNameVocabulariesNode setNameVocabulariesNode = new SetNameVocabulariesNode();
        TranslateEditorNode translateEditorNode = new TranslateEditorNode();

        addLinksInStr(choseTopic,
                Arrays.asList(startFirstStr, startSecondStr, wrongTopicStr, topicStr));
        addLinksInStr(checkWord,
                Arrays.asList(firstEnWordStr, secondEnWordStr, hintStr));
        addLinksInStr(wrong,
                Arrays.asList(statStr, helpStr, wrongStr));

        toTryTo.initLinks(yesNo);
        ruWordStr.initLinks(exitOrNext);
        exitStr.initLinks(zero);
        statisticStr.initLinks(statistic);
        addVocabularyStr.initLinks(addVocabulary);
        addWordVocabularyStr.initLinks(wordEditionVocabularyNode);
        endAddVocabularyStr.initLinks(setNameVocabulariesNode);
        editTranslateStr.initLinks(translateEditorNode);

        zero.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr);
            put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.CHANGE_TOPIC, topicStr);

            put(Event.FIRST_START, startFirstStr);
            put(Event.SECOND_START, startSecondStr);
            put(Event.ADD_VOCABULARY, addVocabularyStr);
        }});
        choseTopic.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr);
            put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.CHANGE_TOPIC, topicStr);

            put(Event.FIRST_START, startFirstStr);
            put(Event.WRONG_TOPIC, wrongTopicStr);
            put(Event.ADD_VOCABULARY, addVocabularyStr);
            put(Event.FIRST_EN_WORD, firstEnWordStr);
        }});
        checkWord.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr);
            put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.CHANGE_TOPIC, topicStr);

            put(Event.FIRST_START, startFirstStr);
            put(Event.TRY, toTryTo);
            put(Event.HINT, hintStr);
            put(Event.ADD_VOCABULARY, addVocabularyStr);
            put(Event.FIRST_EN_WORD, firstEnWordStr);
        }});
        yesNo.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr);
            put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.CHANGE_TOPIC, topicStr);

            put(Event.SECOND_EN_WORD, secondEnWordStr);
            put(Event.RU_WORD, ruWordStr);
            put(Event.HINT, hintStr);
            put(Event.ADD_VOCABULARY, addVocabularyStr);
        }});
        exitOrNext.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr);
            put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.CHANGE_TOPIC, topicStr);

            put(Event.FIRST_EN_WORD, firstEnWordStr);
            put(Event.ADD_VOCABULARY, addVocabularyStr);
        }});
        wrong.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr);
            put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.CHANGE_TOPIC, topicStr);

            put(Event.FIRST_EN_WORD, firstEnWordStr);
            put(Event.ADD_VOCABULARY, addVocabularyStr);
        }});
        statistic.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr);
            put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.CHANGE_TOPIC, topicStr);

            put(Event.WRONG, wrongStr);
            put(Event.STAT_STR, statStr);
            put(Event.ADD_VOCABULARY, addVocabularyStr);
        }});
        addVocabulary.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr);
            put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.WRONG, wrongStr);

            put(Event.ADD_WORD_VOCABULARY, addWordVocabularyStr);
        }});
        wordEditionVocabularyNode.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr);
            put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.WRONG, wrongStr);
            put(Event.CHANGE_TOPIC, topicStr);
            put(Event.FIRST_EN_WORD, firstEnWordStr);

            put(Event.EDIT_TRANSLATE, editTranslateStr);
            put(Event.ADD_WORD_VOCABULARY, addWordVocabularyStr);
            put(Event.END_VOCABULARY, endAddVocabularyStr);
        }});
        setNameVocabulariesNode.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.STATISTIC, statisticStr);
            put(Event.EXIT, exitStr);
            put(Event.HELP, helpStr);
            put(Event.CHANGE_TOPIC, topicStr);

            put(Event.FIRST_EN_WORD, firstEnWordStr);
        }});

        translateEditorNode.initLinks(new HashMap<Event, PrintNode>() {{
            put(Event.END_VOCABULARY, endAddVocabularyStr);
            put(Event.ADD_WORD_VOCABULARY, addWordVocabularyStr);
        }});

        return zero;
    }

    private void addLinksInStr(HandlerNode handler, List<PrintNode> printNodes) {
        for (PrintNode item : printNodes)
            item.initLinks(handler);
    }
}
