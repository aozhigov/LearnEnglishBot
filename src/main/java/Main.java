import automat.MainLogic;
import bot.TelegramBot;
import common.YandexTranslate;
import org.telegram.telegrambots.ApiContextInitializer;
import user.UserRepository;

public class Main {
    public static void main(String[] args) throws Exception {
        SetEnvVar.setEnv();
        ApiContextInitializer.init();

        TelegramBot bot = new TelegramBot(System.getenv("TELEGRAM_BOT_USERNAME"),
                System.getenv("TELEGRAM_BOT_TOKEN"),
                new MainLogic(new UserRepository(System.getProperty("user.dir")
                        + "/src/main/resources/db/", ".json"),
                        System.getProperty("user.dir")
                                + "/src/main/resources/dictionaries.json",
                        System.getProperty("user.dir")
                                + "/src/main/resources/frequency_words.csv", ",",
                        new YandexTranslate(System.getenv("Y_API_O_AUTH"),
                                System.getenv("Y_API_FOLDER_ID"))));

        bot.botConnect();
    }
}