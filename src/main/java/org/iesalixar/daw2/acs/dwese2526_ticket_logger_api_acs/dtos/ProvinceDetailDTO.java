package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDetailDTO {
    private Long id;
    private String code;
    private String name;

    private RegionDTO region;
}
