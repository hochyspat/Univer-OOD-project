package org.example;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Bot {
    private Scanner scanner;
    private PrintStream out;
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
                default:
                    System.out.println("Извини, но я тебя не понял. Для выбора действия введите название действия.");
            }
        }
    }


}
