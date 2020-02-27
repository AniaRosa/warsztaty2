package pl.coderslab.warsztaty2;

import pl.coderslab.warsztaty2.daos.GroupDao;
import pl.coderslab.warsztaty2.models.Group;

import java.util.Scanner;

public class GroupManagement {

    final static String add = "add", edit = "edit", view = "view", delete = "delete", quit = "quit";
    final static String quitOptionSelection = "quitOptionSelection", quitGroupPart = "quitGroupPart";

    public static void main(String[] args) {

        String option = "", quitStatus = "";

        while (!quitStatus.equals(quitGroupPart)) {
            getGroupsList();
            quitStatus = makeActionOnGroup(option);
        }
    }

    private static String makeActionOnGroup(String option) {
        boolean correctOption = false;
        GroupDao groupDao = new GroupDao();
        while (!correctOption) {
            option = optionsSelection();
            if (option.equals(add)) {
                String name = getData("Podaj nazwę grupy: ");
                Group group = new Group(name);
                groupDao.create(group);
                correctOption = true;
                System.out.println("Grupa dodana");
            } else if (option.equals(edit)) {
                String id = getData("Podaj numer id: ");
                while (!validateData(id)) {
                    System.out.println("Niepoprawny numer id grupy");
                    id = getData("Podaj numer id: ");
                }
                Group group = groupDao.read(Integer.parseInt(id));
                String name = getData("Podaj nową nazwę grupy: ");
                group.setName(name);
                groupDao.update(group);
                correctOption = true;
                System.out.println("Grupa zaktualizowana");
            } else if (option.equals(delete)) {
                String id = getData("Podaj numer id grupy, którą chcesz usunąć: ");
                while (!validateData(id)) {
                    System.out.println("Niepoprawny numer id grupy");
                    id = getData("Podaj numer id: ");
                }
                groupDao.delete(Integer.parseInt(id));
                System.out.println("Grupa usunięta");
                correctOption = true;
            } else if (option.equals(quit)) {
                correctOption = true;
                System.out.println("Koniec!");
                return "quitGroupPart";
            } else {
                System.out.println("Spróbuj jeszcze raz!");
            }
        }

        return "quitOptionSelection";
    }

    private static void getGroupsList() {
        GroupDao groupDao = new GroupDao();
        Group[] allGroups = groupDao.findAll();
        for (Group group : allGroups) {
            System.out.println(group.toString());
        }
    }

    private static String optionsSelection() {
        System.out.println("Wybierz jedną z opcji:");
        System.out.println("--> add – dodanie grupy,");
        System.out.println("--> edit – edycja grupy,");
        System.out.println("--> delete – usunięcie grupy,");
        System.out.println("--> quit – zakończenie programu.");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static String getData(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        return scanner.nextLine();
    }

    private static boolean validateData(String data) {
        GroupDao groupDao = new GroupDao();
        Group[] allGroups = groupDao.findAll();
        String[] listToValidation = new String[allGroups.length];
        for (int i = 0; i < listToValidation.length; i++) {
            listToValidation[i] = String.valueOf(allGroups[i].getId());
        }
        for (String element : listToValidation) {
            if (element.equals(data)) {
                return true;
            }
        }
        return false;
    }

}
