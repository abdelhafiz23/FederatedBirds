package fr.sio.ecp.federatedbirds;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import fr.sio.ecp.federatedbirds.auth.TokenManager;
import fr.sio.ecp.federatedbirds.model.Message;
import fr.sio.ecp.federatedbirds.model.User;

/**
 * Created by Michaël on 24/11/2015.
 */
public class ApiClient {

    private static final String API_BASE = "http://10.0.2.2:8080/";

    private static ApiClient mInstance;

    public static synchronized ApiClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiClient(context);
        }
        return mInstance;
    }

    private Context mContext;

    private ApiClient(Context context) {
        mContext = context.getApplicationContext();
    }

    private <T> T get(String path, Type type) throws IOException {
        return method("GET", path, null, type);
    }

    private <T> T post(String path, Object body, Type type) throws IOException {
        return method("POST", path, body, type);
    }

    private <T> T method(String method, String path, Object body, Type type) throws IOException {
        String url = API_BASE + path;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        String token = TokenManager.getUserToken(mContext);
        if (token != null) {
            connection.addRequestProperty("Authorization", "Bearer " + token);
        }
        if (body != null) {
            Writer writer = new OutputStreamWriter(connection.getOutputStream());
            try {
                new Gson().toJson(body, writer);
            } finally {
                writer.close();
            }
        }
        Reader reader = new InputStreamReader(connection.getInputStream());
        try {
            return new Gson().fromJson(reader, type);
        } finally {
            reader.close();
        }
    }

    public List<Message> getMessages(Long userId) throws IOException {
        TypeToken<List<Message>> type = new TypeToken<List<Message>>() {};
        String path = userId == null ? "messages" : "users/" + userId + "/messages";
        return get(path, type.getType());
    }

    public User getUser(long id) throws IOException {
        return get("users/" + id, User.class);
    }

    public List<User> getUserFollowed(Long userId) throws IOException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        return get("users", type.getType());
        //return get("users/" + id + "/followed", type.getType());
    }

    public List<User> getUserFollower(Long userId) throws IOException {
        String id = userId != null ? Long.toString(userId) : "me";
        TypeToken<List<User>> type = new TypeToken<List<User>>() {};
        return get("users/" + id + "/followers", type.getType());
    }


    public String login(String login, String password) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("password", password);
        return post("auth/token", body, String.class);
    }

    public Message postMessage(String text) throws IOException {
        Message message = new Message();
        message.text = text;
        return post("messages", message, Message.class);
    }
    public String signUp(String login, String email, String password) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("login", login);
        body.addProperty("email", email);
        body.addProperty("password", password);
        return post("users", body, String.class);
    }

}
