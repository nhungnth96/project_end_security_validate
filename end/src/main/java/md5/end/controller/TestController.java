package md5.end.controller;

import md5.end.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import md5.end.service.IUserService;

import java.util.List;

@RestController
@RequestMapping("api/v4/test")
@CrossOrigin("*")
public class TestController {
    @Autowired
    private IUserService userService;
    @GetMapping("/users")
//    @PreAuthorize("has('ROLE_ADMIN')")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
}
