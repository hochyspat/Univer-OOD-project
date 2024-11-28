package fitnesbot.bot.apiparser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitnesbot.models.MealsInTake;

import java.io.IOException;


public class JsonSimpleParser {
    private final ObjectMapper objectMapper = new ObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public MealsInTake parseToIntake(String analyseMeals) {
        try {
            return objectMapper.readValue(analyseMeals, MealsInTake.class);
        } catch (IOException e) {
            System.out.println("Error during parsing" + e.getMessage());
            return null;
        }

    }


}
