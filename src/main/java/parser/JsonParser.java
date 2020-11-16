package parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vocabulary.Selection;
import vocabulary.Word;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class JsonParser {
    public static Hashtable<String, Selection> getVocabulariesFromJson() throws IOException, ParseException {
        Hashtable<String, Selection> vocabularies = new Hashtable<>();
        JSONParser parser = new JSONParser();

        String path = System.getProperty("user.dir") + "\\resources\\dictionaries.json";
        JSONObject obj = (JSONObject) parser.parse(new FileReader(path));

        for (String name : Arrays.asList("linq", "string", "io-api", "collection-api")) {
            ArrayList<Word> t = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) obj.get(name);

            for (Object item : jsonArray) {
                JSONObject temp = (JSONObject) item;
                t.add(new Word(Integer.parseInt(temp.get("frequency").toString()), temp.get("en").toString(),
                        temp.get("ru").toString(), temp.get("example").toString()));
            }

            vocabularies.put(name, new Selection(t));
        }

        return vocabularies;
    }
}

