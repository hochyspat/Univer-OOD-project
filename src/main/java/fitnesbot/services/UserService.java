package fitnesbot.services;

import fitnesbot.bot.MessageOutputData;
import fitnesbot.exeptions.usererrors.InvalidParameterError;
import fitnesbot.exeptions.usererrors.NonExistenceUserError;
import fitnesbot.exeptions.usererrors.UserAlreadyExistsError;
import fitnesbot.models.SleepGoal;
import fitnesbot.models.User;
import fitnesbot.models.WaterGoal;

public class UserService {
    private final UserRepository userRepository;
    static final int UPPER_HEIGHT_LIMIT = 220;
    static final int LOWER_HEIGHT_LIMIT = 140;
    static final int UPPER_WEIGHT_LIMIT = 200;
    static final int LOWER_WEIGHT_LIMIT = 35;
    static final int UPPER_AGE_LIMIT = 100;
    static final int LOWER_AGE_LIMIT = 12;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MessageOutputData registerUser(String name, String age, String height,
                                          String weight, Long chatId) {
        if (!(isValidName(name))) {
            return new MessageOutputData(
                    new InvalidParameterError("имя").getErrorMessage(), chatId);
        }
        if (!(isValidInputParameter(height, LOWER_HEIGHT_LIMIT, UPPER_HEIGHT_LIMIT))) {
            return new MessageOutputData(
                    new InvalidParameterError("рост").getErrorMessage(), chatId);
        }
        if (!(isValidInputParameter(weight, LOWER_WEIGHT_LIMIT, UPPER_WEIGHT_LIMIT))) {
            return new MessageOutputData(
                    new InvalidParameterError("вес").getErrorMessage(), chatId);
        }
        if (!(isValidInputParameter(age, LOWER_AGE_LIMIT, UPPER_AGE_LIMIT))) {
            return new MessageOutputData(
                    new InvalidParameterError("возраст").getErrorMessage(), chatId);
        }

        if (!userRepository.existsById(chatId)) {
            User user = new User(
                    name,
                    Integer.parseInt(height),
                    Integer.parseInt(weight),
                    Integer.parseInt(age), chatId);
            userRepository.save(user);
            return new MessageOutputData("Отлично! Пользователь добавлен."
                    + " Введи /help для справки или /menu для выбора команд", chatId);
        } else {
            System.out.println("Пользователь уже существует." + chatId);
            return new MessageOutputData(new UserAlreadyExistsError(chatId)
                    .getErrorMessage(), chatId);
        }
    }


    public User getUser(long chatId) {
        return userRepository.findById(chatId);
    }

    public MessageOutputData saveWaterIntake(long chatId,
                                             String inputQuantity,
                                             String inputUnit) {
        User user = getUser(chatId);
        if (user == null) {
            return new MessageOutputData(new NonExistenceUserError(chatId).getErrorMessage(), chatId);
        }
        if (isNumber(inputQuantity)) {
            int quantity = Integer.parseUnsignedInt(inputQuantity);
            NutrientUnits nutrientUnit = NutrientUnits.fromString(inputUnit);
            if (nutrientUnit != null) {
                user.setWaterGoal(new WaterGoal(quantity, nutrientUnit));
                return new MessageOutputData(
                        "Цель по потреблению воды успешно установлена!", chatId);
            }
        } else {
            return new MessageOutputData(
                    "Неправильные параметры при установке цели по воде,попробуйте еще раз",
                    chatId);
        }
        return new MessageOutputData(
                "Ошибка при установке цели по воде",
                chatId);
    }

    public MessageOutputData saveSleepInTake(long chatId,
                                             String inputQuantity) {
        User user = getUser(chatId);
        if (user == null) {
            return new MessageOutputData(new NonExistenceUserError(chatId).getErrorMessage(), chatId);
        }
        if (isNumber(inputQuantity)) {
            int quantity = Integer.parseUnsignedInt(inputQuantity);
            user.setSleepGoal(new SleepGoal(quantity));
            return new MessageOutputData(
                    "Цель по количеству сна успешно установлена!", chatId);
        } else {
            return new MessageOutputData(
                    "Неправильные параметры при установке цели сна,попробуйте еще раз",
                    chatId);
        }
    }

    private boolean isValidName(String inputName) {
        return inputName != null && !inputName.trim().isEmpty();
    }

    private boolean isInCorrectBounds(int value, int lowerBound, int upperBound) {
        return value >= lowerBound && value <= upperBound;
    }

    private boolean isNumber(String value) {
        return value.matches("-?\\d+");
    }

    private boolean isValidInputParameter(String inputParameter, int lowerBound, int upperBound) {
        if (isNumber(inputParameter)) {
            int result = Integer.parseInt(inputParameter);
            return isInCorrectBounds(result, lowerBound, upperBound);
        }
        return false;
    }

}
