package academy.mindswap.springSecurity.controllers;

import academy.mindswap.springSecurity.command.CreateUserDto;
import academy.mindswap.springSecurity.command.UserConverter;
import academy.mindswap.springSecurity.command.UserDto;
import academy.mindswap.springSecurity.models.Role;
import academy.mindswap.springSecurity.models.User;
import academy.mindswap.springSecurity.services.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static academy.mindswap.springSecurity.utils.PrintErrors.printErrors;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers(){
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/findByUsername/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok().body(userService.findByUserName(username));
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
          return printErrors(bindingResult);
        }
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        return new ResponseEntity<>(userService.saveRole(role), HttpStatus.CREATED);
    }

    @PutMapping("/role={name}/user={username}")
    public ResponseEntity<?> addRoleToUser(@PathVariable String name, @PathVariable String username){
        return new ResponseEntity<>(userService.addRoleToUser(username,name), HttpStatus.OK);
    }

    @DeleteMapping("/delete/user={username}")
    public void deleteUser(@PathVariable String username){
        if(username == null){
            throw new RuntimeException("You need to give a proper username");
        }

        userService.deleteUser(username);
    }


    @DeleteMapping("/delete/role={roleType}")
    public void deleteRole(@PathVariable String roleType){
        if(roleType == null){
            throw new RuntimeException("You need to give a proper username");
        }

        userService.deleteRole(roleType);
    }



    @GetMapping("/role/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
       String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); //secret é tipo a assinatura do nosso token, deveria ser guardada e encriptada num local seguro
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject(); //dá me username
                User user = UserConverter.convertUserDtoToEntity(userService.findByUserName(username));
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getRoleType).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);// devolve os tokens na resposta
            }
        } else {
            throw new RuntimeException("Refresh Token is missing!!");
        }
    }

}
