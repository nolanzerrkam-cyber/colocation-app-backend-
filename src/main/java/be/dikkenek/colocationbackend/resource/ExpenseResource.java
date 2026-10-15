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
