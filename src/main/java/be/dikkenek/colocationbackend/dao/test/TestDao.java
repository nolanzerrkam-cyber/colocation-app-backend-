package be.dikkenek.colocationbackend.dao.test;

import be.dikkenek.colocationbackend.entity.TestEntity;

import java.util.Optional;

public interface TestDao {
    Optional<TestEntity> getTestById(int id);
    boolean save(TestEntity testModel);
}
