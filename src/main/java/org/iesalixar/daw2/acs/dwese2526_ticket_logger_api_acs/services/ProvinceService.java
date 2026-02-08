package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.services;

import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.ProvinceDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.ProvinceDetailDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.ProvinceUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProvinceService {
    Page<ProvinceDTO> list(Pageable pageable);
    ProvinceUpdateDTO getForEdit(Long id);
    void create(ProvinceCreateDTO dto);
    void update(ProvinceUpdateDTO dto);
    void delete(Long id);
    ProvinceDetailDTO getDetail(Long id);

    List<ProvinceDTO> listAll();

    Object listRegionsForSelect();
}
