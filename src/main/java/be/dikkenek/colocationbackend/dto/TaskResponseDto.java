package be.dikkenek.colocationbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskResponseDto {

    private int id;
    private String name;
    private String description;
    private boolean isDone;
    private String deadline;
    private String roommateFullName;

    public TaskResponseDto() {
    }

    public TaskResponseDto(int id, String name, String description, boolean isDone, String deadline, String roommateFullName) {
        setId(id);
        setName(name);
        setDescription(description);
        setIsDone(isDone);
        setDeadline(deadline);
        setRoommateFullName(roommateFullName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("isDone")
    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getRoommateFullName() {
        return roommateFullName;
    }

    public void setRoommateFullName(String roommateFullName) {
        this.roommateFullName = roommateFullName;
    }
}
