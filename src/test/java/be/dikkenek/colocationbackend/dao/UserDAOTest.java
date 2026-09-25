package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.entity.LandlordEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<LandlordEntity> typedQuery;

    @InjectMocks
    private UserDao userDao;

    private LandlordEntity sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new LandlordEntity("test@hotmail.com", "testpwd", "test", "test", "123456789");
    }

    @Nested
    @DisplayName("Tests de la méthode get()")
    class GetTests {

        @Test
        @DisplayName("Devrait retourner un Optional avec l'utilisateur quand l'ID existe")
        void get_WhenUserExists_ShouldReturnUser() {
            String userId = "user-123";
            when(entityManager.find(LandlordEntity.class, userId)).thenReturn(sampleUser);

            Optional result = userDao.get(userId);

            assertTrue(result.isPresent());
            assertEquals(sampleUser, result.get());
            verify(entityManager, times(1)).find(LandlordEntity.class, userId);
        }

        @Test
        @DisplayName("Devrait retourner un Optional vide quand l'ID n'existe pas")
        void get_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
            String userId = "unknown-id";
            when(entityManager.find(LandlordEntity.class, userId)).thenReturn(null);

            Optional result = userDao.get(userId);

            assertFalse(result.isPresent());
            verify(entityManager, times(1)).find(LandlordEntity.class, userId);
        }
    }

    @Nested
    @DisplayName("Tests de la méthode create()")
    class CreateTests {

        @BeforeEach
        void setupTransaction() {
            when(entityManager.getTransaction()).thenReturn(transaction);
        }

        @Test
        @DisplayName("Devrait créer l'utilisateur et retourner true en cas de succès")
        void create_Success_ShouldReturnTrue() {
            boolean result = userDao.create(sampleUser);

            assertTrue(result);
            verify(transaction, times(1)).begin();
            verify(entityManager, times(1)).persist(sampleUser);
            verify(transaction, times(1)).commit();
            verify(transaction, never()).rollback();
        }

        @Test
        @DisplayName("Devrait effectuer un rollback et retourner false en cas d'exception")
        void create_Failure_ShouldRollbackAndReturnFalse() {
            doThrow(new RuntimeException("Database error")).when(entityManager).persist(sampleUser);
            when(transaction.isActive()).thenReturn(true);

            boolean result = userDao.create(sampleUser);

            assertFalse(result);
            verify(transaction, times(1)).begin();
            verify(transaction, times(1)).rollback();
            verify(transaction, never()).commit();
        }
    }

    @Nested
    @DisplayName("Tests de la méthode update()")
    class UpdateTests {

        @BeforeEach
        void setupTransaction() {
            when(entityManager.getTransaction()).thenReturn(transaction);
        }

        @Test
        @DisplayName("Devrait mettre à jour l'utilisateur et retourner true en cas de succès")
        void update_Success_ShouldReturnTrue() {
            when(entityManager.merge(sampleUser)).thenReturn(sampleUser);

            boolean result = userDao.update(sampleUser);

            assertTrue(result);
            verify(transaction, times(1)).begin();
            verify(entityManager, times(1)).merge(sampleUser);
            verify(transaction, times(1)).commit();
            verify(transaction, never()).rollback();
        }

        @Test
        @DisplayName("Devrait effectuer un rollback et retourner false en cas d'erreur de fusion")
        void update_Failure_ShouldRollbackAndReturnFalse() {
            when(entityManager.merge(sampleUser)).thenThrow(new RuntimeException("Merge failed"));
            when(transaction.isActive()).thenReturn(true);

            boolean result = userDao.update(sampleUser);

            assertFalse(result);
            verify(transaction, times(1)).begin();
            verify(transaction, times(1)).rollback();
            verify(transaction, never()).commit();
        }
    }

    @Nested
    @DisplayName("Tests de la méthode delete()")
    class DeleteTests {

        @BeforeEach
        void setupTransaction() {
            when(entityManager.getTransaction()).thenReturn(transaction);
        }

        @Test
        @DisplayName("Devrait supprimer l'utilisateur existant et retourner true")
        void delete_UserExists_ShouldRemoveAndReturnTrue() {
            String userId = "user-123";
            when(entityManager.find(LandlordEntity.class, userId)).thenReturn(sampleUser);

            boolean result = userDao.delete(userId);

            assertTrue(result);
            verify(transaction, times(1)).begin();
            verify(entityManager, times(1)).find(LandlordEntity.class, userId);
            verify(entityManager, times(1)).remove(sampleUser);
            verify(transaction, times(1)).commit();
            verify(transaction, never()).rollback();
        }

        @Test
        @DisplayName("Devrait annuler et retourner false si l'utilisateur n'existe pas")
        void delete_UserDoesNotExist_ShouldRollbackAndReturnFalse() {
            String userId = "unknown-id";
            when(entityManager.find(LandlordEntity.class, userId)).thenReturn(null);

            boolean result = userDao.delete(userId);

            assertFalse(result);
            verify(transaction, times(1)).begin();
            verify(entityManager, times(1)).find(LandlordEntity.class, userId);
            verify(entityManager, never()).remove(any());
            verify(transaction, times(1)).rollback();
            verify(transaction, never()).commit();
        }

        @Test
        @DisplayName("Devrait effectuer un rollback et retourner false en cas d'exception")
        void delete_ExceptionThrown_ShouldRollbackAndReturnFalse() {
            String userId = "user-123";
            when(entityManager.find(LandlordEntity.class, userId)).thenThrow(new RuntimeException("Database error"));
            when(transaction.isActive()).thenReturn(true);

            boolean result = userDao.delete(userId);

            assertFalse(result);
            verify(transaction, times(1)).begin();
            verify(transaction, times(1)).rollback();
            verify(transaction, never()).commit();
        }
    }

    @Nested
    @DisplayName("Tests de la méthode getAll()")
    class GetAllTests {

        @Test
        @DisplayName("Devrait retourner la liste de tous les utilisateurs")
        void getAll_ShouldReturnListOfUsers() {
            List<LandlordEntity> expectedUsers = Arrays.asList(
                    sampleUser,
                    new LandlordEntity("other@hotmail.com", "otherpwd", "other", "other", "987654321")
            );
            String jpqlQuery = "SELECT u FROM LandlordEntity u";

            when(entityManager.createQuery(jpqlQuery, LandlordEntity.class)).thenReturn(typedQuery);
            when(typedQuery.getResultList()).thenReturn(expectedUsers);

            List<UserEntity> actualUsers = userDao.getAll();

            assertEquals(2, actualUsers.size());
            assertEquals(expectedUsers, actualUsers);
            verify(entityManager, times(1)).createQuery(jpqlQuery, LandlordEntity.class);
            verify(typedQuery, times(1)).getResultList();
        }
    }
}