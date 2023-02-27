package pl.kolo.Spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MockAdvice {
    @ResponseBody
    @ExceptionHandler(MockException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String MockExceptionHandler(MockException mockException){
        return mockException.getMessage();
    }
}
