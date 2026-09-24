package be.dikkenek.colocationbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TESTTABLE")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "TESTATTRIBUTE", nullable = false, length = 100)
    private String testAttribute;

    public TestEntity(String testAttribute) {
        setTestAttribute(testAttribute);
    }

    public TestEntity() {
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
