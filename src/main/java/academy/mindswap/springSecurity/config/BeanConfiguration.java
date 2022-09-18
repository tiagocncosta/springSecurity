package academy.mindswap.springSecurity.config;

import academy.mindswap.springSecurity.command.CreateUserDto;
import academy.mindswap.springSecurity.models.Role;
import academy.mindswap.springSecurity.services.UserService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static academy.mindswap.springSecurity.utils.RoleTypes.*;

@Configuration
public class BeanConfiguration {


    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.createRoles();
            userService.createOwner();
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
