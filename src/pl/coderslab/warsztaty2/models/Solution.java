package pl.coderslab.warsztaty2.models;

import java.util.StringJoiner;

public class Solution {

    private int id;
    private String created;
    private String updated;
    private String description;
    private int exercisesId;
    private int userId;

    public Solution(String created, String updated, String description, int exercisesId, int userId) {
        this.created = created;
        this.updated = updated;
        this.description = description;
        this.exercisesId = exercisesId;
        this.userId = userId;
    }

    public Solution() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExercisesId() {
        return exercisesId;
    }

    public void setExercisesId(int exercisesId) {
        this.exercisesId = exercisesId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Solution.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("created=" + created)
                .add("updated=" + updated)
                .add("description='" + description + "'")
                .add("exerciseId=" + exercisesId)
                .add("userId=" + userId)
                .toString();
    }
}
