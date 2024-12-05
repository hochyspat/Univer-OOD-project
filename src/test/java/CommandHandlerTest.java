import fitnesbot.bot.Command;
import fitnesbot.bot.CommandHandler;
import fitnesbot.bot.MessageCommandData;
import fitnesbot.bot.MessageOutputData;
import fitnesbot.models.User;
import fitnesbot.repositories.InMemorySleepRepository;
import fitnesbot.repositories.InMemoryTrainingRepository;
import fitnesbot.repositories.InMemoryUserRepository;

import fitnesbot.repositories.InMemoryWaterRepository;
import fitnesbot.services.CalorieCountingService;
import fitnesbot.services.Help;
import fitnesbot.services.MealsInTakeRepository;
import fitnesbot.services.MealsInTakeService;
import fitnesbot.services.Menu;
import fitnesbot.services.SleepInTakeRepository;
import fitnesbot.services.SleepInTakeService;
import fitnesbot.services.TrainingRepository;
import fitnesbot.services.TrainingService;
import fitnesbot.services.UserRepository;
import fitnesbot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CommandHandlerTest {

    private CommandHandler commandHandler;
    private UserRepository userRepository;
    private UserService userService;
    private CalorieCountingService calorieService;
    private MealsInTakeService mealService;
    private SleepInTakeService sleepService;
    private TrainingService trainingService;
    private MealsInTakeRepository mealsIntakeRepository;
    private SleepInTakeRepository sleepInTakeRepository;
    private TrainingRepository trainingRepository;

    @BeforeEach
    void setUp() {
        trainingRepository = new InMemoryTrainingRepository();
        userRepository = new InMemoryUserRepository();
        sleepInTakeRepository = new InMemorySleepRepository();
        calorieService = new CalorieCountingService();
        userService = new UserService(userRepository);
        mealService = new MealsInTakeService(mealsIntakeRepository, new InMemoryWaterRepository());
        sleepService = new SleepInTakeService(sleepInTakeRepository);
        trainingService = new TrainingService(trainingRepository);
        commandHandler = new CommandHandler(new Help(), new Menu(),
                calorieService, userService, mealService, sleepService, trainingService);
    }


    @Test
    void testAddUserToRepository() {
        Command command = new Command("addUser Alice 19 171 58");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));
        assertEquals("Отлично! Пользователь добавлен. Введи /help для справки "
                + "или /menu для выбора команд", outputData.messageData());
        User savedUser = userRepository.findById(12345L);
        assertNotNull(savedUser);
        assertEquals("Alice", savedUser.getName());
        assertEquals(19, savedUser.getAge());
        assertEquals(171, savedUser.getHeight());
        assertEquals(58, savedUser.getWeight());
    }

    @Test
    void testCalculateCalories() {
        userService.registerUser("Alice", "19", "171", "58", 12345L);
        Command command = new Command("/mycalories");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));
        User alice = userRepository.findById(12345L);
        assertNotNull(alice);
        assertEquals(1392.75, alice.getCalories(), 0.01);
        assertEquals("Твоя норма калорий на день: " + alice.getCalories(),
                outputData.messageData());
    }


    @Test
    void testAddUserInvalidData() {
        Command command = new Command("addUser Alice 19 171");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));
        assertNull(userRepository.findById(12345L));
        assertEquals("Неверное количество аргументов. "
                + "Используйте addUser [имя] [возраст] [рост] [вес]", outputData.messageData());
    }

    @Test
    void testInvalidParamAddUser() {
        Command command = new Command("addUser Alice 19 171 250");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));
        assertNull(userRepository.findById(12345L));
        assertEquals("Параметр вес введён неверно.", outputData.messageData());
    }

    @Test
    void testInvalidCommand() {
        Command command = new Command("неизвестнаяКоманда");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));
        assertEquals("Неверная команда", outputData.messageData());
    }

    @Test
    void testAddWaterGoal() {
        userService.registerUser("Alice", "19", "171", "58", 12345L);
        Command command = new Command("addWaterGoal 2 l");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));

        assertEquals("Цель по потреблению воды успешно установлена!", outputData.messageData());

        User user = userService.getUser(12345L);
        assertNotNull(user);
        assertEquals(2000, user.getWaterGoal().quantity(), 0.1);
        assertEquals("ml", user.getWaterGoal().units().getValue());
    }

    @Test
    void testAddTraining() {
        Command command = new Command("addTraining Бег 1.5 500");
        MessageOutputData outputData = commandHandler.handleMessage(
                new MessageCommandData(command, 12345L));

        assertEquals("Тренировка успешно добавлена!", outputData.messageData());

        assertEquals(1, trainingRepository.findByChatId(12345L).size());
    }

    @Test
    void testDeleteTraining() {
        Command command1 = new Command("addTraining Бег 1.5 500");
        MessageOutputData outputData1 = commandHandler.handleMessage(
                new MessageCommandData(command1, 12345L));
        Command command2 = new Command("deleteTraining 05.12.2024 Бег");
        MessageOutputData outputData2 = commandHandler.handleMessage(
                new MessageCommandData(command2, 12345L));
        assertEquals("Тренировка \"Бег\" успешно удалена.", outputData2.messageData());
    }

    @Test
    void testAddWaterIntake() {
        mealService.saveWaterInTake(12345L);
        String date = "05.12.2024";
        int count = mealService.getWaterInTake(12345L, "05.12.2024");
        assertEquals(1, count);
    }

    @Test
    void testAddSleepIntake() {
        String date = "05.12.2024";
        sleepService.saveSleepInTake(12345L, 7.5);
        double sleep = sleepInTakeRepository.getDayStat(12345L, date);
        assertEquals(7.5, sleep);
    }

    @Test
    void testGetWeekSleepStat() {
        sleepInTakeRepository.save(12345L, "23.11.2024", 7.0);
        sleepInTakeRepository.save(12345L, "25.11.2024", 8.0);
        sleepInTakeRepository.save(12345L, "25.11.2024", 6.5);
        sleepInTakeRepository.save(12345L, "26.11.2024", 7.5);
        sleepInTakeRepository.save(12345L, "27.11.2024", 7.0);
        sleepInTakeRepository.save(12345L, "28.11.2024", 6.0);
        sleepInTakeRepository.save(12345L, "29.11.2024", 8.0);

        double avgSleep = sleepInTakeRepository.getWeekStat(12345L);
        assertEquals(8.0, avgSleep, 0.1);
    }
}

