package org.example;

public record Command(String commandData) {
    

    public boolean isValidCommand() {
        return commandData.equals("/help") || commandData.equals("/menu") ||
                commandData.equals("Рассчитать КБЖУ") || commandData.split(" ")[0].equals("информация") || commandData.equals("Добавить пользователя") ||
                commandData.equals("/exit");
    }
    public void executeCommand(Bot bot,Help help,Menu menu) {

        String[] argumentsCommand = commandData.split(" ");
        String command = (argumentsCommand[0].equals("информация")) ? argumentsCommand[0] : commandData;
        String param =(argumentsCommand.length > 1) ? argumentsCommand[1].trim() : "";
                switch (command) {
                    case "/help":
                        help.showHelp();
                        break;
                    case "/menu":
                        menu.showMenu();
                        break;
                    case "Добавить пользователя":
                        bot.readInfoAboutUser();
                        break;
                    case "Рассчитать КБЖУ":
                        bot.readDataForCalculateCPRF();
                        break;
                    case "информация":
                        if (!param.equals(""))
                        {
                            bot.showUserByName(param);
                        }
                        else
                        {
                            System.out.println("Введи имя пользователя для команды 'информация'");
                        }
                        break;
                    case "/exit":
                        System.out.print("Finish bot");
                        break;
                    default:
                        System.out.println("Неверная команда.");
                }
    }


}
