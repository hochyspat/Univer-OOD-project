package fitnesbot.bot.apiparser;

import fitnesbot.models.Meal;
import fitnesbot.models.Nutrient;
import org.json.simple.JSONObject;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonSimpleParser {
    public Meal parse(String analyseMeals) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            //вывод для себя
            /*for (Map.Entry<String, Nutrient> entry : meal.totalNutrients().entrySet()) {
                String nutrientName = entry.getKey();
                Nutrient nutrient = entry.getValue();
                System.out.println(nutrientName + ": " + nutrient.getQuantity() + " " + nutrient.getUnit());
            }*/
            return objectMapper.readValue(analyseMeals, Meal.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
