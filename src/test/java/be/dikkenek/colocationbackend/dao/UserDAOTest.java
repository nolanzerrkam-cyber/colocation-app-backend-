package be.dikkenek.colocationbackend.dao;

import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.entity.*;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoTest
{
    private static final String EMAIL = "johndoe@gmail.com";
    private static final String GET_ALL_QUERY = "SELECT u FROM UserEntity u";

    @Mock
    private EntityManager man;
    @Mock
    private EntityTransaction tr;
    @Mock
    private TypedQuery<UserEntity> query;

    private UserDao userDao;

    @BeforeEach
    void setUp()
    {
        userDao = new UserDao(man);
    }

    @Test
    void get_found_returnsOptionalWithUser()
    {
        // given
        UserEntity user = mock(RoommateEntity.class);
        when(man.find(UserEntity.class, EMAIL)).thenReturn(user);

        // when
        Optional<UserEntity> result = userDao.get(EMAIL);

        // then
        assertTrue(result.isPresent());
        assertSame(user, result.get());
    }

    @Test
    void get_notFound_returnsEmptyOptional()
    {
        // given
        when(man.find(UserEntity.class, EMAIL)).thenReturn(null);

        // when
        Optional<UserEntity> result = userDao.get(EMAIL);

        // then
        assertTrue(result.isEmpty());
    }


    @Test
    void create_success_returnsTrue()
    {
        // given
        UserEntity user = mock(RoommateEntity.class);
        when(man.getTransaction()).thenReturn(tr);

        // when
        boolean result = userDao.create(user);

        // then
        assertTrue(result);
        verify(tr, times(1)).begin();
        verify(man, times(1)).persist(user);
        verify(tr, times(1)).commit();
        verify(tr, never()).rollback();
    }

    @Test
    void create_exception_returnsFalseAndRollback()
    {
        // given
        UserEntity user = mock(RoommateEntity.class);
        when(man.getTransaction()).thenReturn(tr);
        doThrow(new RuntimeException("db error")).when(man).persist(user);
        when(tr.isActive()).thenReturn(true);

        // when
        boolean result = userDao.create(user);

        // then
        assertFalse(result);
        verify(tr, never()).commit();
        verify(tr, times(1)).rollback();
    }

    @Test
    void create_exceptionInactiveTransaction_noRollback()
    {
        // given
        UserEntity user = mock(RoommateEntity.class);
        when(man.getTransaction()).thenReturn(tr);
        doThrow(new RuntimeException("db error")).when(man).persist(user);
        when(tr.isActive()).thenReturn(false);

        // when
        boolean result = userDao.create(user);

        // then
        assertFalse(result);
        verify(tr, never()).rollback();
    }


    @Test
    void update_success_returnsTrue()
    {
        // given
        UserEntity user = mock(RoommateEntity.class);
        when(man.getTransaction()).thenReturn(tr);

        // when
        boolean result = userDao.update(user);

        // then
        assertTrue(result);
        verify(tr, times(1)).begin();
        verify(man, times(1)).merge(user);
        verify(tr, times(1)).commit();
        verify(tr, never()).rollback();
    }

    @Test
    void update_exception_returnsFalseAndRollback()
    {
        // given
        UserEntity user = mock(RoommateEntity.class);
        when(man.getTransaction()).thenReturn(tr);
        when(man.merge(user)).thenThrow(new RuntimeException("db error"));
        when(tr.isActive()).thenReturn(true);

        // when
        boolean result = userDao.update(user);

        // then
        assertFalse(result);
        verify(tr, never()).commit();
        verify(tr, times(1)).rollback();
    }

    @Test
    void update_exceptionInactiveTransaction_noRollback()
    {
        // given
        UserEntity user = mock(RoommateEntity.class);
        when(man.getTransaction()).thenReturn(tr);
        when(man.merge(user)).thenThrow(new RuntimeException("db error"));
        when(tr.isActive()).thenReturn(false);

        // when
        boolean result = userDao.update(user);

        // then
        assertFalse(result);
        verify(tr, never()).rollback();
    }

    @Test
    void delete_found_returnsTrue()
    {
        // given
        UserEntity user = mock(RoommateEntity.class);
        when(man.getTransaction()).thenReturn(tr);
        when(man.find(UserEntity.class, EMAIL)).thenReturn(user);

        // when
        boolean result = userDao.delete(EMAIL);

        // then
        assertTrue(result);
        verify(tr, times(1)).begin();
        verify(man, times(1)).remove(user);
        verify(tr, times(1)).commit();
        verify(tr, never()).rollback();
    }

    @Test
    void delete_notFound_returnsFalseAndRollback()
    {
        // given
        when(man.getTransaction()).thenReturn(tr);
        when(man.find(UserEntity.class, EMAIL)).thenReturn(null);

        // when
        boolean result = userDao.delete(EMAIL);

        // then
        assertFalse(result);
        verify(man, never()).remove(any());
        verify(tr, never()).commit();
        verify(tr, times(1)).rollback();
    }

    @Test
    void delete_exception_returnsFalseAndRollback()
    {
        // given
        when(man.getTransaction()).thenReturn(tr);
        when(man.find(UserEntity.class, EMAIL)).thenThrow(new RuntimeException("db error"));
        when(tr.isActive()).thenReturn(true);

        // when
        boolean result = userDao.delete(EMAIL);

        // then
        assertFalse(result);
        verify(tr, never()).commit();
        verify(tr, times(1)).rollback();
    }

    @Test
    void delete_exceptionInactiveTransaction_noRollback()
    {
        // given
        when(man.getTransaction()).thenReturn(tr);
        when(man.find(UserEntity.class, EMAIL)).thenThrow(new RuntimeException("db error"));
        when(tr.isActive()).thenReturn(false);

        // when
        boolean result = userDao.delete(EMAIL);

        // then
        assertFalse(result);
        verify(tr, never()).rollback();
    }


    @Test
    void getAll_returnsQueryResult()
    {
        // given
        List<UserEntity> users = List.of(mock(RoommateEntity.class), mock(RoommateEntity.class));
        when(man.createQuery(GET_ALL_QUERY, UserEntity.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(users);

        // when
        List<UserEntity> result = userDao.getAll();

        // then
        assertEquals(2, result.size());
        assertSame(users, result);
    }

    @Test
    void getAll_noUsers_returnsEmptyList()
    {
        // given
        when(man.createQuery(GET_ALL_QUERY, UserEntity.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        // when
        List<UserEntity> result = userDao.getAll();

        // then
        assertTrue(result.isEmpty());
    }
}