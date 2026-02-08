package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.services;

import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.RegionCreateDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.RegionDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.RegionDetailDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.RegionUpdateDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RegionService {
    Page<RegionDTO> list(Pageable pageable);
    RegionUpdateDTO getForEdit(Long id);
    RegionDTO create(RegionCreateDTO dto);
    RegionDTO update(RegionUpdateDTO dto);
    void delete(Long id);
    RegionDetailDTO getDetail(Long id);

    List<RegionDTO> listAll();
    Region findById(Long id);
}
