import fitnesbot.services.Menu;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuTest {

    @Test
    public void testGetMenu() {

        Menu testMenu = new Menu();
        String expectedOutput = """
                Выберите действие:
                            
                Пользователь:
                - addUser [имя] [возраст] [рост] [вес] — Добавить нового пользователя
                - /myprofile — Показать данные профиля
                - /mycalories — Рассчитать дневную норму калорий
                            
                Дневник питания:
                - learnMeal [ингредиент1], ..., [ингредиент n] — Анализ блюда
                - addMeal [название] [ингредиент1], ..., [ингредиент n] — Добавить блюдо
                - getMeal [дата] [название] — Показать информацию о блюде за указанную дату
                - getMealChartPFC - Показать диаграмму потребления БЖУ

                Учет воды:
                - addWaterGoal [количество] [мера] — Установить цель по потреблению воды
                - addWaterInTake — Добавить прием воды
                - getWaterInTakeInfo [дата] — Показать информацию по воде за дату
                - getWaterChart - показать график по приемам воды
                            
                Учет сна:
                - addSleepGoal [количество] [часов] — Установить цель по сну
                - addSleep [количество часов] — Добавить запись сна
                - getWeekSleepStat — Статистика сна за неделю
                - getDaySleepStat [дата] — Статистика сна за дату
                - getSleepChart - показать график по количеству часов в день
                            
                Тренировки:
                - addTraining [название] [длительность в часах] [сожженные калории] — Добавить тренировку
                - getTrainings — Показать все тренировки
                - getTrainingsByDate [дата] — Показать тренировки за дату
                - deleteTraining [дата] [название] — Удалить тренировку
                - getTrainingCaloriesChart - показать график сожженых калорий
                            
                Общие команды:
                - /help — Показать справку
                - /menu — Показать меню
                - /exit — Выйти из программы
                """;

        assertEquals(testMenu.getMenu(), expectedOutput);
    }
}
