package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.services;

import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.*;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface UserService {
    Page<UserDTO> list(Pageable pageable);
    UserUpdateDTO getForEdit(Long id);
    void create(UserCreateDTO dto);
    void update(UserUpdateDTO dto, Set<Role> roles);
    void delete(Long id);
    UserDetailDTO getDetail(Long id);
    List<UserDTO> listAll();
}
