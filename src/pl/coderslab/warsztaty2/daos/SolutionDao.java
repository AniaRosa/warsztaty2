package pl.coderslab.warsztaty2.daos;

import pl.coderslab.warsztaty2.DBUtil;
import pl.coderslab.warsztaty2.models.Solution;
import pl.coderslab.warsztaty2.models.User;

import java.sql.*;
import java.util.Arrays;

public class SolutionDao {

    private static final String CREATE_SOLUTION_QUERY =
            "INSERT INTO solutions(created, updated, description, exercises_id, user_id) VALUES (?, ?, ?, ?, ?)";
    private static final String READ_SOLUTION_QUERY = "SELECT * FROM solutions where id = ?";
    private static final String UPDATE_SOLUTION_QUERY =
            "UPDATE solutions SET created = ?, updated = ?, description = ?, exercises_id = ?, user_id = ? where id = ?";
    private static final String DELETE_SOLUTION_QUERY = "DELETE FROM solutions WHERE id = ?";
    private static final String FIND_ALL_SOLUTION_QUERY = "SELECT * FROM solutions";
    private static final String FIND_ALL_BY_USER_QUERY = "SELECT * FROM solutions WHERE user_id = ?";
    private static final String FIND_ALL_BY_EXERCISE_QUERY = "SELECT * FROM solutions WHERE exercises_id = ?";


    public Solution create(Solution solution) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_SOLUTION_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, solution.getCreated());
            statement.setString(2, solution.getUpdated());
            statement.setString(3, solution.getDescription());
            statement.setInt(4, solution.getExercisesId());
            statement.setInt(5, solution.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                solution.setId(resultSet.getInt(1));
            }
            return solution;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Solution read(int solutionId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(READ_SOLUTION_QUERY);
            statement.setInt(1, solutionId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getSolution(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Solution solution) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(UPDATE_SOLUTION_QUERY);
            statement.setString(1, solution.getCreated());
            statement.setString(2, solution.getUpdated());
            statement.setString(3, solution.getDescription());
            statement.setInt(4, solution.getExercisesId());
            statement.setInt(5, solution.getUserId());
            statement.setInt(6, solution.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int solutionId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(DELETE_SOLUTION_QUERY);
            statement.setInt(1, solutionId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Solution[] findAll() {
        try (Connection conn = DBUtil.getConnection()) {
            Solution[] solutions = new Solution[0];
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_SOLUTION_QUERY);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                solutions = addToArray(getSolution(resultSet), solutions);
            }
            return solutions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Solution[] addToArray(Solution s, Solution[] solutions) {
        Solution[] tmpSolutions = Arrays.copyOf(solutions, solutions.length + 1);
        tmpSolutions[solutions.length] = s;
        return tmpSolutions;
    }

    public Solution[] findAllByUserId (int userId) {
        try (Connection conn = DBUtil.getConnection()) {
            Solution[] solutions = new Solution[0];
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_BY_USER_QUERY);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                solutions = addToArray(getSolution(resultSet), solutions);
            }
            return solutions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Solution[] findAllByExerciseId(int exerciseId) {
        try (Connection conn = DBUtil.getConnection()) {
            Solution[] solutions = new Solution[0];
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_BY_EXERCISE_QUERY);
            statement.setInt(1, exerciseId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                solutions = addToArray(getSolution(resultSet), solutions);
            }
            return solutions;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Solution getSolution(ResultSet resultSet) throws SQLException {
        Solution solution = new Solution();
        solution.setId(resultSet.getInt("id"));
        solution.setCreated(resultSet.getString("created"));
        solution.setUpdated(resultSet.getString("updated"));
        solution.setDescription(resultSet.getString("description"));
        solution.setExercisesId(resultSet.getInt("exercises_id"));
        solution.setUserId(resultSet.getInt("user_id"));

        return solution;
    }
}
