package be.dikkenek.colocationbackend.test.api.expense;

import be.dikkenek.colocationbackend.dao.ExpenseDAO;
import be.dikkenek.colocationbackend.dto.ExpenseInsertDTO;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import be.dikkenek.colocationbackend.resource.ExpenseRessource;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExpenseResourceTest {

    @Mock
    // creating a false dao
    private ExpenseDAO expenseDAO;

    @InjectMocks
    // there im creation a object, to test later the false dao
    private ExpenseRessource expenseResource;

    private ExpenseInsertDTO validDto() {
        ExpenseInsertDTO dto = new ExpenseInsertDTO();
        dto.setPrice(new BigDecimal("24.50"));
        dto.setLibele("Courses");
        dto.setEmail("test@test.com");
        dto.setDateExpense(new Date());
        return dto;
    }

    @Test
    void testPostExpense_Success()
    {
        // when the DAO is created, it has to return true (to do a illusion of insertion into oracle)
        Mockito.when(expenseDAO.create(Mockito.any(ExpenseEntity.class))).thenReturn(true);

        // Im calling the postExpense to build the ExpenseEntity , it will do the @Inject from the ressource of expenseDAO (calling the false DAO) and than return true (normally)
        // Be careful that the expenseDAO here and in ExpenseRessource is the same, each field is a reference
        Response response = expenseResource.postExpense(validDto());

        // We verify if the ressource did successfully the creation, exactly one time (because we don't need more)
        assertEquals(201, response.getStatus());
        Mockito.verify(expenseDAO, Mockito.times(1)).create(Mockito.any(ExpenseEntity.class));
    }

    @Test
    void testPostExpense_DaoFails() {
        Mockito.when(expenseDAO.create(Mockito.any(ExpenseEntity.class))).thenReturn(false);

        Response response = expenseResource.postExpense(validDto());

        assertEquals(503, response.getStatus());
    }

    @Test
    void testPostExpense_NullBody() {
        Response response = expenseResource.postExpense(null);

        assertEquals(400, response.getStatus());
        Mockito.verify(expenseDAO, Mockito.never()).create(Mockito.any());
    }

    @Test
    void testPostExpense_NegativePrice() {
        ExpenseInsertDTO dto = validDto();
        dto.setPrice(new BigDecimal("-5"));

        Response response = expenseResource.postExpense(dto);

        assertEquals(400, response.getStatus());
        Mockito.verify(expenseDAO, Mockito.never()).create(Mockito.any());
    }
}