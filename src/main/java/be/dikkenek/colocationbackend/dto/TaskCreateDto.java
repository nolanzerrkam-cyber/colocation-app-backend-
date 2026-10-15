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
    setName(name);
    setDescription(description);
    setDone(isDone);
    setDeadline(deadline);
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

public boolean isDone() {
    return isDone;
}
public void setDone(boolean isDone) {
    this.isDone = isDone;
}

public Date getDeadline() {
    return deadline;
}
public void setDeadline(Date deadline) {
    this.deadline = deadline;
}
