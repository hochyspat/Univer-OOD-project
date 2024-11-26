package fitnesbot.bot.apiparser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitnesbot.models.MealsInTake;

import java.io.IOException;


public class JsonSimpleParser {
    public MealsInTake parseToIntake(String analyseMeals) {
        ObjectMapper objectMapper = new ObjectMapper().configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //вывод для себя
            /*for (Map.Entry<String, Nutrient> entry : meal.totalNutrients().entrySet()) {
                String nutrientName = entry.getKey();
                Nutrient nutrient = entry.getValue();
                System.out.println(nutrientName + ": " +
                 nutrient.getQuantity() + " " + nutrient.getUnit());
            }*/
        try {
            return objectMapper.readValue(analyseMeals, MealsInTake.class);
        } catch (IOException e) {
            System.out.println("Error during parsing" + e.getMessage());
            return null;
        }

    }


}
