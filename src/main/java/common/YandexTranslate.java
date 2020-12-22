package common;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import vocabulary.Word;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static parser.JsonParser.getTranslateStringsInYandex;
import static parser.JsonParser.getYandexToken;

public class YandexTranslate {
    private final String folderId;
    private final String token;
    private final HashMap<String, String> wordsLanguage;
    private CloseableHttpClient httpclient;

    public YandexTranslate(String oAuth, String folderId)
            throws IOException, ParseException {
        openHttp();
        wordsLanguage = new HashMap<>();
        this.folderId = folderId;
        token = getToken(oAuth);
        this.wordsLanguage.put("en", "ru");
        this.wordsLanguage.put("ru", "en");
    }

    private void openHttp() {
        httpclient = HttpClients.createDefault();
    }

    private String getToken(String oAuth)
            throws IOException, ParseException {
        HttpPost httpPost = getHttpPost(
                "https://iam.api.cloud.yandex.net/iam/v1/tokens",
                new JSONObject() {{
                    put("yandexPassportOauthToken", oAuth);
                }}
                        .toJSONString(),
                false);

        return getYandexToken(getAnswerJson(httpPost));
    }


    public ArrayList<Word> getWord(List<String> words) throws IOException, ParseException {
        String translateLanguage = wordsLanguage.get("en");//this.autoDetectLanguage(words.get(0));

        HttpPost httpPost = getHttpPost(
                "https://translate.api.cloud.yandex.net/translate/v2/translate",
                new JSONObject() {{
                    put("folder_id", folderId);
                    put("texts", new JSONArray() {{
                        addAll(words);
                    }});
                    put("targetLanguageCode", translateLanguage);
                }}
                        .toJSONString(),
                true);

        ArrayList<String> translatedWords = getTranslateStringsInYandex(
                getAnswerJson(httpPost));

        return this.prepareResponse(words,
                translatedWords,
                wordsLanguage.get(translateLanguage));
    }

    private ArrayList<Word> prepareResponse(List<String> wordsOnTranslate,
                                            ArrayList<String> words,
                                            String wordsLanguage) {
        ArrayList<Word> arr = new ArrayList<>();

        for (int counter = 0; counter < wordsOnTranslate.size(); counter++)
            if (wordsLanguage.equals("en"))
                arr.add(new Word(
                        wordsOnTranslate.get(counter),
                        words.get(counter)));

            else
                arr.add(new Word(
                        words.get(counter),
                        wordsOnTranslate.get(counter)));
        return arr;
    }

    private HttpPost getHttpPost(String uri,
                                 String content,
                                 boolean withAuthorization)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");

        if (withAuthorization)
            httpPost.setHeader("Authorization", "Bearer " + this.token);

        StringEntity stringEntity = new StringEntity(content);
        httpPost.setEntity(stringEntity);

        return httpPost;
    }

    private String getAnswerJson(HttpPost httpPost)
            throws IOException {

        InputStream is = httpclient.execute(httpPost).getEntity().getContent();

        Scanner sc = new Scanner(is);
        StringBuilder json = new StringBuilder();
        while (sc.hasNext())
            json.append(sc.next());

        return json.toString();
    }
}
