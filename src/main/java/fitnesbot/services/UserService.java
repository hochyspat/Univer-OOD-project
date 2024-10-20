package fitnesbot.services;

import fitnesbot.Errors;
import fitnesbot.bot.MessageData;
import fitnesbot.models.User;
import fitnesbot.out.OutputService;
import fitnesbot.repositories.UserRepository;

public class UserService {
    private IUserRepository userRepository;
    private OutputService outputService;
    final int UPPER_HEIGHT_LIMIT = 220;
    final int LOWER_HEIGHT_LIMIT = 140;
    final int UPPER_WEIGHT_LIMIT = 200;
    final int LOWER_WEIGHT_LIMIT = 35;
    final int UPPER_AGE_LIMIT = 100;
    final int LOWER_AGE_LIMIT = 12;

    public UserService(IUserRepository userRepository,OutputService outputService) {
        this.userRepository = userRepository;
        this.outputService = outputService;
    }

    public void registerUser(String name, String age, String height, String weight, Long chatId) {
        if ((!(isValidName(name)))){
            outputService.output(new MessageData(Errors.INPUT.getErrorMessage(),chatId));
            return;
        }
        if ((!(isValidInputParameter(height, LOWER_HEIGHT_LIMIT, UPPER_HEIGHT_LIMIT)))){
            outputService.output(new MessageData(Errors.INPUT.getErrorMessage(),chatId));
            return;
        }
        if ((!(isValidInputParameter(weight, LOWER_WEIGHT_LIMIT, UPPER_WEIGHT_LIMIT)))){
            outputService.output(new MessageData(Errors.INPUT.getErrorMessage(),chatId));
            return;
        }
        if ((!(isValidInputParameter(age, LOWER_AGE_LIMIT, UPPER_AGE_LIMIT)))){
            outputService.output(new MessageData(Errors.INPUT.getErrorMessage(),chatId));
            return;
        }

        if (!userRepository.existsById(chatId)){
            User user = new User(name, Integer.parseInt(height), Integer.parseInt(weight), Integer.parseInt(age),chatId);
            userRepository.save(user);
            outputService.output(new MessageData("Отлично! Введи /help для справки или /menu для выбора команд",chatId));
        }
        else {
            outputService.output(new MessageData("User already exists!",chatId));
        }


    }

    public User getUser(long chatId) {
        return userRepository.findById(chatId);
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
