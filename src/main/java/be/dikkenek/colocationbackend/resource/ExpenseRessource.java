package be.dikkenek.colocationbackend.resource;

import be.dikkenek.colocationbackend.dao.ExpenseDAO;
import be.dikkenek.colocationbackend.dto.ExpenseInsertDTO;
import be.dikkenek.colocationbackend.entity.ExpenseEntity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/expense")
public class ExpenseRessource
{
    @Inject
    private ExpenseDAO expenseDAO;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)

    public Response postExpense(ExpenseInsertDTO dto)
    {
        if (dto == null)
        {
            return Response.status(Response.Status.BAD_REQUEST)
                           .build();
        }

        ExpenseEntity exp;
        try
        {
            exp = new ExpenseEntity(dto.getLibele(), dto.getPrice(), dto.getDateExpense(), dto.getEmail());
        }
        catch (IllegalArgumentException ie)
        {
            return Response.status(Response.Status.BAD_REQUEST)
                           .build();
        }

        if(exp.create(expenseDAO))
        {
            return Response.status(Response.Status.CREATED)
                           .build();
        }
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                       .build();
    }
}
