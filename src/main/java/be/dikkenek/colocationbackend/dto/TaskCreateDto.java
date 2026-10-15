package be.dikkenek.colocationbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class TaskCreateDto {

    private String name;
    private String description;
    @JsonProperty("isDone")
    private boolean isDone;
    private Date deadline;

    public TaskCreateDto() {
    }

    public TaskCreateDto(String name, String description, boolean isDone, Date deadline) {
        this.name = name;
        this.description = description;
        this.isDone = isDone;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public Date getDeadline() {
        return deadline;
    }
}
