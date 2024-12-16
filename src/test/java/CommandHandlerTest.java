import fitnesbot.bot.Command;
import fitnesbot.bot.CommandHandler;
import fitnesbot.bot.MessageCommandData;
import fitnesbot.bot.MessageOutputData;
import fitnesbot.models.User;
import fitnesbot.repositories.inmemory.InMemoryMealsInTakeRepository;
import fitnesbot.repositories.inmemory.InMemorySleepRepository;
import fitnesbot.repositories.inmemory.InMemoryTrainingRepository;
import fitnesbot.repositories.inmemory.InMemoryUserRepository;

import fitnesbot.repositories.inmemory.InMemoryWaterRepository;
import fitnesbot.services.CalorieCountingService;
import fitnesbot.services.Help;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    private SleepInTakeRepository sleepInTakeRepository;
    private TrainingRepository trainingRepository;

    @BeforeEach
    void setUp() {
        trainingRepository = new InMemoryTrainingRepository();
        userRepository = new InMemoryUserRepository();
        sleepInTakeRepository = new InMemorySleepRepository();
        calorieService = new CalorieCountingService();
        userService = new UserService(userRepository);
        mealService = new MealsInTakeService(new InMemoryMealsInTakeRepository(), new InMemoryWaterRepository());
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
        System.out.println(user.getWaterGoal());
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
        assertEquals("Тренировка \"Бег\" на дату 05.12.2024 не найдена или уже удалена.", outputData2.messageData());
    }

    @Test
    void testAddWaterIntake() {
        mealService.saveWaterInTake(12345L);
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        int count = mealService.getWaterInTake(12345L, date);
        assertEquals(1, count);
    }

    @Test
    void testAddSleepIntake() {
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        sleepService.saveSleepInTake(12345L, 7.5);
        double sleep = sleepInTakeRepository.getDayStat(12345L, date);
        assertEquals(7.5, sleep);
    }

    @Test
    void testGetWeekSleepStat() {
        sleepInTakeRepository.save(12345L, "3.12.2024", 7.0);
        sleepInTakeRepository.save(12345L, "4.12.2024", 8.0);
        sleepInTakeRepository.save(12345L, "4.12.2024", 6.5);
        sleepInTakeRepository.save(12345L, "5.12.2024", 7.5);
        sleepInTakeRepository.save(12345L, "6.12.2024", 7.0);
        sleepInTakeRepository.save(12345L, "7.12.2024", 6.0);
        sleepInTakeRepository.save(12345L, "8.12.2024", 8.0);
        double avgSleep = sleepInTakeRepository.getWeekStat(12345L);
        assertEquals(7.0, avgSleep, 0.1);
    }
}

