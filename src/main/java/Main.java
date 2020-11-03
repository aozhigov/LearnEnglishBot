import bot.TelegramBot;
import org.telegram.telegrambots.ApiContextInitializer;

public class Main {
    public static void main(String[] args) throws Exception {
        SetEnvVar.setEnv();
        ApiContextInitializer.init();

        TelegramBot bot = new TelegramBot(System.getenv("TELEGRAM_BOT_USERNAME"),
                System.getenv("TELEGRAM_BOT_TOKEN"));

        bot.botConnect();
    }
}