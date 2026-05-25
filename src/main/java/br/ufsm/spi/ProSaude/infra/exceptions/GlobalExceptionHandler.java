package br.ufsm.spi.ProSaude.infra.exceptions;// br.cspi.infra.exceptions.GlobalExceptionHandler.java

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Regex para extrair a mensagem de erro específica do PostgreSQL
    private static final Pattern POSTGRES_UNIQUE_CONSTRAINT_PATTERN =
            Pattern.compile("Detalhe: Chave \\((.*?)\\)=\\((.*?)\\) j\\u00E1 existe\\.");

    /**
     * Trata a exceção de violação de integridade de dados, comum em chaves duplicadas.
     * Retorna 409 CONFLICT com uma mensagem detalhada.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        // Tentativa de extrair a causa raiz (geralmente uma SQLException)
        Throwable rootCause = ex.getRootCause();
        String detailedMessage = "Erro de integridade de dados.";

        if (rootCause instanceof SQLException sqlEx) {
            String message = sqlEx.getMessage();

            // Tenta analisar a mensagem de erro específica do PostgreSQL
            Matcher matcher = POSTGRES_UNIQUE_CONSTRAINT_PATTERN.matcher(message);

            if (matcher.find()) {
                String campo = matcher.group(1); // Ex: email
                String valor = matcher.group(2); // Ex: Will@gmail.com

                detailedMessage = String.format(
                        "O valor '%s' para o campo '%s' já está em uso e deve ser único.",
                        valor, campo
                );
            } else {
                detailedMessage = "Erro de integridade de dados: " + sqlEx.getSQLState();
            }
        } else {
            detailedMessage = "Erro no banco de dados. Tente novamente.";
        }

        var erroResponse = new ErroResponse(
                HttpStatus.CONFLICT.value(),
                detailedMessage
        );

        // Retorna 409 CONFLICT
        return new ResponseEntity<>(erroResponse, HttpStatus.CONFLICT);
    }

    // DTO de erro (use o mesmo que você definiu para os erros de segurança)
    private record ErroResponse(int status, String message) {
    }
}