package user;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import parser.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class UserRepository {
    private final JSONParser jsonParser;
    private final String pathDB;
    private final String extension;

    public UserRepository(String pathDB, String extension) {
        jsonParser = new JSONParser();
        this.pathDB = pathDB;
        this.extension = extension;
    }

    public User getUserById(String id) throws IOException, ParseException {
        JSONObject jsonObject = readDBFileJson(id);
        if (jsonObject != null)
            return JsonParser.parseUser((JSONObject) jsonObject.get(id));
        return null;
    }

    public void saveUser(String id, User user) throws IOException, ParseException {
        JSONObject jsonObject = readDBFileJson(id);
        if (jsonObject == null)
            jsonObject = new JSONObject();
        jsonObject.put(id, user.getJson());
        saveJsonToDB(id, jsonObject);
    }

    public void dellUser(String id) {
        delFile(id);
    }

    private JSONObject readDBFileJson(String id) throws IOException, ParseException {
        File file = getOrCreateIfNone(id);
        if (file.length() == 0)
            return null;
        return (JSONObject) jsonParser.parse(new FileReader(file));
    }

    private void saveJsonToDB(String id, JSONObject jsonObject) throws IOException {
        delFile(id);
        File file = getOrCreateIfNone(id);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(jsonObject.toJSONString());
        }
    }

    private File getOrCreateIfNone(String id) throws IOException {
        File file = new File(pathDB + id + extension);
        if (!file.exists())
            file.createNewFile();

        return file;
    }

    private void delFile(String id){
        File file = new File(pathDB + id + extension);
        if (file.exists())
            file.delete();
    }
}
