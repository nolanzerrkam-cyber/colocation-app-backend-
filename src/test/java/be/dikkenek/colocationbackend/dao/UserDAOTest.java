package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    @Mock
    private EntityManager man;

    @Mock
    private EntityTransaction tr;

    @Mock
    private TypedQuery typedQuery;

    @InjectMocks
    private UserDao dao;

    private UserEntity entity;

    @BeforeEach
    void setUp() {
        entity = new RoommateEntity("jean@dupont.com", "mdp1234", "jean", "dupont", "1234567890");

        lenient().when(man.getTransaction()).thenReturn(tr);
    }

    @Test
    void get_found_shouldReturnOptionalEntity() {
        when(man.find(UserEntity.class, entity.getEmail())).thenReturn(entity);

        Optional<UserEntity> result = dao.get(entity.getEmail());

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void get_notFound_shouldReturnEmptyOptional() {
        when(man.find(UserEntity.class, "unknown@email.com")).thenReturn(null);

        Optional<UserEntity> result = dao.get("unknown@email.com");

        assertFalse(result.isPresent());
    }


    @Test
    void create_success_shouldReturnTrue() {
        boolean success = dao.create(entity);

        assertTrue(success);
        verify(tr).begin();
        verify(man).persist(entity);
        verify(tr).commit();
    }

    @Test
    void create_exception_shouldRollbackAndReturnFalse() {
        doThrow(new RuntimeException("Database error")).when(man).persist(entity);
        when(tr.isActive()).thenReturn(true);

        boolean success = dao.create(entity);

        assertFalse(success);
        verify(tr).begin();
        verify(tr).rollback();
    }


    @Test
    void update_success_shouldReturnTrue() {
        boolean success = dao.update(entity);

        assertTrue(success);
        verify(tr).begin();
        verify(man).merge(entity);
        verify(tr).commit();
    }

    @Test
    void update_exception_shouldRollbackAndReturnFalse() {
        doThrow(new RuntimeException("Database error")).when(man).merge(entity);
        when(tr.isActive()).thenReturn(true);

        boolean success = dao.update(entity);

        assertFalse(success);
        verify(tr).begin();
        verify(tr).rollback();
    }


    @Test
    void delete_success_shouldReturnTrue() {
        when(man.find(UserEntity.class, entity.getEmail())).thenReturn(entity);

        boolean success = dao.delete(entity.getEmail());

        assertTrue(success);
        verify(tr).begin();
        verify(man).remove(entity);
        verify(tr).commit();
    }
    @Test
    void delete_notFound_shouldRollbackAndReturnFalse() {
        when(man.find(UserEntity.class, "unknown@email.com")).thenReturn(null);

        boolean success = dao.delete("unknown@email.com");

        assertFalse(success);
        verify(tr).begin();
        verify(man, never()).remove(any());
        verify(tr).rollback();
        verify(tr, never()).commit();
    }

    @Test
    void delete_exception_shouldRollbackAndReturnFalse() {
        when(man.find(UserEntity.class, entity.getEmail())).thenThrow(new RuntimeException("Database error"));
        when(tr.isActive()).thenReturn(true);

        boolean success = dao.delete(entity.getEmail());

        assertFalse(success);
        verify(tr).begin();
        verify(tr).rollback();
    }

    @Test
    void getAll_shouldReturnList() {
        List<UserEntity> expectedList = List.of(entity);
        when(man.createQuery("SELECT u FROM UserEntity u", UserEntity.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedList);

        List<UserEntity> result = dao.getAll();

        assertEquals(expectedList, result);
        assertEquals(1, result.size());
    }
}