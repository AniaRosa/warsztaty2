package pl.coderslab.warsztaty2;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.warsztaty2.daos.UserDao;
import pl.coderslab.warsztaty2.models.User;

import java.util.Scanner;

public class UserManagement {
final static String add = "add", edit = "edit", view = "view", delete = "delete", quit = "quit";
final static String quitOptionSelection = "quitOptionSelection", quitUserPart = "quitUserPart";
final static String typeGroupId = "groupId", typeId = "id", typePassword = "password";

    public static void main(String[] args) {

        String option = "", quitStatus = "";

        while (!quitStatus.equals(quitUserPart)) {
            getUsersList();
            quitStatus = makeActionOnUser(option);
        }
    }

    private static String makeActionOnUser(String option) {
        boolean correctOption = false;
        UserDao userDao = new UserDao();
        while (!correctOption) {
            option = optionsSelection();
            if (option.equals(add)) {
                String userName = getData("Podaj nazwę użytkownika: ");
                String email = getData("Podaj adres email: ");
                String password = getData("Podaj hasło: ");
                String groupId = getData("Podaj numer ID grupy: ");
                while (!validateData(groupId, typeGroupId)) {
                    System.out.println("Niepoprawny numer grupy");
                    groupId = getData("Podaj numer ID grupy: ");
                }
                User user = new User(userName, email, password, Integer.parseInt(groupId));
                userDao.create(user);
                correctOption = true;
                System.out.println("Użytkownik dodany");
            } else if (option.equals(edit)) {
                String id = getData("Podaj numer id: ");
                while (!validateData(id, typeId)) {
                    System.out.println("Niepoprawny numer id użytkownika");
                    id = getData("Podaj numer id: ");
                }
                User user = userDao.read(Integer.parseInt(id));
                String password = getData("Podaj hasło: ");
                while (!checkPassword(password, user.getPassword())) {
                    System.out.println("Niepoprawne hasło");
                    password = getData("Podaj hasło: ");
                }
                String userName = getData("Podaj nową nazwę użytkownika: ");
                String email = getData("Podaj nowy adres email: ");

                String groupId = getData("Podaj nowy numer ID grupy: ");
                while (!validateData(groupId, typeGroupId)) {
                    System.out.println("Niepoprawny numer grupy");
                    groupId = getData("Podaj numer ID grupy: ");
                }
                user.setUserName(userName);
                user.setEmail(email);
                user.setUserGroupId(Integer.parseInt(groupId));
                userDao.update(user);
                correctOption = true;
                System.out.println("Dane użytkownika zaktualizowane");
            } else if (option.equals(delete)) {
                String id = getData("Podaj numer id użytkownika, którego chcesz usunąć: ");
                while (!validateData(id, typeId)) {
                    System.out.println("Niepoprawny numer id użytkownika");
                    id = getData("Podaj numer id: ");
                }
                userDao.delete(Integer.parseInt(id));
                correctOption = true;
            } else if (option.equals(quit)) {
                correctOption = true;
                System.out.println("Koniec!");
                return "quitUserPart";
            } else {
                System.out.println("Spróbuj jeszcze raz!");
            }
        }

        return "quitOptionSelection";
    }

    private static void getUsersList() {
        UserDao userDao = new UserDao();
        User[] allUsers = userDao.findAll();
        for (User user : allUsers) {
            System.out.println(user.toString());
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

    private static boolean checkPassword(String password, User user) {
        if (BCrypt.checkpw(password, user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean validateData(String data, String type) {
        UserDao userDao = new UserDao();
        User[] allUsers = userDao.findAll();
        String[] listToValidation = new String[allUsers.length];
        listToValidation = createListToValidation(listToValidation, allUsers, type);

        return checkValidatedElements(listToValidation, data);
    }

    private static String[] createListToValidation(String[] listToValidation, User[] allUsers, String type) {
        if (type.equals(typeGroupId)) {
            for (int i = 0; i < listToValidation.length; i++) {
                listToValidation[i] = String.valueOf(allUsers[i].getUserGroupId());
            }
        } else if (type.equals(typeId)) {
            for (int i = 0; i < listToValidation.length; i++) {
                listToValidation[i] = String.valueOf(allUsers[i].getId());
            }
        }

        return listToValidation;
    }

    private static boolean checkValidatedElements(String[] listToValidation, String data){
        for (String element : listToValidation) {
            if (element.equals(data)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkPassword(String passwordToVerify, String userPassword){
        if (BCrypt.checkpw(passwordToVerify, userPassword)) {
            return true;
        }
        return false;
    }
}
