package br.ufsm.spi.ProSaude.infra.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity tratarErro404() {
        var erro404 = new DadosErroSimples("Recurso não Encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro404);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErroDadosInvalidos(MethodArgumentNotValidException ex) {
        List<FieldError> errors = ex.getFieldErrors();
        List<DadosErroValidacao> dados = new ArrayList<>();
        for (FieldError fe : errors) {
            dados.add(new DadosErroValidacao(fe.getField(), fe.getDefaultMessage()));
        }
        return ResponseEntity.badRequest().body(dados);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity tratarErro403Forbidden(AccessDeniedException ex) {
        var erro403 = new DadosErroSimples("Acesso negado. Você não tem permissão para este recurso.");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro403);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> tratarErroAutenticacao() {
        var erroAuth = new DadosErroSimples("Usuário ou senha inválidos.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erroAuth);
    }

    // Caso o seu service lance alguma outra exceção geral de segurança no login
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> tratarErroGeralAutenticacao(AuthenticationException ex) {
        var erroAuth = new DadosErroSimples("Falha na autenticação: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erroAuth);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> tratarErroChaveEstrangeira(DataIntegrityViolationException ex) {
        // Criamos uma mensagem amigável explicando o motivo real do erro
        var erroSimples = new DadosErroSimples("Não é possível excluir esta turma porque existem alunos inscritos nela.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erroSimples); // Status 409 Conflict
    }

    private record DadosErroValidacao(String campo, String message) {
    }

    private record ErroResponse(int status, String mensagem) {
    }

    private record DadosErroSimples(String campo) {
    }



}
