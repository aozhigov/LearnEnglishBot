package common;

import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import common.User;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;


public class UserRepository {
    private CloseableHttpClient httpclient;
    private final String token;
    private final JSONParser jsonParser;

    public UserRepository(String oAuth){
        openHttp();
        jsonParser = new JSONParser();
        token = oAuth;
    }

    public User getUserById(String id){

    }

    public void saveUser(String id, User user){

    }



    private HttpPost getHttpPostAuthorize(String uri)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json");

        //httpPost.setHeader("Authorization", "Bearer " + this.token);

        return httpPost;
    }

    private JSONObject getAnswerJson(HttpPost httpPost)
            throws IOException, ParseException {

        InputStream is = httpclient.execute(httpPost).getEntity().getContent();
        Reader reader = new InputStreamReader(is);
        return (JSONObject) jsonParser.parse(reader);
    }

    private void openHttp() {
        httpclient = HttpClients.createDefault();
    }

    private void closeHttp() throws IOException {
        httpclient.close();
    }

    private HttpPatch httpPatch(String uri){
        HttpPatch httpPatch = new HttpPatch(uri);
        httpPatch.setHeader("Content-Type", "application/json");
        return httpPatch;
    }

    private void updateField(String nameField, String valueJson){

    }
}
