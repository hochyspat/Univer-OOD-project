import com.fasterxml.jackson.core.JsonProcessingException;
import fitnesbot.bot.apiparser.JsonSimpleParser;
import fitnesbot.models.MealsInTake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonSimpleParserTest {
    private JsonSimpleParser parser;

    @BeforeEach
    void setUp() {
        parser = new JsonSimpleParser();
    }

    @Test
    public void parseToIntake_valid_calories_totalWeight() throws JsonProcessingException {
        String json = "{\"calories\": 200, \"totalWeight\": 300, \"ingredients\": []}";

        MealsInTake mealsInTake = parser.parseToIntake(json);

        assertNotNull(mealsInTake);
        assertEquals(200, mealsInTake.getCalories());
        assertEquals(300, mealsInTake.getTotalWeight());
        assertNotNull(mealsInTake.getMeals());
    }

    @Test
    public void parseToIntake_valid_returnsMealsIntake() throws JsonProcessingException {
        String test = "{\n" +
                "  \"calories\": 488,\n" +
                "  \"totalWeight\": 543.2655384812423,\n" +
                "  \"ingredients\": [\n" +
                "    {\n" +
                "      \"parsed\": [{\n" +
                "        \n" +
                "          \"quantity\": 100,\n" +
                "          \"measure\": \"gram\",\n" +
                "          \"retainedWeight\": 100,\n" +
                "          \"foodId\": \"food_bpumdjzb5rtqaeabb0kbgbcgr4t9\",\n" +
                "          \"weight\": 100,\n" +
                "          \"foodMatch\": \"rice\",\n" +
                "          \"food\": \"rice\",\n" +
                "          \"measureURI\": " +
                "\"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
                "          \"nutrients\": {\n" +
                "            \"VITB6A\": {\n" +
                "              \"unit\": \"mg\",\n" +
                "              \"quantity\": 0.145,\n" +
                "              \"label\": \"Vitamin B-6\"\n" +
                "            },\n" +
                "            \"FE\": {\n" +
                "              \"unit\": \"g\",\n" +
                "              \"quantity\": 0.8,\n" +
                "              \"label\": \"Iron, Fe\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"status\": \"OK\"\n" +
                "        }],\n" +
                "      \n" +
                "      \"text\": \"100 gram rice\"\n" +
                "    },\n" +
                "    \n" +
                "    {\n" +
                "       \"parsed\" :  [\n" +
                "        {\n" +
                "          \"quantity\": 200,\n" +
                "          \"measure\": \"gram\",\n" +
                "          \"retainedWeight\": 200,\n" +
                "          \"foodId\": \"food_bpumdjzb5rtqaeabb0kbgbcgr4t10\",\n" +
                "          \"weight\": 200,\n" +
                "          \"foodMatch\": \"chicken\",\n" +
                "          \"food\": \"chicken\",\n" +
                "          \"measureURI\": " +
                "\"http://www.edamam.com/ontologies/edamam.owl#Measure_gram\",\n" +
                "          \"nutrients\": {\n" +
                "            \"VITB6A\": {\n" +
                "              \"unit\": \"mg\",\n" +
                "              \"quantity\": 0.200,\n" +
                "              \"label\": \"Vitamin B-6\"\n" +
                "            },\n" +
                "            \"FE\": {\n" +
                "              \"unit\": \"mg\",\n" +
                "              \"quantity\": 1.2,\n" +
                "              \"label\": \"Iron, Fe\"\n" +
                "            }\n" +
                "          },\n" +
                "          \"status\": \"OK\"\n" +
                "        }\n" +
                "       ],\n" +
                "       \"text\" : \"200 gram chicken\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        String expected = "{\n" +
                "  \"totalWeight\" : 543.2655384812423,\n" +
                "  \"calories\" : 488.0,\n" +
                "  \"ingredients\" : [ {\n" +
                "    \"parsed\" : [ {\n" +
                "      \"foodMatch\" : \"rice\",\n" +
                "      \"weight\" : 100.0,\n" +
                "      \"nutrients\" : {\n" +
                "        \"VITB6A\" : {\n" +
                "          \"unit\" : \"MG\",\n" +
                "          \"quantity\" : 0.145,\n" +
                "          \"label\" : \"Vitamin B-6\"\n" +
                "        },\n" +
                "        \"FE\" : {\n" +
                "          \"unit\" : \"GRAM\",\n" +
                "          \"quantity\" : 0.8,\n" +
                "          \"label\" : \"Iron, Fe\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ],\n" +
                "    \"text\" : \"100 gram rice\"\n" +
                "  }, {\n" +
                "    \"parsed\" : [ {\n" +
                "      \"foodMatch\" : \"chicken\",\n" +
                "      \"weight\" : 200.0,\n" +
                "      \"nutrients\" : {\n" +
                "        \"VITB6A\" : {\n" +
                "          \"unit\" : \"MG\",\n" +
                "          \"quantity\" : 0.2,\n" +
                "          \"label\" : \"Vitamin B-6\"\n" +
                "        },\n" +
                "        \"FE\" : {\n" +
                "          \"unit\" : \"MG\",\n" +
                "          \"quantity\" : 1.2,\n" +
                "          \"label\" : \"Iron, Fe\"\n" +
                "        }\n" +
                "      }\n" +
                "    } ],\n" +
                "    \"text\" : \"200 gram chicken\"\n" +
                "  } ]\n" +
                "}";
        MealsInTake mealsInTake = parser.parseToIntake(test);
        assertNotNull(mealsInTake);
        assertNotNull(mealsInTake.getMeals());
        String actual = mealsInTake.toString().replace("\r\n", "\n");
        expected = expected.replace("\r\n", "\n");
        assertEquals(expected, actual);

    }

    @Test
    void parseToIntake_invalid_returnsNull() throws JsonProcessingException {
        String json = "Some wrong file";
        MealsInTake mealsInTake = parser.parseToIntake(json);
        assertNull(mealsInTake);
    }


}
