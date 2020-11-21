package bot;

import automat.MainLogic;
import common.MessageBot;
import common.Tuple;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import static bot.Keyboard.addSimpleKeyboard;
import static bot.Keyboard.addUnderMsgKeyboard;
import static bot.WorkWithSendMessage.*;


public class TelegramBot extends TelegramLongPollingBot {
    String token;
    String botName;
    MainLogic mainLogic;
    private final Hashtable<Long, Tuple<Boolean, Long>> users;

    public TelegramBot(String botName, String token) throws IOException, ParseException {
        this.botName = botName;
        this.token = token;
        this.mainLogic = new MainLogic();
        users = new Hashtable<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message msgTelegram;
        String inputText;
        boolean isCallback;

        if (update.hasCallbackQuery()) {
            msgTelegram = update.getCallbackQuery().getMessage();
            inputText = update.getCallbackQuery().getData();
            isCallback = true;
        }
        else {
            msgTelegram = update.getMessage();
            inputText = update.getMessage().getText();
            isCallback = false;
        }

        Long chatId = msgTelegram.getChatId();
        String userName = msgTelegram.getFrom().getUserName();

        if (!users.containsKey(chatId))
            users.put(chatId, new Tuple<>(isCallback, (long) msgTelegram.getMessageId()));
        else
            users.get(chatId).setTuple(isCallback, (long) msgTelegram.getMessageId());

        MessageBot answer = mainLogic.operate(chatId, inputText, userName);
        if (users.get(chatId).getKey())
            sendMsg(chatId, editMessage(users.get(chatId).getValue(), answer));
        else
            sendMsg(chatId, createMsgWithKeyboard(answer));
    }

    public synchronized void sendMsg(long chatId, SendMessage sendMessage) {
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMsg(long chatId, EditMessageText msg) {
        msg.enableMarkdown(true);
        msg.setChatId(chatId);
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void botConnect() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            try {
                Thread.sleep(10000);
                botConnect();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
    }

    private EditMessageText editMessage(long message_id, MessageBot msg){
        EditMessageText editMsg = new EditMessageText()
                .setMessageId(Math.toIntExact(message_id))
                .setText(msg.getText());
        Tuple<Integer, List<String>> keyboard = msg.getKeyboard();
        if (msg.getKeyboardType() == KeyboardType.UNDER_MESSAGE)
            editMsg.setReplyMarkup(addUnderMsgKeyboard(keyboard.getKey(), keyboard.getValue()));
        return editMsg;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}