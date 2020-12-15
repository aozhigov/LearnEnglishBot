package User;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import parser.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class UserRepository {
    private final JSONParser jsonParser;
    private final String pathDB;

    public UserRepository(String pathDB) {
        jsonParser = new JSONParser();
        this.pathDB = pathDB;
    }

    public User getUserById(String id) throws IOException, ParseException {
        JSONObject jsonObject = openFile();
        if (jsonObject.containsKey(id))
            return JsonParser.parseUser((JSONObject) jsonObject.get(id));
        return null;
    }

    public void saveUser(String id, User user) throws IOException, ParseException {
        JSONObject jsonObject = openFile();
        jsonObject.remove(id);
        jsonObject.put(id, user.getJson());
        saveFile(jsonObject);
    }

    public void dellUser(String id) throws IOException, ParseException {
        JSONObject jsonObject = openFile();
        jsonObject.remove(id);
        saveFile(jsonObject);
    }

    public HashMap<String, User> getAllUsers() throws IOException, ParseException {
        JSONObject jsonObject = openFile();
        HashMap<String, User> users = new HashMap<>();
        for (Object key : jsonObject.keySet())
            users.put((String) key,
                    JsonParser.parseUser((JSONObject) jsonObject.get(key)));

        return users;
    }

    public void saveAllUser(HashMap<String, User> users) throws IOException {
        JSONObject jsonObject = new JSONObject();
        for (String key : users.keySet()) {
            jsonObject.put(key, users.get(key).getJson());
        }

        saveFile(jsonObject);
    }

    private JSONObject openFile() throws IOException, ParseException {
        File file = checkFile();
        if (file.length() == 0)
            return new JSONObject();
        FileReader t = new FileReader(pathDB);
        return (JSONObject) jsonParser.parse(t);
    }

    private void saveFile(JSONObject jsonObject) throws IOException {
        File file = checkFile();
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(jsonObject.toJSONString());
        }
    }

    private File checkFile() throws IOException {
        File file = new File(pathDB);
        if (!file.exists())
            file.createNewFile();

        return file;
    }
}
