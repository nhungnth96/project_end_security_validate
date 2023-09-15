package md5.end.service;

import md5.end.model.dto.request.RegisterForm;
import md5.end.model.entity.User;
import org.springframework.data.jpa.repository.Query;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User save(RegisterForm registerDto) throws LoginException;
    void changeStatus(User user);
    Optional<User> findByEmail (String email);
    Optional<User> findByTel (String tel);
    List<User> searchByName(String name);
}
