package md5.end.controller;

import md5.end.model.dto.request.RegisterForm;
import md5.end.model.entity.User;
import md5.end.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<List<User>> findAll() {
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>("Id not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    public ResponseEntity<?> searchByName(@RequestParam(name = "q") String name) {
        if(userService.searchByName(name).isEmpty()){
            return new ResponseEntity<>("No result found.", HttpStatus.OK);
        }
        return new ResponseEntity<>(userService.searchByName(name), HttpStatus.OK);
    }
    @PutMapping("/changeStatus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>("Id not found", HttpStatus.NOT_FOUND);
        }
        if (userOptional.get().getRoles().size() > 1) {
            return new ResponseEntity<>("Can't change status", HttpStatus.BAD_REQUEST);
        }
        userService.changeStatus(userOptional.get());
        return new ResponseEntity<>("Change status successfully", HttpStatus.OK);
    }
}
