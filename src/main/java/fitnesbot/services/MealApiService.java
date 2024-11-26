package fitnesbot.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import fitnesbot.bot.apiparser.JsonSimpleParser;
import fitnesbot.exeptions.apiErrors.ApiErrorHandler;
import fitnesbot.models.MealsInTake;
import org.json.JSONObject;
import org.json.JSONArray;

public class MealApiService {
    private final HttpClient client;
    private final String appId;
    private final String appKey;
    private final JsonSimpleParser parser = new JsonSimpleParser();
    private final String apiUrl = "https://api.edamam.com/api/nutrition-details";


    public MealApiService(String appId, String appKey) {
        this.client = HttpClient.newHttpClient();
        this.appId = appId;
        this.appKey = appKey;
    }

    public MealApiService(String appId, String appKey, HttpClient client) {
        this.appId = appId;
        this.appKey = appKey;
        this.client = client;
    }

    public MealsInTake analyzeRecipe(String title, String[] ingredients) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", title);
        requestBody.put("ingr", new JSONArray(ingredients));
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(
                apiUrl  + "?app_id="
                        + appId
                        + "&app_key=" + appKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String analyseMeals = new String(response.body().getBytes(StandardCharsets.UTF_8),
                        StandardCharsets.UTF_8);
                analyseMeals = analyseMeals.replace("µ", "u");
                return parser.parseToIntake(analyseMeals);
            } else {
                System.err.println(new ApiErrorHandler().errorHandler(response.statusCode()));
                return null;
            }

        } catch (Exception e) {
            System.err.println("Error with HttpResponse: " + e.getMessage());
        }
        return null;
    }
}
