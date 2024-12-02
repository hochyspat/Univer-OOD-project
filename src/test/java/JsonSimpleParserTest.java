import fitnesbot.bot.apiparser.JsonSimpleParser;
import fitnesbot.models.MealsInTake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JsonSimpleParserTest {
    private JsonSimpleParser parser;

    @BeforeEach
    void setUp() {
        parser = new JsonSimpleParser();
    }

    @Test
    public void parseToIntake_valid_calories_totalWeight() {
        String json = "{\"calories\": 200, \"totalWeight\": 300, \"ingredients\": []}";

        MealsInTake mealsInTake = parser.parseToIntake(json);

        assertNotNull(mealsInTake);
        assertEquals(200, mealsInTake.getCalories());
        assertEquals(300, mealsInTake.getTotalWeight());
        assertNotNull(mealsInTake.getMeals());
    }

    @Test
    public void parseToIntake_valid_returnsMealsIntake() {

        String test = """
                {
                  "calories": 488,
                  "totalWeight": 543.2655384812423,
                  "ingredients": [
                    {
                      "parsed": [{
                        "quantity": 100,
                        "measure": "gram",
                        "retainedWeight": 100,
                        "foodId": "food_bpumdjzb5rtqaeabb0kbgbcgr4t9",
                        "weight": 100,
                        "foodMatch": "rice",
                        "food": "rice",
                        "measureURI": "http://www.edamam.com/ontologies/edamam.owl#Measure_gram",
                        "nutrients": {
                          "VITB6A": {
                            "unit": "mg",
                            "quantity": 0.145,
                            "label": "Vitamin B-6"
                          },
                          "FE": {
                            "unit": "g",
                            "quantity": 0.8,
                            "label": "Iron, Fe"
                          }
                        },
                        "status": "OK"
                      }],
                      "text": "100 gram rice"
                    },
                    {
                      "parsed": [{
                        "quantity": 200,
                        "measure": "gram",
                        "retainedWeight": 200,
                        "foodId": "food_bpumdjzb5rtqaeabb0kbgbcgr4t10",
                        "weight": 200,
                        "foodMatch": "chicken",
                        "food": "chicken",
                        "measureURI": "http://www.edamam.com/ontologies/edamam.owl#Measure_gram",
                        "nutrients": {
                          "VITB6A": {
                            "unit": "mg",
                            "quantity": 0.200,
                            "label": "Vitamin B-6"
                          },
                          "FE": {
                            "unit": "mg",
                            "quantity": 1.2,
                            "label": "Iron, Fe"
                          }
                        },
                        "status": "OK"
                      }],
                      "text": "200 gram chicken"
                    }
                  ]
                }
                """;
        String expected = """
                {
                  "deleted" : false,
                  "totalWeight" : 543.2655384812423,
                  "calories" : 488.0,
                  "ingredients" : [ {
                    "parsed" : [ {
                      "foodMatch" : "rice",
                      "weight" : 100.0,
                      "nutrients" : {
                        "VITB6A" : {
                          "unit" : "MG",
                          "quantity" : 0.145,
                          "label" : "Vitamin B-6"
                        },
                        "FE" : {
                          "unit" : "GRAM",
                          "quantity" : 0.8,
                          "label" : "Iron, Fe"
                        }
                      }
                    } ],
                    "text" : "100 gram rice"
                  }, {
                    "parsed" : [ {
                      "foodMatch" : "chicken",
                      "weight" : 200.0,
                      "nutrients" : {
                        "VITB6A" : {
                          "unit" : "MG",
                          "quantity" : 0.2,
                          "label" : "Vitamin B-6"
                        },
                        "FE" : {
                          "unit" : "MG",
                          "quantity" : 1.2,
                          "label" : "Iron, Fe"
                        }
                      }
                    } ],
                    "text" : "200 gram chicken"
                  } ]
                }""";

        MealsInTake mealsInTake = parser.parseToIntake(test);
        assertNotNull(mealsInTake);
        assertNotNull(mealsInTake.getMeals());
        String actual = mealsInTake.toString().replace("\r\n", "\n");
        assertEquals(expected, actual);

    }

    @Test
    void parseToIntake_invalid_returnsNull() {
        String json = "Some wrong file";
        final MealsInTake[] mealsInTake = {null};
        assertDoesNotThrow(() -> {
            mealsInTake[0] = parser.parseToIntake(json);
        });
        assertNull(mealsInTake[0]);
    }


}
