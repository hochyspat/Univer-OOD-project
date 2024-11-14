package fitnesbot.bot.apiparser;

import com.fasterxml.jackson.databind.ObjectMapper;
import fitnesbot.models.Meal;
import fitnesbot.models.Nutrient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.HashMap;
import java.util.Map;


public class JsonSimpleParser {
    public Meal parse(String analyseMeals) {
        JSONParser parser = new JSONParser();
        JSONObject mealJsonObject = null;
        try {
            mealJsonObject = (JSONObject) parser.parse(analyseMeals);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String mealType = mealJsonObject.get("mealType").toString();
        double totalWeight = (double) mealJsonObject.get("totalWeight");
        long calories = (long) mealJsonObject.get("calories");

        JSONObject nutrientsJsonObject = (JSONObject) mealJsonObject.get("totalNutrients");
        Map<String, Nutrient> nutrientMap = new HashMap<>();
        addNutrient("FAT", nutrientsJsonObject, nutrientMap);
        addNutrient("CHOCDF", nutrientsJsonObject, nutrientMap);
        addNutrient("PROCNT", nutrientsJsonObject, nutrientMap);

        return new Meal(mealType, totalWeight, calories, nutrientMap);
    }

    private JSONObject getNutrient(String nutrientName, JSONObject nutrientsJsonObject) {
        return (JSONObject) nutrientsJsonObject.get(nutrientName);
    }

    private void addNutrient(String nutrientName, JSONObject nutrientsJsonObject, Map<String, Nutrient> nutrientMap) {
        JSONObject nutrientJsonObject = getNutrient(nutrientName,nutrientsJsonObject);
        if (nutrientJsonObject != null) {
            String unitNutrient = (String) nutrientJsonObject.get("unit");
            double quantityNutrient = (double) nutrientJsonObject.get("quantity");
            String labelNutrient = (String) nutrientJsonObject.get("label");

            Nutrient nutrient = new Nutrient(unitNutrient, quantityNutrient, labelNutrient);
            nutrientMap.put(nutrientName, nutrient);
        }

    }
}
