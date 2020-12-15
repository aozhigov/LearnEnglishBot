package automat.HandlerNodes;

import user.User;
import automat.HandlerNode;
import common.*;
import org.json.simple.parser.ParseException;
import vocabulary.Selection;

import java.io.IOException;
import java.util.ArrayList;

public class AddVocabularyNode extends HandlerNode {
    private final YandexTranslate translate;
    private final String pathToCSV;
    private final String separator;

    public AddVocabularyNode(String pathToCSV,
                             String separator,
                             YandexTranslate translate) {
        this.translate = translate;
        this.pathToCSV = pathToCSV;
        this.separator = separator;
    }

    @Override
    public MessageBot action(String query, User user) throws IOException, ParseException {
        MessageBot msg = checkCommand(query, user);

        if (msg != null)
            return msg;

        CsvFile freqFile = new CsvFile(pathToCSV, separator);

        ArrayList<String> findWords = freqFile.search(query);
        if (findWords.size() > 0){
            user.addVocabulary("myVoc" + user.getUserVocabularies().size(),
                    new Selection(translate.getWord(findWords)));
            return move(Event.ADD_WORD_VOCABULARY).action(user.getNextWordForAdd(true));
        }

        return move(Event.WRONG).action(user.getName());
    }
}
