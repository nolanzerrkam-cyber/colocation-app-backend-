package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Roommate")
public class RoommateEntity extends UserEntity {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskEntity> tasks;

    public RoommateEntity() {
    }

    public RoommateEntity(String email, String password, String firstname, String lastname, String phonenumber) throws BackendException {
        super(email, password, firstname, lastname, phonenumber);
        this.tasks = new ArrayList<>();
    }

    public RoommateEntity(String email, String password, String firstname, String lastname, String phonenumber, List<TaskEntity> tasks) throws BackendException {
        this(email, password, firstname, lastname, phonenumber);
        this.tasks = tasks;
    }

    public void addTask(TaskEntity taskEntity) throws BackendException {
        if (taskEntity == null)
            throw new BackendException(Response.Status.INTERNAL_SERVER_ERROR, "TaskEntity cannot be null");

        if (!tasks.contains(taskEntity))
            tasks.add(taskEntity);
    }

    public void removeTask(TaskEntity taskEntity) {
        tasks.remove(taskEntity);
    }
}
