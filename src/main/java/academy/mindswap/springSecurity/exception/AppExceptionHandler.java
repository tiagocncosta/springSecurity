package academy.mindswap.springSecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {TeacherNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
       // response.sendRedirect("/person/1");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
