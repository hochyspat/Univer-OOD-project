package fitnesbot.models;

public class UserSQL {
    public static final String CREATE_TABLE = """
                CREATE TABLE IF NOT EXISTS Users (
                    chat_id BIGINT PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    height INT NOT NULL,
                    weight INT NOT NULL,
                    age INT NOT NULL,
                    calories DOUBLE PRECISION,
                    water_goal_quantity DOUBLE PRECISION,
                    water_goal_units VARCHAR(10),
                    sleep_goal_quantity DOUBLE PRECISION,
                    deleted BOOLEAN DEFAULT FALSE
                );
            """;

    public static final String INSERT_USER = """
                INSERT INTO Users (chat_id, name, height, weight, age, calories,
                                   water_goal_quantity, water_goal_units, sleep_goal_quantity, deleted)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """;

    public static final String SELECT_USER_BY_ID = """
                SELECT * FROM Users WHERE chat_id = ? AND deleted = FALSE;
            """;

    public static final String DELETE_USER = """
                UPDATE Users SET deleted = TRUE WHERE chat_id = ?;
            """;

    public static final String UPDATE_USER = """
                UPDATE Users SET name = ?, height = ?, weight = ?, age = ?,
                                  calories = ?, water_goal_quantity = ?, water_goal_units = ?,
                                  sleep_goal_quantity = ?, deleted = ? WHERE chat_id = ?;
            """;

    public static final String EXISTS_BY_ID = """
                SELECT COUNT(*) FROM Users WHERE chat_id = ? AND deleted = FALSE;
            """;

    public static final String UPDATE_WATER_GOAL = """
                UPDATE Users
                SET water_goal_quantity = ?
                WHERE chat_id = ?;
            """;

    public static final String UPDATE_SLEEP_GOAL = """
                UPDATE Users
                SET sleep_goal_quantity = ?
                WHERE chat_id = ?;
            """;
}
