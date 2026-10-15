package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.TaskDao;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.Date;

@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_done")
    private boolean isDone;

    @Column(name = "deadline")
    private Date deadline;

    public TaskEntity() {
    }

    public TaskEntity(String name, String description, boolean isDone, Date deadline) throws BackendException {
        setName(name);
        setDescription(description);
        setIsDone(isDone);
        setDeadline(deadline);
    }

    public boolean create(TaskDao taskDao) throws BackendException {
        return taskDao.create(this);
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

    public void setName(String name) throws BackendException {
        if (name.isEmpty())
            throw new BackendException(Response.Status.BAD_REQUEST, "Error: Task name must not be empty.");
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

    public void turnIsDone() {
        isDone = !isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) throws BackendException {
        if (deadline == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "Error: Date cannot be null.");
        this.deadline = deadline;
    }
}
