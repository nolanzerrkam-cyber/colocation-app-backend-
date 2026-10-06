package be.dikkenek.colocationbackend.entity;

import jakarta.persistence.*;
// a modifier pour heriter de user
// fait en attendant pour pouvoir creer les depenses
@Entity
@Table(name = "COLOC")
public class ColocEntity {
    @Id
    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    public ColocEntity() {}
}
