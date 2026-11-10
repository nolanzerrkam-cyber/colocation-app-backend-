package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.HelloWorldDao;
import be.dikkenek.colocationbackend.exception.BackendException;
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

    public HelloWorldEntity(int id, String testAttribute) {
        setId(id);
        setTestAttribute(testAttribute);
    }

    public static HelloWorldEntity getById(int id, HelloWorldDao helloWorldDao) throws BackendException {
        Optional<HelloWorldEntity> optionalTest = helloWorldDao.get(id);

        return optionalTest.orElse(null);
    }

    public boolean create(HelloWorldDao helloWorldDao) throws BackendException {
        return helloWorldDao.create(this);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTestAttribute(String testAttribute) {
        this.testAttribute = testAttribute;
    }

    public String getTestAttribute() {
        return this.testAttribute;
    }
}
