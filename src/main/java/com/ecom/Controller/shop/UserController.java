package com.ecom.Controller.shop;

import com.ecom.Entity.User;
import com.ecom.Exception.BadRequestException;
import com.ecom.Model.dto.UserDTO;
import com.ecom.Model.mapper.UserMapper;
import com.ecom.Model.request.*;
import com.ecom.Security.CustomUserDetails;
import com.ecom.Security.JwtTokenUtil;
import com.ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Objects;

import static com.ecom.config.Contant.MAX_AGE_COOKIE;

@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/users")
    public ResponseEntity<Object> getListUsers() {
        List<UserDTO> userDTOS = userService.getListUsers();
        return ResponseEntity.ok(userDTOS);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email)
    {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(UserMapper.toUserDTO(user));
    }

    @PostMapping("/api/admin/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        User user = userService.createUser(createUserRequest);
        return ResponseEntity.ok(UserMapper.toUserDTO(user));
    }

    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@Valid @RequestBody CreateUserRequest createUserRequest, HttpServletResponse response) {
        //Create user
        User user = userService.createUser(createUserRequest);

        //Gen token
        UserDetails principal = new CustomUserDetails(user);
        String token = jwtTokenUtil.generateToken(principal);

        //Add token on cookie to login
//        Cookie cookie = new Cookie("JWT_TOKEN", token);
//        cookie.setMaxAge(MAX_AGE_COOKIE);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//        return ResponseEntity.ok(UserMapper.toUserDTO(user));
        return ResponseEntity.ok(token);
    }

    @PostMapping("/api/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        //Authenticate
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
            //Gen token
            String token = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());

//            Add token to cookie to login
//            Cookie cookie = new Cookie("JWT_TOKEN", token);
//            cookie.setMaxAge(MAX_AGE_COOKIE);
//            cookie.setPath("/");
//            response.addCookie(cookie)
//            return ResponseEntity.ok(UserMapper.toUserDTO(((CustomUserDetails) authentication.getPrincipal()).getUser()));
//            UserDTO userDto = UserMapper.toUserDTO(((CustomUserDetails) authentication.getPrincipal()).getUser());
            return ResponseEntity.ok(token);
        } catch (Exception ex) {
            throw new BadRequestException("Email hoặc mật khẩu không chính xác!");

        }
    }

    @PostMapping("/api/change-password")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordRequest passwordReq) {
        User user = userService.findByEmail(passwordReq.getEmail());
        if(Objects.isNull(user)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.changePassword(user, passwordReq);
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @PostMapping("/api/forgot/change-password")
    public ResponseEntity<HttpStatus> changeForgotPassword(@Valid @RequestBody ForgotPassword forgotPassword) {
        User user = userService.findByEmail(forgotPassword.getEmail());
        if(Objects.isNull(user)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.changeForgotPassword(user, forgotPassword.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/api/update-profile")
    public ResponseEntity<Object> updateProfile(@Valid @RequestBody UpdateProfileRequest profileReq) {
        User user = userService.findByEmail(profileReq.getEmail());

        user = userService.updateProfile(user, profileReq);
        UserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok("Cập nhật thành công");
    }
}
