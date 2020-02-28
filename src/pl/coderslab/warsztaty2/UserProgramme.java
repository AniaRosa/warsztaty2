package pl.coderslab.warsztaty2;

import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.warsztaty2.daos.ExerciseDao;
import pl.coderslab.warsztaty2.daos.SolutionDao;
import pl.coderslab.warsztaty2.daos.UserDao;
import pl.coderslab.warsztaty2.models.Exercise;
import pl.coderslab.warsztaty2.models.Solution;
import pl.coderslab.warsztaty2.models.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UserProgramme {
    final static String typeUserMail = "typeUserMail", typeExercises = "typeExercises", typeExercisesWithoutSolution = "typeExercisesWithoutSolution";
    final static String add = "add", view = "view", quit = "quit";
    final static String quitOptionSelection = "quitOptionSelection", quitExercisePart = "quitExercisePart";

    public static void main(String[] args) {

        UserDao userDao = new UserDao();
        User user = new User();
        int id = 0;

        //Sprawdzanie adresu mailowego
        String mail = "";
        if (args.length == 0) {
            System.out.println("Nie podano adresu mailowego!");
            mail = getData("Podaj adres mailowy");
        } else {
            mail = args[0];
        }
        while (!validateData(mail, typeUserMail, id)) {
            System.out.println("Niepoprawny adres mailowy!");
            mail = getData("Podaj adres mailowy");
        }

        User[] users = userDao.findAll();
        for (User u : users) {
            if (u.getEmail().equals(mail)) {
                id += u.getId();
            }
        }

        user = userDao.read(id);

        //Sprawdzanie hasła
        String password = getData("Podaj hasło");
        while (!checkPassword(password, user.getPassword())) {
            System.out.println("Niepoprawne hasło!");
            password = getData("Podaj hasło");
        }

        System.out.println("Zalogowano!");

        //Wybór opcji
        String option = "", quitStatus = "";

        while (!quitStatus.equals(quitExercisePart)) {
            quitStatus = makeActionOnUserProgramme(option, id);
        }

    }

    private static String makeActionOnUserProgramme(String option, int id) {
        boolean correctOption = false;
        ExerciseDao exerciseDao = new ExerciseDao();
        SolutionDao solutionDao = new SolutionDao();
        while (!correctOption) {
            option = optionsSelection();
            if (option.equals(add)) {
                Exercise[] exercisesWithoutSolution = getExercisesWithoutSolution(id);
                for (Exercise e : exercisesWithoutSolution) {
                    System.out.println(e.toString());
                }
                String exerciseId = getData("Podaj id zadania, do którego chcesz dodać rozwiązanie");
                while (!validateData(exerciseId, typeExercisesWithoutSolution, id)) {
                    System.out.println("Niepoprawny numer id");
                    exerciseId = getData("Podaj id zadania, do którego chcesz dodać rozwiązanie");
                }
                String description = getData("Podaj treść rozwiązania");

                Solution solution = new Solution();
                solution.setDescription(description);
                solution.setExercisesId(Integer.parseInt(exerciseId));
                solution.setUserId(id);
                solution.setCreated(today());
                solutionDao.create(solution);
                correctOption = true;
                System.out.println("Rozwiązanie dodane");
            } else if (option.equals(view)) {
                correctOption = true;
                getList(id);
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

    private static String getData(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(question);
        return scanner.nextLine();
    }

    private static void getList(int id) {
        SolutionDao solutionDao = new SolutionDao();
        Solution[] allSolutions = solutionDao.findAllByUserId(id);
        for (Solution solution : allSolutions) {
            System.out.println(solution.toString());
        }
    }

    private static Exercise[] getExercisesWithoutSolution(int userId) {
        SolutionDao solutionDao = new SolutionDao();
        Solution[] allSolutions = solutionDao.findAllByUserId(userId);
        int counter = 0;
        for (Solution s : allSolutions) {
            if (s.getDescription() == null) {
                counter++;
            }
        }
        Exercise[] exercisesWithoutSolution = new Exercise[counter];
        ExerciseDao exerciseDao = new ExerciseDao();
        Exercise[] allExercises = exerciseDao.findAll();
        for (int i = 0; i < allSolutions.length; i++) {
            for (int j = 0; j < allExercises.length; j++) {
                if (allSolutions[i].getExercisesId() == allExercises[j].getId()) {
                    if (allSolutions[i].getDescription() == null) {
                        for (int k = 0; k < exercisesWithoutSolution.length; k++) {
                            if (exercisesWithoutSolution[k] == null) {
                                exercisesWithoutSolution[k] = allExercises[j];
                            }
                        }
                    }
                }
            }
        }
        return exercisesWithoutSolution;
    }

    private static boolean validateData(String data, String type, int id) {
        String[] listToValidation;

        if (type.equals(typeUserMail)) {
            UserDao userDao = new UserDao();
            User[] allUsers = userDao.findAll();
            listToValidation = new String[allUsers.length];
            for (int i = 0; i < listToValidation.length; i++) {
                listToValidation[i] = allUsers[i].getEmail();
            }
        } else if (type.equals(typeExercisesWithoutSolution)) {
            Exercise[] exercises = getExercisesWithoutSolution(id);
            listToValidation = new String[exercises.length];
            for (int i = 0; i < listToValidation.length; i++) {
                listToValidation[i] = String.valueOf(exercises[i].getId());
            }
        }

        else {
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

    private static boolean checkPassword(String passwordToVerify, String userPassword){
        if (BCrypt.checkpw(passwordToVerify, userPassword)) {
            return true;
        }
        return false;
    }

    private static String optionsSelection() {
        System.out.println("Wybierz jedną z opcji:");
        System.out.println("--> add – dodawanie rozwiązania,");
        System.out.println("--> view – przeglądanie swoich rozwiązań,");
        System.out.println("--> quit – zakończenie programu.");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static String today() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
