package fitnesbot.services;

import fitnesbot.repositories.database.DataBaseMealsInTakeRepository;
import fitnesbot.repositories.database.DataBaseSleepRepository;
import fitnesbot.repositories.database.DataBaseTrainingRepository;
import fitnesbot.repositories.database.DataBaseUserRepository;
import fitnesbot.repositories.database.DaterBaseWaterRepository;
import fitnesbot.repositories.inmemory.InMemoryMealsInTakeRepository;
import fitnesbot.repositories.inmemory.InMemorySleepRepository;
import fitnesbot.repositories.inmemory.InMemoryTrainingRepository;
import fitnesbot.repositories.inmemory.InMemoryUserRepository;
import fitnesbot.repositories.inmemory.InMemoryWaterRepository;
import fitnesbot.services.enums.TypeRepositories;

public record Repositories(Help help, Menu menu,
                           CalorieCountingService calorieCountingService,
                           UserRepository userRepository,
                           MealsInTakeRepository mealsIntakeRepository,
                           SleepInTakeRepository sleepInTakeRepository,
                           WaterInTakeRepository waterInTakeRepository,
                           TrainingRepository trainingRepository) {
    public static Repositories createRepositories(TypeRepositories storageType) {
        Help help = new Help();
        Menu menu = new Menu();
        CalorieCountingService calorieCountingService = new CalorieCountingService();

        if (storageType == TypeRepositories.IN_MEMORY) {
            return new Repositories(
                    help,
                    menu,
                    calorieCountingService,
                    new InMemoryUserRepository(),
                    new InMemoryMealsInTakeRepository(),
                    new InMemorySleepRepository(),
                    new InMemoryWaterRepository(),
                    new InMemoryTrainingRepository()
            );
        } else if (storageType == TypeRepositories.DATABASE) {
            DataBaseService dataBaseService = new DataBaseService();
            dataBaseService.createAllTables();
            return new Repositories(
                    help,
                    menu,
                    calorieCountingService,
                    new DataBaseUserRepository(dataBaseService),
                    new DataBaseMealsInTakeRepository(dataBaseService),
                    new DataBaseSleepRepository(dataBaseService),
                    new DaterBaseWaterRepository(dataBaseService),
                    new DataBaseTrainingRepository(dataBaseService)
            );
        } else {
            throw new IllegalArgumentException("Неизвестный тип репозитория: " + storageType);
        }
    }
}


