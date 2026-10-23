package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.ExpenseDao;
import be.dikkenek.colocationbackend.dao.UserDao;
import be.dikkenek.colocationbackend.dto.ExpenseInsertDto;
import be.dikkenek.colocationbackend.dto.GetExpenseDto;
import be.dikkenek.colocationbackend.dto.ListExpenseResponseDto;
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

        List<RoommateEntity> roommates = RoommateEntity.findAll(userDao);
        float totalPrice = RoommateEntity.getTotalExpense(roommates);
        List<ExpenseEntity> expenses = roommates.stream()
                .flatMap(roommate -> roommate.getExpenses().stream())
                .collect(Collectors.toList());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        List<GetExpenseDto> expenseDtos = expenses.stream().map(entity -> new GetExpenseDto(
                entity.getId(),
                entity.getLabel(),
                entity.getPrice(),
                entity.getDate() != null ? formatter.format(entity.getDate()) : null,
                entity.getRoomate().getFirstname(),
                entity.getRoomate().getLastname(),
                entity.getRoomate().getEmail()
        )).collect(Collectors.toList());
        ListExpenseResponseDto responseDto = new ListExpenseResponseDto(totalPrice,expenseDtos);

        return Response.status(200).entity(responseDto).build();
    }
    @GET
    @Path("/total")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPricePerRoommate(@QueryParam("email") String email) throws BackendException {
        RoommateEntity roommate = RoommateEntity.get(userDao, email);
        float priceRoommate = roommate.getPriceRoomate();

        return Response.status(200).entity(priceRoommate).build();
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
        if (dto == null || dto.getEmail() == null ||dto.getEmail().trim().isEmpty())
        {
            return Response.status(Response.Status.BAD_REQUEST)
                    .build();
        }

        // Optional is used to handle two types of values : a simple value, and a null value. I have to use it, beacause userDao.get(email)
        // could say that the email dosen't exist(meaning nothing get returned !), and with Optional,
        // I can receive a null value safely, without an exception being risen.
        Optional<UserEntity> user = userDao.get(dto.getEmail());
        // to check if we don't have a user or if the user is an instance of roomateEntity (meaning its a type roomateEntity.)
        if(!user.isPresent() || !(user.get() instanceof RoommateEntity))
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        ExpenseEntity exp;
        try
        {
            exp = new ExpenseEntity(dto.getLibele(), dto.getPrice(), (RoommateEntity) user.get());
        }
        catch (IllegalArgumentException ie)
        {
            return Response.status(Response.Status.BAD_REQUEST)
                    .build();
        }

        if(exp.create(expenseDao))
        {
            return Response.status(Response.Status.CREATED)
                    .build();
        }
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .build();
    }
}
