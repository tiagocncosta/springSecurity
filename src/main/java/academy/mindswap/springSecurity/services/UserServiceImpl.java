package academy.mindswap.springSecurity.services;

import academy.mindswap.springSecurity.command.CreateUserDto;
import academy.mindswap.springSecurity.command.UserConverter;
import academy.mindswap.springSecurity.command.UserDto;
import academy.mindswap.springSecurity.models.Role;
import academy.mindswap.springSecurity.models.User;
import academy.mindswap.springSecurity.repositories.RoleRepository;
import academy.mindswap.springSecurity.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

;

@Service
@RequiredArgsConstructor//com isto não necessito de iniciar um construtor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDto registerUser(CreateUserDto user) {
        if(userRepository.findByUsername(user.getUsername()) != null){
        throw new RuntimeException("User already exists"); //criar exceção para isto
        }
        log.info("Saving new user {} to database.", user.getUsername());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return UserConverter
                .convertEntityToUserDto(userRepository.
                        save(addRoleToUser(userRepository.
                                save(UserConverter.
                                        convertCreateUserDtoToEntity(user)).getName(), "USER")));
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getRoleType());
        return roleRepository.save(role);
    }

    @Override
    public User addRoleToUser(String username, String roleType) {
        log.info("adding role {} to user {}", roleType, username);
    User user = userRepository.findByUsername(username);
    Role role = roleRepository.findByRoleType(roleType);
    if(checkIfUserHasRole(user, role)){
        return user;
    }
    user.getRoles().add(role);
    return userRepository.save(user);//supostamente como tenho o transactional nao preciso de fazer o save novamente.

    }

    @Override
    public boolean checkIfUserHasRole(User user, Role role) {
        for (int i = 0; i < user.getRoles().size(); i++) {
            if(user.getRoles().get(i).equals(role)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void createRoles() {
        List<Role> roles = roleRepository.findAll();

        if (roles.isEmpty()) {
            saveRole(new Role(null, "OWNER"));
            saveRole(new Role(null, "ADMIN"));
            saveRole(new Role(null, "USER"));
        }
    }


    @Override
    public void createOwner() {
            Optional<User> user = Optional.ofNullable(userRepository.findByUsername("owner"));

            if (user.isEmpty()) {
                User newUser =
                        User.builder()
                                .name("owner")
                                .username("owner")
                                .roles(new ArrayList<>())
                                .password(passwordEncoder.encode("owner123"))
                                .build();
                userRepository.save(newUser);
                addRoleToUser("owner", "OWNER");

        }

    }

    @Override
    public void deleteUser(String username) {
        userRepository.delete(userRepository.findByUsername(username));
    }

    @Override
    public void deleteRole(String roleType) {
        roleRepository.delete(roleRepository.findByRoleType(roleType));
    }


    @Override
    public UserDto findByUserName(String username) {
        log.info("fetching {}", username );
        return UserConverter.convertEntityToUserDto(userRepository.findByUsername(username));
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("fetching all users");
        return userRepository.findAll().stream().map(UserConverter::convertEntityToUserDto).collect(Collectors.toList());
    }




}
