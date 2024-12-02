package fitnesbot.exeptions.usererrors;

public class UserAlreadyExistsError extends UserErrors {
    private final long userId;

    public UserAlreadyExistsError(long userId) {
        super("Пользователь уже существует.");
        this.userId = userId;

    }

    public long getUserId() {
        return userId;
    }

}
