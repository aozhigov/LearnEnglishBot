package bot;

import common.Message;
import automat.MainLogic;
import org.json.simple.parser.ParseException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;


public class TelegramBot extends TelegramLongPollingBot {
    String token;
    String botName;
    MainLogic mainLogic;

    public TelegramBot(String botName, String token) throws IOException, ParseException {
        this.botName = botName;
        this.token = token;
        this.mainLogic = new MainLogic();
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();
        String userName = update.getMessage().getFrom().getUserName();
        Message answer = mainLogic.operate(chatId, inputText, userName);
        SendMessage msg = WorkWithMessage.createMsgWithKeyboard(answer);
        sendMsg(chatId, msg);
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

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}