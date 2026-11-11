package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.ExpenseDao;
import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.ExpenseInsertDto;
import be.dikkenek.colocationbackend.dto.GetExpenseDto;
import be.dikkenek.colocationbackend.dto.ListExpenseResponseDto;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
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
public class ExpenseResource {
    @Inject
    private ExpenseDao expenseDao;
    @Inject
    private UserDao userDao;
    @Inject
    private FlatshareDao flatshareDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllExpenses(@QueryParam("email") String email) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);

        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        List<ExpenseEntity> expenses = flatshareEntity.getExpenses();

        if (email != null) {
            UserEntity userEntity = UserEntity.get(userDao, email);
            if (userEntity instanceof RoommateEntity) {
                RoommateEntity roommateEntity = (RoommateEntity) userEntity;
                expenses = flatshareEntity.getExpenses().stream().filter(e -> e.getRoommate().equals(roommateEntity)).collect(Collectors.toList());
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        List<GetExpenseDto> expenseDtos = expenses.stream().map(entity -> new GetExpenseDto(
                entity.getId(),
                entity.getLabel(),
                entity.getPrice(),
                entity.getDate() != null ? formatter.format(entity.getDate()) : null,
                entity.getRoommate().getFirstname(),
                entity.getRoommate().getLastname(),
                entity.getRoommate().getEmail()
        )).collect(Collectors.toList());

        ListExpenseResponseDto responseDto = new ListExpenseResponseDto(flatshareEntity.getTotalExpenses(), expenseDtos);

        return Response.status(200).entity(responseDto).build();
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

        FlatshareEntity flatshareEntity = expense.getRoommate().getFlatshare();
        flatshareEntity.removeExpense(expense);

        boolean isUpdated = flatshareEntity.update(flatshareDao);
        if (isUpdated) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Failed to delete expense 3")
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postExpense(ExpenseInsertDto dto) throws BackendException {
        UserEntity user = UserEntity.get(userDao, dto.getEmail());

        if (!(user instanceof RoommateEntity)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Only roommates can add expenses !").build();
        }

        RoommateEntity roommateEntity = (RoommateEntity) user;

        // ExpenseEntity constructor automatically adds the expense in the roommate's flatshare
        new ExpenseEntity(dto.getLabel(), dto.getPrice(), roommateEntity);

        if (roommateEntity.getFlatshare().update(flatshareDao)) {
            return Response.status(Response.Status.CREATED).entity("The expense has been created !").build();
        }

        return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Sorry, we couldn't create your expense.").build();
    }
}
