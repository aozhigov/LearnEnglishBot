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
        Event event = checkCommand(query, user);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        CsvFile freqFile = new CsvFile(
                System.getProperty("user.dir")
                        + "/src/main/resources/frequency_words.csv",
                ",");

        ArrayList<String> findWords = freqFile.search(query);
        user.addVocabularies("myVoc" + user.getMyVocabularies().size(),
                translate.getTranslateWord(findWords));

        return move(Event.ADD_WORD_VOCABULARY).action(user.getNextWord(true));
    }
}