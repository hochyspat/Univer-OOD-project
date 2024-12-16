package fitnesbot.models.sql;

public class MealSQL {
    public static final String CREATE_MEALINTAKES_TABLE = """
                CREATE TABLE IF NOT EXISTS Meals_intakes (
                    id SERIAL PRIMARY KEY,
                    chat_id BIGINT NOT NULL,
                    meal_date VARCHAR(50) NOT NULL,
                    meal_type VARCHAR(50) NOT NULL,
                    ingredients TEXT NOT NULL,
                    FOREIGN KEY (chat_id) REFERENCES Users (chat_id),
                    UNIQUE (chat_id, meal_date, meal_type)
                );
            """;
    public static final String INSERT_MEAL = """
                INSERT INTO Meals_intakes (chat_id, meal_date, meal_type, ingredients)
                VALUES (?, ?, ?, ?);
            """;
    public static final String INSERT_OR_UPDATE_MEALINTAKE = """
                INSERT INTO Meals_intakes (chat_id, meal_date, meal_type, ingredients)
                VALUES (?, ?, ?, ?)
                ON CONFLICT (chat_id, meal_date, meal_type)
                DO UPDATE SET ingredients = EXCLUDED.ingredients;
            """;

    public static final String SELECT_MEALS_BY_DATE = """
                SELECT * FROM Meals_intakes
                WHERE chat_id = ? AND meal_date = ? AND meal_type = ?;
            """;

    public static final String DELETE_MEAL = """
                DELETE FROM Meals_intakes
                WHERE id = ? AND chat_id = ?;
            """;


    public static final String SELECT_MEALS_BY_USER = """
                SELECT * FROM Meals_intakes
                WHERE chat_id = ?;
            """;
}
