package parser;

import common.Tuple;
import User.User;
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
import java.util.HashMap;

public class JsonParser {
    public static HashMap<String, Selection> getVocabulariesFromJson(String path)
            throws IOException, ParseException {
        HashMap<String, Selection> vocabularies = new HashMap<>();
        JSONParser parser = new JSONParser();

        JSONObject obj = (JSONObject) parser.parse(new FileReader(path));

        for (String name : Arrays.asList("linq", "string", "io-api", "collection-api")) {
            ArrayList<Word> t = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) obj.get(name);

            for (Object item : jsonArray) {
                JSONObject temp = (JSONObject) item;
                t.add(new Word(
                        temp.get("en").toString(),
                        temp.get("ru").toString()));
            }

            vocabularies.put(name, new Selection(t));
        }

        return vocabularies;
    }

    public static User parseUser(JSONObject jsonObject){
        String id = (String) jsonObject.get("id");
        String userName = (String) jsonObject.get("userName");
        JSONObject stateLearn = (JSONObject) jsonObject.get("stateLearn");
        String key = (String) stateLearn.keySet().toArray()[0];
        int value = Integer.parseInt((String) stateLearn.values().toArray()[0]);
        HashMap<String, Selection> myVocabularies = new HashMap<>();
        JSONObject vocabularies = (JSONObject) jsonObject.get("myVocabularies");
        for (Object item: vocabularies.keySet()){
            JSONObject selection = (JSONObject) vocabularies.get(item);
            myVocabularies.put((String) item, parseSelection(selection));
        }

        return new User(userName, id,
                myVocabularies,
                new Tuple<>(key, value));
    }

    public static Selection parseSelection(JSONObject jsonObject){
        ArrayList<Word> words = new ArrayList<>();
        JSONArray jsonWords = (JSONArray) jsonObject.get("words");
        for (Object jsonWord: jsonWords){
            Word word = parseWord((JSONObject) jsonWord);
            words.add(word);
        }
        return new Selection(words);
    }

    public static Word parseWord(JSONObject jsonObject){
        String ru = (String) jsonObject.get("ru");
        String en = (String) jsonObject.get("en");
        JSONObject statistic = (JSONObject) jsonObject.get("statistic");
        int key = Integer.parseInt((String) statistic.keySet().toArray()[0]);
        int value = Integer.parseInt((String) statistic.values().toArray()[0]);
        return new Word(en, ru, new Tuple<>(key, value));
    }
}