package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.HelloWorldDaoImpl;
import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "TESTTABLE")
public class HelloWorldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "TESTATTRIBUTE", nullable = false, length = 100)
    private String testAttribute;

    public HelloWorldEntity() {
    }

    public static HelloWorldEntity getById(int id, HelloWorldDaoImpl testDao) {
        Optional<HelloWorldEntity> optionalTest = testDao.get(id);

        return optionalTest.orElse(null);
    }

    public boolean create(HelloWorldDaoImpl testDao) {
        return testDao.create(this);
    }

    public int getId() {
        return this.id;
    }

    public void setTestAttribute(String testAttribute) {
        this.testAttribute = testAttribute;
    }

    public String getTestAttribute() {
        return this.testAttribute;
    }
}
