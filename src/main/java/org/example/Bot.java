package org.example;
import java.util.HashMap;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Bot {
    private Scanner scanner;
    private PrintStream out;
    private HashMap<String,User> users = new HashMap<>();
    public Bot(InputStream input, PrintStream out) {
        this.scanner = new Scanner(input);
        this.out = out;
    }
    public void start() {
        Help help = new Help();
        help.GetHelp();
        while (true) {
            Scanner in = new Scanner(System.in);
            String userRequest = in.nextLine();
            switch (userRequest) {
                case "/help":
                    help.GetHelp();
                    break;
                case "/menu":
                    Menu menu = new Menu();
                    menu.GetMenu();
                    break;
                case "Рассчитать КБЖУ":
                    CPFCCount count = new CPFCCount();
                    count.start();
                    break;
                case "Добавить пользователя":
                    setmyname();
                default:
                    System.out.println("Извини, но я тебя не понял. Для выбора действия введите название действия.");
            }
        }
    }
    private void setmyname(){
        System.out.print("как тебя звать?");
        String name = scanner.nextLine();
        System.out.print("рост пожалуйста в см");
        String height = scanner.nextLine();
        System.out.print("теперь что весы говорят в кг ");
        String weight = scanner.nextLine();
        System.out.print("сколько годиков");
        String age = scanner.nextLine();
        User user = new User(name, height, weight, age);
        users.put(name, user);

        System.out.println("Пользователь " + name + " успешно добавлен!");
    }
    private void getuser(String name){
        User user = users.get(name);
        if (user != null) {
            System.out.println(user.getInfo());
        }
        else {
            System.out.println("ERROR user not found");
        }


    }




}
