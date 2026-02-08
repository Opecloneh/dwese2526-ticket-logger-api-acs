package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.services;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.UserCreateDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.UserDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.UserDetailDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.UserUpdateDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Role;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.User;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.mappers.UserMapper;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.repositories.RoleRepository;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    private static final int PASSWORD_EXPIRY_DAYS = 90;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<UserDTO> list(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::toDTO);
    }

    @Override
    public UserUpdateDTO getForEdit(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return UserMapper.toUpdateDTO(user);
    }

    @Override
    public void create(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("user", "email", dto.getEmail());
        }
        LocalDateTime now = LocalDateTime.now();

        dto.setLastPasswordChange(now);
        dto.setPasswordExpiresAt(now.plusDays(PASSWORD_EXPIRY_DAYS));

        User user = UserMapper.toEntity(dto);
        userRepository.save(user);
    }

    @Override
    public void update(UserUpdateDTO dto, Set<Role> roles) {
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())){
            throw new DuplicateResourceException("user", "email", dto.getEmail());
        }
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", dto.getId()));

        if (roles.size() != dto.getRoleIds().size()) {
            throw new ResourceNotFoundException("role", "ids", dto.getRoleIds());
        }

        UserMapper.copyToExistingEntity(dto, user, roles);

        if (dto.getPasswordHash() != null && !dto.getPasswordHash().isBlank()) {
            LocalDateTime now = LocalDateTime.now();
            // Aquí ya no codificamos la contraseña:
            user.setPasswordHash(dto.getPasswordHash());
            user.setLastPasswordChange(now);
            user.setPasswordExpiresAt(now.plusDays(PASSWORD_EXPIRY_DAYS));
        }

        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)){
            throw new ResourceNotFoundException("region", "id", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDetailDTO getDetail(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return UserMapper.toDetailDTO(user);
    }

    @Override
    public List<UserDTO> listAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }
}
