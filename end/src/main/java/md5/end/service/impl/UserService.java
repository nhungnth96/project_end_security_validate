package md5.end.service.impl;


import md5.end.model.dto.request.RegisterForm;
import md5.end.model.entity.Role;
import md5.end.model.entity.RoleName;
import md5.end.model.entity.User;
import md5.end.repository.IUserRepository;
import md5.end.service.IRoleService;
import md5.end.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.security.auth.login.LoginException;
import java.time.LocalDate;
import java.util.*;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private PasswordEncoder encoder;
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByTel(String tel) {
        return userRepository.findByTel(tel);
    }

    @Override
    public List<User> searchByName(String name) {
        return userRepository.searchByName(name);
    }

    @Override
    public User save(RegisterForm registerDto) throws LoginException {
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new LoginException("Username is existed");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new LoginException("Email is existed");
        }
        if(userRepository.existsByTel(registerDto.getTel())){
            throw new LoginException("Tel is existed");
        }
        Set<Role> roles = new HashSet<>();
        if(registerDto.getRoles()==null||registerDto.getRoles().isEmpty()){
            roles.add(roleService.findByRoleName(RoleName.ROLE_BUYER));
        } else {
            for (String role : registerDto.getRoles()) {
                if(!(role.equalsIgnoreCase("admin")|| role.equalsIgnoreCase("seller")||role.equalsIgnoreCase("buyer"))){
                    throw new LoginException("Case role name is wrong");
                }
                switch (role) {
                    case "admin":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_ADMIN));
                    case "seller":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_SELLER));
                    case "buyer":
                        roles.add(roleService.findByRoleName(RoleName.ROLE_BUYER));
                }
            }

        }
        return userRepository.save( User.builder()
                .name(registerDto.getName())
                .username(registerDto.getUsername())
                .password(encoder.encode(registerDto.getPassword()))
                .email(registerDto.getEmail())
                .tel(registerDto.getTel())
                .creationDate(LocalDate.now().toString())
                .avatar("https://t4.ftcdn.net/jpg/05/49/98/39/360_F_549983970_bRCkYfk0P6PP5fKbMhZMIb07mCJ6esXL.jpg")
                .status(true)
                .roles(roles)
                .build());
    }

    @Override
    public void changeStatus(User user) {
        user.setStatus(!user.isStatus());
        userRepository.save(user);
    }
}
