package automat.HandlerNodes;

import automat.HandlerNode;
import common.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AddVocabularyNode extends HandlerNode {
    private YandexTranslate translate;

    public AddVocabularyNode(){

    }

    @Override
    public MessageBot action(String query, User user) throws IOException {
        Event event = checkCommand(query);
        String word = user.getName();

        if (event != Event.NONE)
            return move(event).action(word);

        CsvFile freqFile = new CsvFile(
                System.getProperty("user.dir")
                        + "/src/main/resources/frequenсy_English_words.csv",
                ",");

        ArrayList<Tuple<String, Integer>> findWords = freqFile.search(query);

        return null;
    }

}
