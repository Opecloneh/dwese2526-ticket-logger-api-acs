package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.services;

import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.UserProfileFormDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {
    UserProfileFormDTO getFormByEmail(String email);
    void updateProfile(String email, UserProfileFormDTO profileDto, MultipartFile profileImageFile);
}
