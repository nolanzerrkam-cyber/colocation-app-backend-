package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.FlatshareDao;
import be.dikkenek.colocationbackend.dto.ExpenseInsertDto;
import be.dikkenek.colocationbackend.dto.GetExpenseDto;
import be.dikkenek.colocationbackend.dto.ListExpenseResponseDto;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import be.dikkenek.colocationbackend.entity.FlatshareEntity;
import be.dikkenek.colocationbackend.entity.RoommateEntity;
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
    private FlatshareDao flatshareDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllExpenses(@QueryParam("email") String email) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);

        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        List<ExpenseEntity> expenses = flatshareEntity.getExpenses();
        float totalExpenses = flatshareEntity.getTotalExpenses();

        if (email != null) {
            RoommateEntity roommateEntity = flatshareEntity.getRoommate(email);
            if (roommateEntity == null)
                return Response.status(Response.Status.NOT_FOUND).entity("Roommate not found").build();

            expenses = flatshareEntity.getExpenses().stream().filter(e -> e.getRoommate().equals(roommateEntity)).collect(Collectors.toList());
            totalExpenses = roommateEntity.getTotalExpenses();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        List<GetExpenseDto> expenseDtos = expenses.stream().map(entity -> new GetExpenseDto(
                entity.getId_expense(),
                entity.getLabel(),
                entity.getPrice(),
                entity.getDate() != null ? formatter.format(entity.getDate()) : null,
                entity.getRoommate().getFirstname(),
                entity.getRoommate().getLastname(),
                entity.getRoommate().getEmail()
        )).collect(Collectors.toList());

        ListExpenseResponseDto responseDto = new ListExpenseResponseDto(totalExpenses, expenseDtos);

        return Response.status(200).entity(responseDto).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteExpense(@PathParam("id") int id) throws BackendException {
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);

        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        ExpenseEntity expense = flatshareEntity.getExpense(id);

        if (expense == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("expense not found")
                    .build();
        }

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
        FlatshareEntity flatshareEntity = FlatshareEntity.get(flatshareDao);

        if (flatshareEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Flatshare not found").build();

        RoommateEntity roommateEntity = flatshareEntity.getRoommate(dto.getEmail());
        if (roommateEntity == null)
            return Response.status(Response.Status.NOT_FOUND).entity("Roommate not found").build();

        // ExpenseEntity constructor automatically adds the expense in the roommate's flatshare
        new ExpenseEntity(dto.getLabel(), dto.getPrice(), roommateEntity);

        if (flatshareEntity.update(flatshareDao)) {
            return Response.status(Response.Status.CREATED).entity("The expense has been created !").build();
        }

        return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Sorry, we couldn't create your expense.").build();
    }
}
