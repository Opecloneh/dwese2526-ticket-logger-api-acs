package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.exceptions;

import lombok.Data;

@Data
public class DuplicateResourceException extends RuntimeException{
    private final String resource;
    private final String field;
    private final Object value;

    public DuplicateResourceException(String resource, String field, Object value) {
        super("Duplicate " + resource + " (" + field + "=" + value + ")");
        this.resource = resource;
        this.field = field;
        this.value = value;
    }
}
