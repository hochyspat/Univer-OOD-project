
import fitnesbot.models.MealsInTake;
import fitnesbot.services.MealsInTakeApiService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MealsInTakeApiServiceTest {


    private MealsInTakeApiService mealsIntakeApiService;

    private HttpClient httpClient;
    private HttpResponse<String> response;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse<String> mockHttpResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        httpClient = mockHttpClient;
        response = mockHttpResponse;
        mealsIntakeApiService = new MealsInTakeApiService("testAppId", "testAppKey", mockHttpClient);
    }

    @Test
    public void testMealApiService() throws IOException, InterruptedException {
        String testTitle = "Test Recipe";
        String[] testIngredients = {"100 g rice", "200 ml milk"};
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", testTitle);
        requestBody.put("ingr", new JSONArray(testIngredients));
        String mockResponseBody = """
                {
                  "calories": 500,
                  "totalWeight": 300,
                  "ingredients": [
                    {
                      "parsed": [
                        {
                          "quantity": 100,
                          "measure": "gram",
                          "food": "rice",
                          "calories": 300
                        }
                      ],
                      "text": "100g rice"
                    },
                    {
                      "parsed": [
                        {
                          "quantity": 200,
                          "measure": "ml",
                          "food": "milk",
                          "calories": 200
                        }
                      ],
                      "text": "200ml milk"
                    }
                  ]
                }
                """;
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);
        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(mockResponseBody);
        MealsInTake mealsInTake = mealsIntakeApiService.analyzeRecipe(testTitle, testIngredients);
        assertNotNull(mealsInTake);
        assertEquals(500, mealsInTake.getCalories());
        assertEquals(300, mealsInTake.getTotalWeight());
        assertEquals(2, mealsInTake.getMeals().size());
        assertNotNull(mealsInTake.getMeals());

    }


}
