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
import java.util.List;

import static bot.Keyboard.addUnderMsgKeyboard;
import static bot.WorkWithSendMessage.*;


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
        Message msgTelegram;
        String inputText;

        if (update.hasCallbackQuery()) {
            msgTelegram = update.getCallbackQuery().getMessage();
            inputText = update.getCallbackQuery().getData();
        }
        else {
            msgTelegram = update.getMessage();
            inputText = update.getMessage().getText();
        }

        Long chatId = msgTelegram.getChatId();
        String userName = msgTelegram.getFrom().getUserName();

        MessageBot answer = mainLogic.operate(chatId, inputText, userName);
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