package uz.pdp.online.m6l5apphr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.m6l5apphr.entity.Role;
import uz.pdp.online.m6l5apphr.entity.enums.RoleName;

import java.util.List;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findByRoleName(RoleName roleName);


}
