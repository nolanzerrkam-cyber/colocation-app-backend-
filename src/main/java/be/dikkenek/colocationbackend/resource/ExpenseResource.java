package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.ExpenseDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.ExpenseInsertDto;
import be.dikkenek.colocationbackend.dto.GetExpenseDto;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
import be.dikkenek.colocationbackend.entity.UserEntity;
import be.dikkenek.colocationbackend.exception.BackendException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Path("/expense")
public class ExpenseResource
{
    @Inject
    private ExpenseDao expenseDao;
    @Inject
    private UserDao userDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllExpenses()
    {
        List<ExpenseEntity> expenses = ExpenseEntity.findAll(expenseDao);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        List<GetExpenseDto> expenseDtos = expenses.stream().map(entity -> new GetExpenseDto(
                entity.getLabel(),
                entity.getPrice(),
                entity.getDate() != null ? formatter.format(entity.getDate()) : null,
                entity.getRoomate() != null ? entity.getRoomate().getEmail() : null
        )).collect(Collectors.toList());

        return Response.status(200).entity(expenseDtos).build();
    }

    // FIX : I passed directly to the dto for the object user : I fixed it : now I pass though the static
    // method to respect the object rules.
    // DELETED : the Optional type, because now its useless
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postExpense(ExpenseInsertDto dto) {
        if (dto == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try {
            UserEntity user = UserEntity.get(userDao, dto.getEmail());
            if (!(user instanceof RoommateEntity))
            {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            ExpenseEntity expense = new ExpenseEntity(dto.getLibele(), dto.getPrice(), (RoommateEntity) user);

            if (expense.create(expenseDao))
            {
                return Response.status(Response.Status.CREATED).build();
            }
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
        catch (IllegalArgumentException e)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        catch (BackendException backendException)
        {
            return Response.status(backendException.getStatus()).entity(backendException.getMessage()).build();
        }
    }
}
