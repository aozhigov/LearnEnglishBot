package automat.HandlerNodes;

import automat.HandlerNode;
import common.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class AddVocabularyNode extends HandlerNode {
    private final YandexTranslate translate;

    public AddVocabularyNode() throws IOException, ParseException {
        translate = new YandexTranslate(System.getenv("Y_API_O_AUTH"),
                System.getenv("Y_API_FOLDER_ID"));
    }

    @Override
    public MessageBot action(String query, User user) throws IOException, ParseException {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        CsvFile freqFile = new CsvFile(
                System.getProperty("user.dir")
                        + "/src/main/resources/frequency_words.csv",
                ",");

        ArrayList<String> findWords = freqFile.search(query);
        if (findWords.size() > 0){
            user.addVocabulary("myVoc" + user.getUserVocabularies().size(),
                    translate.getTranslateWord(findWords));
            return move(Event.ADD_WORD_VOCABULARY).action(user.getNextWordForAdd(true));
        }

        return move(Event.WRONG).action(user.getName());
    }
}
