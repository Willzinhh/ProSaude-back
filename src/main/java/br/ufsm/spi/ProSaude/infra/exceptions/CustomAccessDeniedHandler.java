package br.ufsm.spi.ProSaude.infra.exceptions;// CustomAccessDeniedHandler.java

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        // DTO de erro simplificado para o 403
        var erroResponse = new ErroResponse(
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado. Você não possui as permissões necessárias para este recurso."
        );

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Escreve o JSON no corpo da resposta
        new ObjectMapper().writeValue(response.getWriter(), erroResponse);
    }

    // Record (DTO) simples para o corpo da resposta de erro
    private record ErroResponse(int status, String message) {}
}