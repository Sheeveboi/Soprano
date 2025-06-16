package net.altosheeve.soprano.client.Networking;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.altosheeve.soprano.client.Networking.old.OtherRequest;
import net.altosheeve.soprano.client.Networking.Request;

import java.io.IOException;
import java.net.URISyntaxException;

public class Firebase {
    private static final String fbAuthEndpoint = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyCnd_P98zXywWTBH789f3ajCoMMepYd6T4";
    private static final String fbCreateEndpoint = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyCnd_P98zXywWTBH789f3ajCoMMepYd6T4";
    private static final String fbDbEndpoint = "https://magic-lantern-f7b86-default-rtdb.firebaseio.com/";
    private static String idToken;

    private static void runAuthSession(String email, String password, OtherRequest cb) throws URISyntaxException, IOException {
        String body = """
                {
                    "email" : %s,
                    "password" : %s,
                    "returnSecureToken" : true
                }""".formatted(email, password);
        String headers = """
                {
                    "Content-Type" : "application/json; charset=UTF-8"
                }
                """;
        Request.post(fbAuthEndpoint, headers, body, ((headers1, body1) -> {
            JsonParser parser = new JsonParser();
            JsonObject d = parser.parse(body1).getAsJsonObject();
            if (d.has("idToken")) idToken = d.get("idToken").getAsString();
            cb.exec();
        }));
    }
}
