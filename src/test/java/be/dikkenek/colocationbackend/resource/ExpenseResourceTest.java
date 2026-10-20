package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.ExpenseDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.ExpenseInsertDto;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ExpenseResourceTest {

    @Mock
    // creating a false dao
    private ExpenseDao expenseDAO;

    @Mock
    // NEW : Since now we are finding the roommate from his email via this dao, I have to implent it. This is going to change a lot in the unit test.
    private UserDao userDao;

    @InjectMocks
    // there im creating an object, to test later the false dao
    private ExpenseResource expenseResource;

    private ExpenseInsertDto validDto() {
        ExpenseInsertDto dto = new ExpenseInsertDto();
        dto.setPrice(24.50f);
        dto.setLabel("Courses");
        dto.setEmail("test@test.com");
        dto.setDateExpense(new Date());
        return dto;
    }

    // NEW : just to check if the false dao if the roommate exists does return a fake user.
    // This is really important since im going to check on every test if the roommate exist, to
    // reproduce the fact that ExpenseRessource has to check if the user really exist or not.

    /* Why this unit test is in private ?
        The role of roomate_exists is simple : its a code that we are going to use on multiple unit test.
        I have to put it in private because "Mockito.when... thenReturn" is called a Stub. A stub not being used by any method is going to throw an error. Here the method we are going to use is postExpense.
        If behind, postExpense is not called, mockito will throw an error, because the stub needs to have postExpense.
        The error threw is UnnecessaryStubbingException (since, yes, a stub NOT being used is useless.)
     */
    private void roommate_exists() throws BackendException
    {
        Optional<UserEntity> user = Optional.of(Mockito.mock(RoommateEntity.class));
        Mockito.when(userDao.get("test@test.com")).thenReturn(user);
    }

    @Test
    void testPostExpense_Success() throws BackendException
    {
        roommate_exists();
        // when the DAO is created, it has to return true (to do an illusion of insertion into oracle)
        Mockito.when(expenseDAO.create(Mockito.any(ExpenseEntity.class))).thenReturn(true);

        // Im calling the postExpense to build the ExpenseEntity , it will do the @Inject from the ressource of expenseDAO (calling the false DAO) and than return true (normally)
        // Be careful that the expenseDAO here and in ExpenseRessource is the same, each field is a reference
        Response response = expenseResource.postExpense(validDto());

        // We verify if the ressource did successfully the creation, exactly one time (because we don't need more)
        assertEquals(201, response.getStatus());
        Mockito.verify(expenseDAO, Mockito.times(1)).create(Mockito.any(ExpenseEntity.class));
    }

    @Test
    void testPostExpense_DaoFails() throws BackendException
    {
        roommate_exists();
        Mockito.when(expenseDAO.create(Mockito.any(ExpenseEntity.class))).thenReturn(false);

        Response response = expenseResource.postExpense(validDto());

        assertEquals(503, response.getStatus());
    }

    /*@Test
    void testPostExpense_NullBody() throws BackendException
    {
        Response response = expenseResource.postExpense(null);

        assertEquals(400, response.getStatus());
        Mockito.verify(expenseDAO, Mockito.never()).create(Mockito.any());
    }*/

    @Test
    void testPostExpense_NegativePrice() throws BackendException
    {
        roommate_exists(); // // The roommate must exist here otherwise, the 400 status would come from the roommate check instead of the negative price.
        ExpenseInsertDto dto = validDto();
        dto.setPrice(-5f);

        Response response = expenseResource.postExpense(dto);

        //assertEquals(400, response.getStatus());
        assertThrows(BackendException.class, () -> {
            expenseResource.postExpense(dto);
        });
        Mockito.verify(expenseDAO, Mockito.never()).create(Mockito.any());
    }

    @Test
    void testPostExpense_CheckInvalidEmail() throws BackendException
    {
        ExpenseInsertDto dto = validDto();
        dto.setEmail(null);

        Response resp = expenseResource.postExpense(dto);

        //assertEquals(400, resp.getStatus());
        assertThrows(BackendException.class, () -> {
            expenseResource.postExpense(dto);
        });
        Mockito.verify(expenseDAO, Mockito.never()).create(Mockito.any());
    }
}