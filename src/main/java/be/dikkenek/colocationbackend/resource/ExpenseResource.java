package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.ExpenseDao;
import be.dikkenek.colocationbackend.dto.GetExpenseDto;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Path("/expense")
public class ExpenseResource {
    @Inject
    private ExpenseDao expenseDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllExpenses() {
        List<ExpenseEntity> expenses = ExpenseEntity.findAll(expenseDao);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        List<GetExpenseDto> expenseDtos = expenses.stream().map(entity -> new GetExpenseDto(
                entity.getLibele(),
                entity.getPrice(),
                entity.getDate() != null ? formatter.format(entity.getDate()) : null,
                entity.getColoc() != null ? entity.getColoc().getEmail() : null
        )).collect(Collectors.toList());

        return Response.status(200).entity(expenseDtos).build();
    }
}
