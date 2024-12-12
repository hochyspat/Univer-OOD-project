package fitnesbot.models.sql;

public class SleepSQL {
    public static final String CREATE_SLEEP_RECORDS = """
            CREATE TABLE IF NOT EXISTS sleep_records (
                id SERIAL NOT NULL,
                chat_id BIGINT NOT NULL,
                sleep_date VARCHAR(50) NOT NULL,
                sleep_quantity DOUBLE PRECISION NOT NULL,
                PRIMARY KEY (chat_id, sleep_date),
                FOREIGN KEY (chat_id) REFERENCES Users (chat_id)
            );
            """;
    public static final String INSERT_SLEEP = """
            INSERT INTO sleep_records (chat_id, sleep_date, sleep_quantity)
            VALUES (?, ?, ?)
            ON CONFLICT (chat_id, sleep_date) DO UPDATE
            SET sleep_quantity = EXCLUDED.sleep_quantity;
            """;
    public static final String SELECT_SLEEP_BY_DATE = """
            SELECT sleep_quantity
            FROM sleep_records
            WHERE chat_id = ? AND sleep_date = ?;
            """;
    public static final String SELECT_WEEK_SLEEP = """
                SELECT sleep_quantity
                FROM sleep_records
                WHERE chat_id = ? AND sleep_date >= ? AND sleep_date <= ?;
            """;


}
