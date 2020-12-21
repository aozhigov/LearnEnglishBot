package parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import user.User;
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
            vocabularies.put(name, new Gson().fromJson((JsonElement) obj.get(name), Selection.class));
        }
        return vocabularies;
    }

    public static User parseGsonUser(String json){
        return new Gson().fromJson(json, User.class);
    }

    public String fromUserToJson(User user){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(user);
    }
}