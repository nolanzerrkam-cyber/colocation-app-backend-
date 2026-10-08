package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.ExpenseDao;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/expense")
public class ExpenseResource {
    @Inject
    private ExpenseDao expenseDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllExpenses() {
        List<ExpenseEntity> expenses = ExpenseEntity.findAll(expenseDao);
        return Response.status(200).entity(expenses).build();
    }
}
