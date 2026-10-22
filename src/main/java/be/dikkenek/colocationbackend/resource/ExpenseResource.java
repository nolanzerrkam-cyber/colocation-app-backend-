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
import java.util.Optional;
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
    public Response getAllExpenses() throws BackendException {
        List<ExpenseEntity> expenses = ExpenseEntity.findAll(expenseDao);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        List<GetExpenseDto> expenseDtos = expenses.stream().map(entity -> new GetExpenseDto(
                entity.getId(),
                entity.getLabel(),
                entity.getPrice(),
                entity.getDate() != null ? formatter.format(entity.getDate()) : null,
                entity.getRoomate() != null ? entity.getRoomate().getFirstname() : null,
                entity.getRoomate() != null ? entity.getRoomate().getLastname() : null,
                entity.getRoomate() != null ? entity.getRoomate().getEmail() : null
        )).collect(Collectors.toList());

        return Response.status(200).entity(expenseDtos).build();
    }
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteExpense(@PathParam("id") int id) throws BackendException {
        ExpenseEntity expense = ExpenseEntity.get(expenseDao, id);

        if (expense == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("expense not found")
                    .build();
        }

        boolean isDeleted = expense.delete(expenseDao);
        if (isDeleted) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Failed to delete expense 3")
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postExpense(ExpenseInsertDto dto) throws BackendException
    {
        UserEntity user = UserEntity.get(userDao, dto.getEmail());

        if (!(user instanceof RoommateEntity))
        {
            return Response.status(Response.Status.BAD_REQUEST).entity("Only roommates can add expenses !").build();
        }

        ExpenseEntity expense = new ExpenseEntity(dto.getLabel(), dto.getPrice(), (RoommateEntity) user);

        if (expense.create(expenseDao))
        {
            return Response.status(Response.Status.CREATED).entity("The expense has been created !").build();
        }
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Sorry, we couldn't create your expense.").build();
    }
}
