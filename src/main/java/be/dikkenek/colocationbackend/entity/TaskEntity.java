package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.persistence.*;
import jakarta.ws.rs.core.Response;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_task")
    private int id_task;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_done")
    private boolean isDone;

    @Column(name = "deadline", nullable = false)
    private Date deadline;

    @ManyToOne
    @JoinColumn(name = "email", nullable = false)
    private RoommateEntity roommate;

    @ManyToOne
    @JoinColumn(name = "id_flatshare")
    private FlatshareEntity flatshare;

    public FlatshareEntity getFlatshare() {
        return flatshare;
    }

    public void setFlatshare(FlatshareEntity flatshare) {
        this.flatshare = flatshare;
    }

    public int getId_task() {
        return id_task;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws BackendException {
        if (name == null || name.isEmpty())
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

    public RoommateEntity getRoommate() {
        return roommate;
    }

    public void setRoommate(RoommateEntity roommate) throws BackendException {
        if (roommate == null)
            throw new BackendException(Response.Status.BAD_REQUEST, "Error: roommate cannot be null.");

        if (!roommate.equals(this.roommate)) {
            if (this.roommate != null) this.roommate.getFlatshare().removeTask(this);
            this.roommate = roommate;
            roommate.getFlatshare().addTask(this);
        }
    }

    //Constructeurs

    public TaskEntity() {
    }

    public TaskEntity(String name, String description, boolean isDone, Date deadline, RoommateEntity roommate) throws BackendException {
        setName(name);
        setDescription(description);
        setIsDone(isDone);
        setDeadline(deadline);
        setRoommate(roommate);
        setFlatshare(roommate.getFlatshare());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TaskEntity)) return false;
        TaskEntity that = (TaskEntity) o;
        return id_task == that.id_task && isDone == that.isDone && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(deadline, that.deadline) && Objects.equals(roommate, that.roommate) && Objects.equals(flatshare, that.flatshare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_task, name, description, isDone, deadline, roommate, flatshare);
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id_task=" + id_task +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isDone=" + isDone +
                ", deadline=" + deadline +
                ", roommate=" + roommate +
                ", flatshare=" + flatshare +
                '}';
    }
}
