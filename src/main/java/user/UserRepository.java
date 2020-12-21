package user;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import parser.JsonParser;

import java.io.*;
import java.util.Scanner;


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
        User jsonObject = readDBFileJson(id);
        if (jsonObject != null)
            return jsonObject;
//            return JsonParser.parseUser(jsonObject);
        return null;
    }

    public void saveUser(String id, User user) throws IOException, ParseException {
//        JSONObject jsonObject = readDBFileJson(id);
//        if (jsonObject == null)
//            jsonObject = new JSONObject();
//        jsonObject.put(id, );
        saveJsonToDB(id, user.getJson());
    }

    public void dellUser(String id) {
        delFile(id);
    }

    private User readDBFileJson(String id) throws IOException, ParseException {
        File file = getOrCreateIfNone(id);
        if (file.length() == 0)
            return null;
        Reader t = new FileReader(file);
        Scanner sc = new Scanner(file);
        String r = "";
        while(sc.hasNext()){
            r += sc.next();
        }
        return JsonParser.parseGsonUser(r);
//        return (JSONObject) jsonParser.parse(t);
    }

    private void saveJsonToDB(String id, String jsonObject) throws IOException {
        delFile(id);
        File file = getOrCreateIfNone(id);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(jsonObject);
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
