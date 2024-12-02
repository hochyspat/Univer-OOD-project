package fitnesbot.exeptions.mealsintakeerrors;

public class UserDiaryNotFoundError extends MealInTakeErrors {
    public UserDiaryNotFoundError(long chatId) {
        super("Diary not found for user ID " + chatId);
    }
}
