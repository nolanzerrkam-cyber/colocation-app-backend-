package be.dikkenek.colocationbackend.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class GlobalExceptionHandler implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        try
        {
            chain.doFilter((HttpServletRequest) request, (HttpServletResponse) response);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            handleException((HttpServletRequest) request, (HttpServletResponse) response, e);
        }

    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException
    {

        if (response.isCommitted()) {
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int statusCode;
        String message;



        switch (e)
        {
            case IllegalArgumentException ex ->
            {
                statusCode = HttpServletResponse.SC_BAD_REQUEST;
                message = ex.getMessage();
            }

            case SecurityException ex -> {
                statusCode = HttpServletResponse.SC_FORBIDDEN;
                message = ex.getMessage();
            }

            case NullPointerException ex -> {
                statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                message = ex.getMessage();
            }

            case RuntimeException ex ->
            {
                statusCode = HttpServletResponse.SC_BAD_REQUEST;
                message = ex.getMessage();
            }

            default -> {
                statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                message = "Internal server error";
            }
        }

        response.setStatus(statusCode);

        response.getWriter().write(message);
        response.getWriter().flush();
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
