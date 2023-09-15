package md5.end.controller;

import md5.end.model.dto.request.LoginForm;
import md5.end.model.dto.request.RegisterForm;
import md5.end.model.dto.response.JwtResponse;
import md5.end.model.entity.User;
import md5.end.security.jwt.JwtProvider;
import md5.end.security.principal.UserPrincipal;
import md5.end.service.IUserService;
import md5.end.service.utils.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private IUserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MailService mailService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginForm loginDto) throws LoginException{
        Authentication authentication = null;
            try {
                // tạo đối tượng authentication thông qua username vs password
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDto.getUsername(),
                                loginDto.getPassword()
                        ));
            } catch (AuthenticationException e){
                    throw new LoginException("Username or password is incorrect!");
            }
            // tạo token và trả về cho người dùng
            String token = jwtProvider.generateToken(authentication);
            // lấy ra user principal
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return new ResponseEntity<>(JwtResponse.builder()
                .id(userPrincipal.getId())
                .token(token)
                .type("Bearer")
                .name(userPrincipal.getName())
                .username(userPrincipal.getUsername())
                .status(userPrincipal.isStatus())
                .roles(roles)
                .build(),HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterForm registerDto) throws LoginException, md5.end.exception.LoginException {
         User user = userService.save(registerDto);
         mailService.sendEmail(registerDto.getEmail(),
                 "Register successfully",
                 "Thanks "+registerDto.getName()+" !"+"\n"+
                 "Thank you for filling out our sign up form. We are glad that you joined us. For this reason, we are giving you a special offer. Here is the link to the coupon for you to exclusively use on our website."+"\n"+
                 "Have a nice day!");
         return new ResponseEntity<>("Register successfully",HttpStatus.CREATED);
    }
}
