package everymeal.server.global.exception;

import static everymeal.server.global.exception.ExceptionList.INVALID_REQUEST;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import everymeal.server.global.dto.response.ApplicationErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT = "Class : {}, CODE : {}, Message : {}";
    private static final String INTERNAL_SERVER_ERROR_CODE = "S0001";

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity applicationException(ApplicationException e) {
        String errorCode = e.getErrorCode();
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), errorCode, e.getMessage());
        return new ResponseEntity<>(
                ApplicationErrorResponse.error(errorCode, e.getMessage()), e.getHttpStatus());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ApplicationErrorResponse<Object> runtimeException(RuntimeException e) {
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage());
        return ApplicationErrorResponse.error(INTERNAL_SERVER_ERROR_CODE, "런타임 에러가 발생했습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApplicationErrorResponse<Object> validationException(MethodArgumentNotValidException e) {
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage());
        return ApplicationErrorResponse.error(
                "V0001", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ApplicationErrorResponse<Object> validationException(BindException e) {
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage());
        return ApplicationErrorResponse.error(
                "V0001", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청",
            content = @Content(schema = @Schema(implementation = ApplicationErrorResponse.class)))
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApplicationErrorResponse<Object> typeErrorException(HttpMessageNotReadableException e) {
        String errorCode = "T0001";
        String errorMessage = e.getMessage();
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage());
        if (e.getCause() instanceof InvalidFormatException) {
            errorCode = INVALID_REQUEST.getCODE();
            errorMessage = INVALID_REQUEST.getMESSAGE();
        }
        return ApplicationErrorResponse.error(errorCode, errorMessage);
    }

    @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content())
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApplicationErrorResponse<Object> runtimeException(Exception e) {
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                INTERNAL_SERVER_ERROR_CODE,
                e.getMessage());
        return ApplicationErrorResponse.error(INTERNAL_SERVER_ERROR_CODE, "런타임 에러가 발생했습니다.");
    }
}
