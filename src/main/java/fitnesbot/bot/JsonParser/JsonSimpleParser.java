package fitnesbot.bot.JsonParser;

import fitnesbot.models.Meal;
import fitnesbot.models.Nutrient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.List;


public class JsonSimpleParser {

    public Meal parse(String analyseMeals) {
        JSONParser parser = new JSONParser();
        Meal meal = new Meal();

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
        List<Nutrient> nutrientList = new ArrayList<>();

        JSONObject fats = getNutrient("FAT", nutrientsJsonObject);
        addNutrient(fats, nutrientList);

        JSONObject carbohydrate = getNutrient("CHOCDF", nutrientsJsonObject);
        addNutrient(carbohydrate, nutrientList);

        JSONObject protein = getNutrient("PROCNT", nutrientsJsonObject);
        addNutrient(protein, nutrientList);

        meal.setCalories(calories);
        meal.setTotalWeight(totalWeight);
        meal.setMealType(mealType);
        meal.setTotalNutrients(nutrientList);

        return meal;
    }

    private JSONObject getNutrient(String nutrientName, JSONObject nutrientsJsonObject) {
        return (JSONObject) nutrientsJsonObject.get(nutrientName);
    }

    private void addNutrient(JSONObject nutrientJsonObject, List<Nutrient> nutrientList) {
        String unitNutrient = (String) nutrientJsonObject.get("unit");
        double quantityNutrient = (double) nutrientJsonObject.get("quantity");
        String labelNutrient = (String) nutrientJsonObject.get("label");

        Nutrient nutrient = new Nutrient(unitNutrient, quantityNutrient, labelNutrient);
        nutrientList.add(nutrient);
    }
}
