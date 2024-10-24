package fitnesbot;

public class Errors {

    public String invalidNumberOfArguments(String methodName, String... args){
        String errorMessage = "Неверное количество аргументов. Используйте " + methodName + " ";
        for (String arg : args) {
            errorMessage += arg + " ";
        }

        return errorMessage;
    }

    public String invalidCommand() {
        return "Неверная команда";
    }

    public String nonExistenceUser() {
        return "Пользователя не существует";
    }

    public String invalidParameter(String parameterName) {
        return "Параметр " + parameterName + " введён неверно.";
    }

    public String existenceUser() {
        return "Пользователь уже существует";
    }
}
