package apitests.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.response.Response;

public class GsonHelper {
    static GsonBuilder gsonBuilder = new GsonBuilder();
    static Gson gson = new Gson();

    public static String toJsonString(Object object) {
        return gsonBuilder.disableHtmlEscaping().setPrettyPrinting().create()
                .toJson(object).replaceAll("\n *", "").replaceAll("\": ", "\":");
    }

    public static <T> T jsonParse(Response response, Class<T> typeOfT) {
        return gson.fromJson(response.asString(), typeOfT);
    }

    public static <T> T jsonParse(String json, Class<T> typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

}
