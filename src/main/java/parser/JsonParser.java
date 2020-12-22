package parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.StringMap;
import user.User;
import vocabulary.Selection;
import vocabulary.Word;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static user.UserRepository.readJson;

public class JsonParser {
    public static HashMap<String, Selection> getVocabulariesFromJson(String path)
            throws IOException {
        String json = readJson(new File(path));
        HashMap<String, Selection> vocabularies = new HashMap<>();
        HashMap<String, ArrayList<StringMap>> tempMap = new HashMap<>();
        tempMap = new Gson().fromJson(json, tempMap.getClass());

        for (String key : tempMap.keySet()) {
            ArrayList<Word> words = new ArrayList<>();
            for (StringMap value : tempMap.get(key))
                words.add(new Word((String) value.get("en"),
                        (String) value.get("ru")));

            vocabularies.put(key, new Selection(words));
        }
        return vocabularies;
    }

    public static User fromJsonToUser(String json) {
        return new Gson().fromJson(json, User.class);
    }

    public static String fromUserToJson(User user) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(user);
    }

    public static ArrayList<String> getTranslateStringsInYandex(String json){
        ArrayList<String> temp = new ArrayList<>();
        HashMap<String, ArrayList<StringMap>> temp1 =
                new Gson().fromJson(json, HashMap.class);
        for(StringMap item: temp1.get("translations"))
            temp.add((String) item.get("text"));
        return temp;
    }

    public static String getYandexToken(String json){
        HashMap<String, String> temp1 = new HashMap<>();
        temp1 = new Gson().fromJson(json, temp1.getClass());
        return temp1.get("iamToken");
    }
}