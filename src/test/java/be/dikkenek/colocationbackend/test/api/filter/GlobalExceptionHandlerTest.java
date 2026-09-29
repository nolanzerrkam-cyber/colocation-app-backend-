package be.dikkenek.colocationbackend.test.api.filter;

import be.dikkenek.colocationbackend.filter.GlobalExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        responseWriter = new StringWriter();
        lenient().when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    @DisplayName("Devrait laisser passer la requête sans rien modifier si aucune exception n'est levée")
    void doFilter_WhenNoException_ShouldContinueNormally() throws Exception {
        exceptionHandler.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(response, never()).setStatus(anyInt());
    }

    @Test
    @DisplayName("Devrait retourner le statut 400 pour IllegalArgumentException")
    void doFilter_WhenIllegalArgumentException_ShouldReturn400() throws Exception {
        String errorMessage = "Paramètre invalide";
        doThrow(new IllegalArgumentException(errorMessage))
                .when(filterChain).doFilter(request, response);

        exceptionHandler.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        assertEquals(errorMessage, responseWriter.toString());
    }

    @Test
    @DisplayName("Devrait retourner le statut 403 pour SecurityException")
    void doFilter_WhenSecurityException_ShouldReturn403() throws Exception {
        String errorMessage = "Accès non autorisé";
        doThrow(new SecurityException(errorMessage))
                .when(filterChain).doFilter(request, response);

        exceptionHandler.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        assertEquals(errorMessage, responseWriter.toString());
    }

    @Test
    @DisplayName("Devrait retourner le statut 500 pour NullPointerException")
    void doFilter_WhenNullPointerException_ShouldReturn500() throws Exception {
        String errorMessage = "Pointeur nul détecté";
        doThrow(new NullPointerException(errorMessage))
                .when(filterChain).doFilter(request, response);

        exceptionHandler.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertEquals(errorMessage, responseWriter.toString());
    }

    @Test
    @DisplayName("Devrait retourner le statut 400 pour RuntimeException générique")
    void doFilter_WhenRuntimeException_ShouldReturn400() throws Exception {
        String errorMessage = "Erreur de traitement";
        doThrow(new RuntimeException(errorMessage))
                .when(filterChain).doFilter(request, response);

        exceptionHandler.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        assertEquals(errorMessage, responseWriter.toString());
    }

    @Test
    @DisplayName("Devrait retourner le statut 500 pour une Exception contrôlée (ex: ServletException)")
    void doFilter_WhenCheckedException_ShouldReturnDefault500() throws Exception {
        doThrow(new ServletException("Erreur servlet"))
                .when(filterChain).doFilter(request, response);

        exceptionHandler.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        assertEquals("Internal server error", responseWriter.toString());
    }

    @Test
    @DisplayName("Ne devrait rien écrire si la réponse est déjà commise (isCommitted = true)")
    void doFilter_WhenResponseIsCommitted_ShouldDoNothing() throws Exception {
        when(response.isCommitted()).thenReturn(true);
        doThrow(new IllegalArgumentException("Erreur"))
                .when(filterChain).doFilter(request, response);

        exceptionHandler.doFilter(request, response, filterChain);

        verify(response, never()).setStatus(anyInt());
        assertEquals("", responseWriter.toString());
    }
}