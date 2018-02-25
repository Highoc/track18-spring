package ru.track;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;


/**
 * TASK:
 * POST request to  https://guarded-mesa-31536.herokuapp.com/track
 * fields: name,github,email
 *
 * LIB: http://unirest.io/java.html
 *
 *
 */

public class App {

    public static final String URL = "http://guarded-mesa-31536.herokuapp.com/track";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_GITHUB = "github";
    public static final String FIELD_EMAIL = "email";

    public static void main(String[] args) throws Exception {

        HttpResponse<JsonNode> response = Unirest.post(URL)
                .field(FIELD_NAME, "Max")
                .field(FIELD_GITHUB, "Highoc")
                .field(FIELD_EMAIL, "door0172@gmail.com")
                .asJson();

        System.out.println(response.getBody().getObject().get("success"));
    }

}
