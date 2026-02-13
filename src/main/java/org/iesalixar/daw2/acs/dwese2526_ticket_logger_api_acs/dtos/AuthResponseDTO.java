package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String message;
}
