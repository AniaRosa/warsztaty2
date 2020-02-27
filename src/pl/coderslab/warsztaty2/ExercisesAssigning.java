package pl.coderslab.warsztaty2;

import pl.coderslab.warsztaty2.daos.ExerciseDao;
import pl.coderslab.warsztaty2.daos.GroupDao;
import pl.coderslab.warsztaty2.daos.SolutionDao;
import pl.coderslab.warsztaty2.daos.UserDao;
import pl.coderslab.warsztaty2.models.Exercise;
import pl.coderslab.warsztaty2.models.Group;
import pl.coderslab.warsztaty2.models.Solution;
import pl.coderslab.warsztaty2.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class ExercisesAssigning {

    final static String add = "add", edit = "edit", view = "view", delete = "delete", quit = "quit";
    final static String quitOptionSelection = "quitOptionSelection", quitAssigningPart = "quitAssigningPart";
    final static String typeUserId = "userId", typeExerciseId = "exerciseId";

    public static void main(String[] args) {

        String option = "", quitStatus = "";

        while (!quitStatus.equals(quitAssigningPart)) {
            //getGroupsList();
            quitStatus = makeActionOnGroup(option);
        }
    }

    private static String makeActionOnGroup(String option) {
        boolean correctOption = false;
        GroupDao groupDao = new GroupDao();
        SolutionDao solutionDao = new SolutionDao();
        while (!correctOption) {
            option = optionsSelection();
            if (option.equals(add)) {
                getList(typeUserId);
                String userId = getData("Podaj id użytkownika: ");
                while (!validateData(userId, typeUserId)) {
                    System.out.println("Niepoprawny numer id uzytkownika");
                    userId = getData("Podaj id użytkownika: ");
                }
                getList(typeExerciseId);
                String exerciseId = getData("Podaj id zadania: ");
                while (!validateData(exerciseId, typeExerciseId)) {
                    System.out.println("Niepoprawny numer id zadania");
                    exerciseId = getData("Podaj id zadania: ");
                }
                Solution solution = new Solution();
                solution.setCreated(today());
                solution.setUserId(Integer.parseInt(userId));
                solution.setExercisesId(Integer.parseInt(exerciseId));
                solutionDao.create(solution);
                correctOption = true;
                System.out.println("Zadanie przypisane do użytkownika");
            } else if (option.equals(view)) {
                String id = getData("Podaj numer id uzytkownika, którego zadania chcesz zobaczyć: ");
                while (!validateData(id, typeUserId)) {
                    System.out.println("Niepoprawny numer id uzytkownika");
                    id = getData("Podaj numer id: ");
                }
                Solution[] solutions = solutionDao.findAllByUserId(Integer.parseInt(id));
                for (Solution solution : solutions) {
                    System.out.println(solution.toString());
                }
                correctOption = true;
            } else if (option.equals(quit)) {
                correctOption = true;
                System.out.println("Koniec!");
                return "quitAssigningPart";
            } else {
                System.out.println("Spróbuj jeszcze raz!");
            }
        }

        return "quitOptionSelection";
    }

    private static void getList(String type) {
        if (type.equals(typeUserId)) {
            UserDao userDao = new UserDao();
            User[] allUsers = userDao.findAll();
            for (User user : allUsers) {
                System.out.println(user.toString());
            }
        } else {
            ExerciseDao exerciseDao = new ExerciseDao();
            Exercise[] allExercises = exerciseDao.findAll();
            for (Exercise exercise : allExercises) {
                System.out.println(exercise.toString());
            }
        }

    }

    private static String optionsSelection() {
        System.out.println("Wybierz jedną z opcji:");
        System.out.println("--> add – przypisywanie zadań do użytkowników,");
        System.out.println("--> view – przeglądanie rozwiązań danego użytkownika,");
        System.out.println("--> quit – zakończenie programu.");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static String getData(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        return scanner.nextLine();
    }

    private static boolean validateData(String data, String type) {
        String[] listToValidation;
        if (type.equals(typeUserId)) {
            UserDao userDao = new UserDao();
            User[] allUsers = userDao.findAll();
            listToValidation = new String[allUsers.length];
            for (int i = 0; i < listToValidation.length; i++) {
                listToValidation[i] = String.valueOf(allUsers[i].getId());
            }
        } else {
            ExerciseDao exerciseDao = new ExerciseDao();
            Exercise[] allExercises = exerciseDao.findAll();
            listToValidation = new String[allExercises.length];
            for (int i = 0; i < listToValidation.length; i++) {
                listToValidation[i] = String.valueOf(allExercises[i].getId());
            }
        }

        for (String element : listToValidation) {
            if (element.equals(data)) {
                return true;
            }
        }
        return false;
    }

    private static String today() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       return formatter.format(date);
    }
}
