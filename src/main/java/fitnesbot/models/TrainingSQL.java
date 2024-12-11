package fitnesbot.models;

public class TrainingSQL {
    public static final String CREATE_TRAININGS_TABLE = """
                CREATE TABLE IF NOT EXISTS Trainings (
                    id SERIAL PRIMARY KEY,
                    chat_id BIGINT NOT NULL,
                    training_date VARCHAR(50) NOT NULL,
                    name VARCHAR(100) NOT NULL,
                    training_time VARCHAR(50) NOT NULL,
                    calories_burned DOUBLE PRECISION NOT NULL,
                    FOREIGN KEY (chat_id) REFERENCES Users (chat_id),
                    UNIQUE (chat_id, name, training_date)
                );
            """;

    public static final String INSERT_TRAINING = """
                INSERT INTO Trainings (chat_id, training_date, name, training_time, calories_burned)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (chat_id,name,training_date) DO UPDATE SET
                    training_time = EXCLUDED.training_time,
                    calories_burned = EXCLUDED.calories_burned;
            """;

    public static final String SELECT_TRAININGS_BY_CHAT_ID = """
                SELECT * FROM Trainings WHERE chat_id = ?;
            """;

    public static final String SELECT_TRAININGS_BY_DATE = """
                SELECT * FROM Trainings WHERE chat_id = ? AND training_date = ?;
            """;

    public static final String DELETE_TRAINING = """
                DELETE FROM Trainings WHERE chat_id = ? AND training_date = ? AND name = ?;
            """;
}


