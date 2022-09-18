package academy.mindswap.springSecurity.services;

import academy.mindswap.springSecurity.command.CreateUserDto;
import academy.mindswap.springSecurity.command.UserDto;
import academy.mindswap.springSecurity.models.Role;
import academy.mindswap.springSecurity.models.User;
import academy.mindswap.springSecurity.utils.RoleTypes;

import java.util.List;

public interface UserService {
    UserDto registerUser(CreateUserDto user);
    Role saveRole(Role role);
    User addRoleToUser(String username, String roleType);
    UserDto findByUserName(String username); //assuming every username must be different
    List<UserDto> getUsers();

    boolean checkIfUserHasRole(User user, Role role);

    void createRoles();

    void createOwner();

    void deleteUser(String username);

    void deleteRole(String roleType);
}
