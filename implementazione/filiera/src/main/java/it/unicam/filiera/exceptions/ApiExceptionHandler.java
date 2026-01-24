package it.unicam.filiera.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail notFound(NotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req, null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail badRequest(BadRequestException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req, null);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail forbidden(ForbiddenException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage(), req, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail illegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail methodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.computeIfAbsent(fe.getField(), k -> new ArrayList<>()).add(
                    fe.getDefaultMessage() == null ? "Valore non valido" : fe.getDefaultMessage()
            );
        }
        ProblemDetail pd = build(HttpStatus.BAD_REQUEST, "Validazione fallita", req, null);
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail constraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, List<String>> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.groupingBy(
                        v -> v.getPropertyPath() == null ? "param" : v.getPropertyPath().toString(),
                        LinkedHashMap::new,
                        Collectors.mapping(v -> v.getMessage() == null ? "Valore non valido" : v.getMessage(), Collectors.toList())
                ));
        ProblemDetail pd = build(HttpStatus.BAD_REQUEST, "Validazione fallita", req, null);
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail notReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "JSON non valido o corpo richiesta mancante", req, null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail typeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        String msg = "Parametro non valido: " + ex.getName();
        return build(HttpStatus.BAD_REQUEST, msg, req, Map.of(
                "param", ex.getName(),
                "value", ex.getValue(),
                "expectedType", ex.getRequiredType() == null ? null : ex.getRequiredType().getSimpleName()
        ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail missingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Parametro mancante: " + ex.getParameterName(), req, Map.of(
                "param", ex.getParameterName(),
                "expectedType", ex.getParameterType()
        ));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail methodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        ProblemDetail pd = build(HttpStatus.METHOD_NOT_ALLOWED, "Metodo non consentito", req, null);
        pd.setProperty("method", ex.getMethod());
        pd.setProperty("supported", ex.getSupportedHttpMethods() == null ? null : ex.getSupportedHttpMethods().toString());
        return pd;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ProblemDetail mediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        ProblemDetail pd = build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Content-Type non supportato", req, null);
        pd.setProperty("contentType", ex.getContentType() == null ? null : ex.getContentType().toString());
        pd.setProperty("supported", ex.getSupportedMediaTypes() == null ? null : ex.getSupportedMediaTypes().toString());
        return pd;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail accessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Accesso negato", req, null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail authentication(AuthenticationException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Autenticazione richiesta o non valida", req, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail dataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        String root = mostSpecificMessage(ex);
        ProblemDetail pd = build(HttpStatus.CONFLICT, "Operazione non valida: dati duplicati o vincoli DB violati.", req, null);
        if (root != null && !root.isBlank()) {
            pd.setProperty("dbCause", root);
        }
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail generic(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Errore interno del server.", req, null);
    }

    private ProblemDetail build(HttpStatusCode status, String detail, HttpServletRequest req, Map<String, Object> extra) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(status instanceof HttpStatus hs ? hs.getReasonPhrase() : "Error");
        pd.setInstance(java.net.URI.create(req.getRequestURI()));
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
        if (extra != null) {
            for (Map.Entry<String, Object> e : extra.entrySet()) {
                pd.setProperty(e.getKey(), e.getValue());
            }
        }
        return pd;
    }

    private String mostSpecificMessage(Throwable t) {
        Throwable cur = t;
        Throwable next;
        while ((next = cur.getCause()) != null) {
            cur = next;
        }
        String msg = cur.getMessage();
        if (msg == null) return null;
        String cleaned = msg.replaceAll("\\s+", " ").trim();
        return cleaned.length() > 400 ? cleaned.substring(0, 400) : cleaned;
    }
}
