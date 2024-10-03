package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Help help = new Help();
        Menu menu = new Menu();
        Bot bot = new Bot(help,menu);
        bot.start();
    }
}