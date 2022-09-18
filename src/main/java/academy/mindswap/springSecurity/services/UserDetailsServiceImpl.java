package academy.mindswap.springSecurity.services;

import academy.mindswap.springSecurity.models.User;
import academy.mindswap.springSecurity.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override //é apartir deste método que o spring vai buscar/reconhecer os users aonde eles foram guardados
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user==null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found int the database");
        }else {
            log.info("User found in the database: {}", username);
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();//adicionar/criar as autorizações que do user para depois adicionar ao User da UserDetails
        user.getRoles()
                .forEach(role -> authorities
                        .add((new SimpleGrantedAuthority(role.getRoleType()))));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
