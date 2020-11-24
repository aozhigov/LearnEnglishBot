package bot;

import common.MessageBot;
import org.telegram.telegrambots.api.methods.send.SendMessage;

import static bot.Keyboard.addKeyboard;

public class WorkWithSendMessage {
    public static SendMessage createMessage(String text) {
        SendMessage msg = new SendMessage();
        msg.setText(text);
        return msg;
    }

    public static SendMessage createMsgWithKeyboard(MessageBot answer) {
        SendMessage msg = createMessage(answer.getText());
        return addKeyboard(msg, answer.getKeyboardType(), answer.getKeyboard());
    }
}
