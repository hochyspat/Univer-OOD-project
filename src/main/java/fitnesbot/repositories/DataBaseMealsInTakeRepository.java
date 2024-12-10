package fitnesbot.repositories;

import fitnesbot.bot.apiparser.JsonSimpleParser;
import fitnesbot.models.MealSQL;
import fitnesbot.models.MealsInTake;
import fitnesbot.services.DataBaseService;
import fitnesbot.services.MealType;
import fitnesbot.services.MealsInTakeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DataBaseMealsInTakeRepository implements MealsInTakeRepository {

    @Override
    public void save(MealsInTake mealInTake, long chatId, String date, MealType mealType) {
        try (Connection connection = DataBaseService.connect();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     MealSQL.INSERT_OR_UPDATE_MEALINTAKE)) {
            statement.setLong(1, chatId);
            statement.setString(2, date);
            statement.setString(3, mealType.getMealType());
            statement.setString(4, mealInTake.toString());
            int isUpdate = statement.executeUpdate();
            if (isUpdate > 0) {
                System.out.println("Прием пищи успешно сохранен: ");
            } else {
                System.out.println("Прием пищи не сохранен: ");

            }
        } catch (SQLException e) {
            System.err.println("Ошибка сохранения приема пищи: " + e.getMessage());
        }
    }

    @Override
    public MealsInTake findByMealsInTakeType(MealType mealType, String date, long chatId) {
        try (Connection connection = DataBaseService.connect();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     MealSQL.SELECT_MEALS_BY_DATE)) {
            statement.setLong(1, chatId);
            statement.setString(2, date);
            statement.setString(3, mealType.getMealType());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println(resultSet.getString("meal_type"));
                String ingredients = resultSet.getString("ingredients");
                JsonSimpleParser parser = new JsonSimpleParser();
                return parser.parseToIntake(ingredients);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка сохранения приема пищи: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteMealType(MealType mealType, String date, long chatId) {
        try (Connection connection = DataBaseService.connect();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     MealSQL.DELETE_MEAL)) {
            statement.setLong(1, chatId);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Ошибка сохранения приема пищи: " + e.getMessage());
        }
    }
}