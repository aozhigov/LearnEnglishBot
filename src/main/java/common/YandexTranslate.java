package common;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vocabulary.Selection;
import vocabulary.Word;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YandexTranslate {
    private final String folderId;
    private final String token;
    private final JSONParser jsonParser;
    private CloseableHttpClient httpclient;
    private HashMap<String, String> wordsLanguage = new HashMap<>();

    public YandexTranslate(String oAuth, String folderId) throws IOException, ParseException {
        this.folderId = folderId;
        openHttp();
        jsonParser = new JSONParser();
        token = getToken(oAuth);
        this.wordsLanguage.put("en", "ru");
        this.wordsLanguage.put("ru", "en");
    }

    private void openHttp() {
        httpclient = HttpClients.createDefault();
    }

    private void closeHttp() throws IOException {
        httpclient.close();
    }

    private String getToken(String oAuth) throws IOException, ParseException {
        HttpPost post = new HttpPost("https://iam.api.cloud.yandex.net/iam/v1/tokens");
        post.setHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity("{\"yandexPassportOauthToken\": \"" + oAuth + "\"}");
        post.setEntity(entity);
        InputStream is = httpclient.execute(post).getEntity().getContent();
        Reader reader = new InputStreamReader(is);
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

        return (String) jsonObject.get("iamToken");
    }


    public Selection getTranslateWord(List<String> words) throws IOException, ParseException {
        String preparedWords = this.prepareWords(words);
        String wordsLanguage = this.autoDetectLanguage(words.get(0));
        HttpPost httpPost = new HttpPost("https://translate.api.cloud.yandex.net/translate/v2/translate");
        httpPost.setHeader("Content-Type", "appliacation/json");
        httpPost.setHeader("Authorization", "Bearer " + this.token);
        StringEntity stringEntity = new StringEntity("{\"folder_id\": \"" + this.folderId + "\"," +
                "\"texts\": [" + preparedWords + "]," +
                "\"targetLanguageCode\": " + "\"" + this.wordsLanguage.get(wordsLanguage) + "\"}");
        httpPost.setEntity(stringEntity);
        InputStream is = httpclient.execute(httpPost).getEntity().getContent();
        Reader reader = new InputStreamReader(is);
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        JSONArray jsonArray = (JSONArray) jsonObject.get("translations");
        ArrayList<String> translatedWords = this.prepareTranslatedWords(jsonArray);
        return this.prepareResponse(words, translatedWords, wordsLanguage);
    }
    private ArrayList<String> prepareTranslatedWords(JSONArray jsonArray){
        ArrayList<String> result = new ArrayList<>();
        for (Object item: jsonArray){
            JSONObject temp = (JSONObject) item;
            result.add(temp.get("text").toString());
        }
        return result;
    }

    private Selection prepareResponse(List<String> wordsOnTranslate,
                                      ArrayList<String> words,
                                      String wordsLanguage){
        ArrayList<Word> arr = new ArrayList<>();
        for (int counter = 0; counter < wordsOnTranslate.size(); counter++){
            if (wordsLanguage.equals("en")){
                arr.add(new Word(0,
                        wordsOnTranslate.get(counter),
                        words.get(counter), ""));
            }
            else{
                arr.add(new Word(0,
                        words.get(counter),
                        wordsOnTranslate.get(counter), " "));
            }
        }
        return new Selection(arr);
    }

    private String prepareWords(List<String> words){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < words.size(); i++){
            result.append("\"" + words.get(i) + "\"");
            if (i != words.size() - 1){
                result.append(", ");
            }
        }
        return result.toString();
    }

    public String autoDetectLanguage(String word) throws IOException, ParseException {
        HttpPost httpPost = new HttpPost("https://translate.api.cloud.yandex.net/translate/v2/detect");
        httpPost.setHeader("Content-Type", "appliacation/json");
        httpPost.setHeader("Authorization", "Bearer " + this.token);
        StringEntity stringEntity = new StringEntity("{\"folder_id\": \"" + this.folderId + "\"," +
                " \"text\": \"" + word + "\"}");
        httpPost.setEntity(stringEntity);
        InputStream is = httpclient.execute(httpPost).getEntity().getContent();
        Reader reader = new InputStreamReader(is);
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        return (String) jsonObject.get("languageCode");
    }
}
