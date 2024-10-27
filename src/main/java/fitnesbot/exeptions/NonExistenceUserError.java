package fitnesbot.exeptions;

public class NonExistenceUserError extends UserErrors{
    public NonExistenceUserError() {
        super("Пользователя не существует");
    }
}
