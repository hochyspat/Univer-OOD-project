package fitnesbot.repositories.database;

import fitnesbot.bot.apiparser.JsonSimpleParser;
import fitnesbot.models.sql.MealSQL;
import fitnesbot.models.MealsInTake;
import fitnesbot.services.DataBaseService;
import fitnesbot.services.enums.MealType;
import fitnesbot.services.MealsInTakeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DataBaseMealsInTakeRepository implements MealsInTakeRepository {

    private final DataBaseService dataBaseService;

    public DataBaseMealsInTakeRepository(DataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    @Override
    public void save(MealsInTake mealInTake, long chatId, String date, MealType mealType) {
        try (Connection connection = dataBaseService.getConnection();
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
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     MealSQL.SELECT_MEALS_BY_DATE)) {
            statement.setLong(1, chatId);
            statement.setString(2, date);
            statement.setString(3, mealType.getMealType());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
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
    public Map<String, List<MealsInTake>> getDataById(long chatId) {
        Map<String, List<MealsInTake>> dataMeal = new HashMap<>();
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     MealSQL.SELECT_ALL_MEALS)) {
            statement.setLong(1, chatId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getString("meal_date");
                String ingredients = resultSet.getString("ingredients");
                JsonSimpleParser parser = new JsonSimpleParser();
                MealsInTake mealsInTake = parser.parseToIntake(ingredients);
                List<MealsInTake> mealsInTakes = dataMeal.getOrDefault(date, new ArrayList<>());
                mealsInTakes.add(mealsInTake);
                dataMeal.put(date, mealsInTakes);
            }
        } catch (SQLException e) {
            System.out.println("ERRor");
        }
        return dataMeal;
    }


    @Override
    public void deleteMealType(MealType mealType, String date, long chatId) {
        try (Connection connection = dataBaseService.getConnection();
             PreparedStatement statement = Objects.requireNonNull(connection).prepareStatement(
                     MealSQL.DELETE_MEAL)) {
            statement.setLong(1, chatId);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Ошибка сохранения приема пищи: " + e.getMessage());
        }
    }
}