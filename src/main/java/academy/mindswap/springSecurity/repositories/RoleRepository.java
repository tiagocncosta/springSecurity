package academy.mindswap.springSecurity.repositories;

import academy.mindswap.springSecurity.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleType(String roleType);
}

