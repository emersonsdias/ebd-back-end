package br.com.emersondias.ebd.repositories;

import br.com.emersondias.ebd.entities.User;
import br.com.emersondias.ebd.entities.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String username);

    List<User> findByRolesInOrderByName(List<UserRole> types);

}
