package md5.end.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.security.auth.login.LoginException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    // 401 chưa dc xác thực
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> loginFailed(LoginException loginException){
        return new ResponseEntity<>(loginException.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    // 403 chưa dc cấp quyền
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDenied(AccessDeniedException accessDeniedException){
        return new ResponseEntity<>("You don't have permission to access this page.", HttpStatus.FORBIDDEN);
    }
    // 404 link ko tồn tại
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> notFound(NoHandlerFoundException noHandlerFoundException){
        return new ResponseEntity<>("Ooops,page not found.", HttpStatus.NOT_FOUND);
    }
    // upload file
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> fileTooLarge(){
        return new ResponseEntity<>("File is too large, must be not over 10MB", HttpStatus.BAD_REQUEST);
    }
    // validate
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> invalidFieldRequest (MethodArgumentNotValidException ex){
        Map<String,String> err = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(c->err.put(c.getField(),c.getDefaultMessage()));
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }


//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(CustomerException.class)
//    public String existed(CustomerException e){
//        return e.getMessage();
//    }
}
