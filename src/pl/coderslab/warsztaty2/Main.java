package pl.coderslab.warsztaty2;

import pl.coderslab.warsztaty2.daos.UserDao;
import pl.coderslab.warsztaty2.models.User;

import java.util.Scanner;

public class Main {
final static String add = "add", edit = "edit", view = "view", delete = "delete", quit = "quit";

    public static void main(String[] args) {

        UserDao userDao = new UserDao();
        User[] allUsers = userDao.findAll();
        for (User user : allUsers) {
            System.out.println(user.toString());
        }

        String option = "";
        boolean ok = false;

        while (!ok) {
            option = optionsSelection();
            if (option.equals(add)) {
                //User user = new User();
                UserDao userDao1 = new UserDao();
                String userName = getData("Podaj nazwę użytkownika: ");
                String email = getData("Podaj adres email: ");
                String password = getData("Podaj hasło: ");
                //walidacja TODO
                int groupId = Integer.parseInt(getData("Podaj numer ID grupy: "));
                User user = new User(userName, email, password, groupId);
                userDao1.create(user);
                ok = true;
            } else if (option.equals(edit)) {
                String userName = getData("Podaj nazwę użytkownika: ");
                String email = getData("Podaj adres email: ");
                String password = getData("Podaj hasło: ");
                String id = getData("Podaj numer id: ");
                ok = true;
            } else if (option.equals(delete)) {
                System.out.println(delete);
                String id = getData("Podaj numer id użytkownika, którego chcesz usunąć: ");
                ok = true;
            } else if (option.equals(quit)) {
                System.out.println(quit);
                ok = true;
            } else {
                System.out.println("Spróbuj jeszcze raz!");
            }
        }
    }

    private static String optionsSelection() {
        System.out.println("Wybierz jedną z opcji:");
        System.out.println("--> add – dodanie użytkownika,");
        System.out.println("--> edit – edycja użytkownika,");
        System.out.println("--> delete – usunięcie użytkownika,");
        System.out.println("--> quit – zakończenie programu.");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static String getData(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        return scanner.nextLine();
    }
}
