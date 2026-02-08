package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.repositories;

import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Province;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProvinceRepository extends JpaRepository<Province, Long> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);

}
