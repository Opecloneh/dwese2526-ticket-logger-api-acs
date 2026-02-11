package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.UserProfilePatchDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.UserProfileDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.services.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;

@Validated
@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private MessageSource messageSource;


    @GetMapping
    public ResponseEntity<UserProfileDTO> getMyProfile(Principal principal) {
        String email = principal.getName();
        logger.info("API getMyProfile para {}", email);

        UserProfileDTO dto = userProfileService.getFormByEmail(email);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileDTO> patchMyProfile(
            @ModelAttribute UserProfilePatchDTO patchDTO,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            Principal principal) {

        String email = principal.getName();
        logger.info("API patchMyProfile para {}", email);

        userProfileService.updateProfile(email, patchDTO, profileImageFile);

        UserProfileDTO updated = userProfileService.getFormByEmail(email);
        return ResponseEntity.ok(updated);
    }
}
