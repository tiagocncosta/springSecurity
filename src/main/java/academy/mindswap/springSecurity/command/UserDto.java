package academy.mindswap.springSecurity.command;

import academy.mindswap.springSecurity.models.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String username;
    private String password;
    private List<Role> roles;
}
