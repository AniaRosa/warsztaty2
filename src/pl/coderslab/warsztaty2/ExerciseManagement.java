package pl.coderslab.warsztaty2;

import pl.coderslab.warsztaty2.daos.ExerciseDao;
import pl.coderslab.warsztaty2.models.Exercise;

import java.util.Scanner;

public class ExerciseManagement {

    final static String add = "add", edit = "edit", view = "view", delete = "delete", quit = "quit";
    final static String quitOptionSelection = "quitOptionSelection", quitExercisePart = "quitExercisePart";

    public static void main(String[] args) {

        String option = "", quitStatus = "";

        while (!quitStatus.equals(quitExercisePart)) {
            getExercisesList();
            quitStatus = makeActionOnExercise(option);
        }
    }

    private static String makeActionOnExercise(String option) {
        boolean correctOption = false;
        ExerciseDao exerciseDao = new ExerciseDao();
        while (!correctOption) {
            option = optionsSelection();
            if (option.equals(add)) {
                String title = getData("Podaj nazwę zadania: ");
                String description = getData("Podaj opis zadania: ");
                Exercise exercise = new Exercise(title, description);
                exerciseDao.create(exercise);
                correctOption = true;
                System.out.println("Zadanie dodane");
            } else if (option.equals(edit)) {
                String id = getData("Podaj numer id: ");
                while (!validateData(id)) {
                    System.out.println("Niepoprawny numer id zadania");
                    id = getData("Podaj numer id: ");
                }
                Exercise exercise = exerciseDao.read(Integer.parseInt(id));
                String title = getData("Podaj nową nazwę zadania: ");
                String description = getData("Podaj nowy opis zadania: ");
                exercise.setTitle(title);
                exercise.setDescription(description);
                exerciseDao.update(exercise);
                correctOption = true;
                System.out.println("Zadanie zaktualizowane");
            } else if (option.equals(delete)) {
                String id = getData("Podaj numer id zadania, które chcesz usunąć: ");
                while (!validateData(id)) {
                    System.out.println("Niepoprawny numer id zadania");
                    id = getData("Podaj numer id: ");
                }
                exerciseDao.delete(Integer.parseInt(id));
                System.out.println("Zadanie usunięte");
                correctOption = true;
            } else if (option.equals(quit)) {
                correctOption = true;
                System.out.println("Koniec!");
                return "quitExercisePart";
            } else {
                System.out.println("Spróbuj jeszcze raz!");
            }
        }

        return "quitOptionSelection";
    }

    private static void getExercisesList() {
        ExerciseDao exerciseDao = new ExerciseDao();
        Exercise[] allExercises = exerciseDao.findAll();
        for (Exercise exercise : allExercises) {
            System.out.println(exercise.toString());
        }
    }

    private static String optionsSelection() {
        System.out.println("Wybierz jedną z opcji:");
        System.out.println("--> add – dodanie zadania,");
        System.out.println("--> edit – edycja zadania,");
        System.out.println("--> delete – usunięcie zadania,");
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
        ExerciseDao exerciseDao = new ExerciseDao();
        Exercise[] allExercises = exerciseDao.findAll();
        String[] listToValidation = new String[allExercises.length];
        for (int i = 0; i < listToValidation.length; i++) {
            listToValidation[i] = String.valueOf(allExercises[i].getId());
        }
        for (String element : listToValidation) {
            if (element.equals(data)) {
                return true;
            }
        }
        return false;
    }

}
