package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Help help = new Help();
        help.GetHelp();
        while (true) {
            Scanner in = new Scanner(System.in);
            String userRequest =in.nextLine();
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
                default:
                    System.out.println("Извини, но я тебя не понял. Для выбора действия введите название действия.");
            }
        }
    }
}