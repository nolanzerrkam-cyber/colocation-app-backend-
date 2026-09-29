package be.dikkenek.colocationbackend.entity;

import be.dikkenek.colocationbackend.dao.TestDaoImpl;
import jakarta.persistence.*;

import java.util.Optional;

@Entity
@Table(name = "TESTTABLE")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "TESTATTRIBUTE", nullable = false, length = 100)
    private String testAttribute;

    public TestEntity() {
    }

    public static TestEntity getById(int id, TestDaoImpl testDao) {
        Optional<TestEntity> optionalTest = testDao.get(id);

        return optionalTest.orElse(null);
    }

    public boolean create(TestDaoImpl testDao) {
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
