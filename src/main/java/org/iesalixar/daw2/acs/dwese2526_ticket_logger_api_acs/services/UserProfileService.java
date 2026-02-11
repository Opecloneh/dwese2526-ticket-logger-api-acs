package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.services;

import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.UserProfileDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.UserProfilePatchDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {
    UserProfileDTO getFormByEmail(String email);
    void updateProfile(String email, UserProfilePatchDTO profileDto, MultipartFile profileImageFile);
}
