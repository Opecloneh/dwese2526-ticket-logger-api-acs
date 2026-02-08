package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.repositories;

import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Region;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findAllByIdIn(Set<Long> ids);

    Optional<Role> findByName(String name);
}
