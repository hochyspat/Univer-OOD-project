package fitnesbot.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class ParsedMeal {
    private List<Meal> parsedMeals;
    private String text;

    public ParsedMeal() {

    }

    public ParsedMeal(List<Meal> meal, String text) {
        this.parsedMeals = meal;
        this.text = text;
    }

    @JsonProperty("parsed")
    public List<Meal> getParsedMeals() {
        return parsedMeals;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMeal(List<Meal> meal) {
        this.parsedMeals = meal;
    }

}

