package fitnesbot.exeptions;

public class NonExistenceUserError extends UserErrors {
    private final long userId;

    public NonExistenceUserError(long userId) {

        super("Пользователя не существует");
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
