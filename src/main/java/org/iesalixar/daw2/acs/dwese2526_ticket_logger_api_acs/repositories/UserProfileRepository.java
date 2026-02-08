package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.repositories;

import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Region;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
