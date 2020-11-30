package automat.HandlerNodes;

import automat.HandlerNode;
import common.*;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AddVocabularyNode extends HandlerNode {
    private YandexTranslate translate;

    public AddVocabularyNode() throws IOException, ParseException {
        translate = new YandexTranslate(System.getenv("Y_API_O_AUTH"),
                System.getenv("Y_API_FOLDER_ID"));
    }

    @Override
    public MessageBot action(String query, User user) throws IOException, ParseException {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        CsvFile freqFile = new CsvFile(
                System.getProperty("user.dir")
                        + "/src/main/resources/frequenсy_English_words.csv",
                ",");

        ArrayList<String> findWords = freqFile.search(query);
        user.addVocabularies("myVoc" + user.getMyVocabularies().size(),
                translate.getTranslateWord(findWords));

        user.currIdx = 1;

        return move(Event.ADD_WORD_VOCABULARY).action(user.getNextWord());
    }

}
