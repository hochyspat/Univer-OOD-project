package fitnesbot.models.sql;

public class WaterInTakeSql {
    public static final String CREATE_WATER_INTAKES_TABLE = """
            CREATE TABLE IF NOT EXISTS WaterIntake (
                chat_id BIGINT NOT NULL,
                water_date VARCHAR(50) NOT NULL,
                intake_count INTEGER DEFAULT 0,
                PRIMARY KEY (chat_id, water_date),
                FOREIGN KEY (chat_id) REFERENCES Users (chat_id)
            );
            """;
    public static final String INSERT_WATER_INTAKE = """
            INSERT INTO WaterIntake (chat_id, water_date, intake_count)
            VALUES (?, ?, 1)
            ON CONFLICT (chat_id, water_date) DO UPDATE
            SET intake_count = WaterIntake.intake_count + 1;
            """;
    public static final String SELECT_WATER_INTAKE = """
            SELECT intake_count
            FROM WaterIntake
            WHERE chat_id = ? AND water_date = ?;
            """;
}
