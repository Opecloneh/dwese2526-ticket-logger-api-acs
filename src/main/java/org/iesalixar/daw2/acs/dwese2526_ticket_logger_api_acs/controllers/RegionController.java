package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.controllers;
        
import jakarta.servlet.Servlet;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.repositories.RegionRepository;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.*;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Region;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.mappers.RegionMapper;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.services.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controlador Spring MVC para gestionar operaciones CRUD sobre regiones.
 * Proporciona métodos para listar, insertar, actualizar y eliminar regiones.
 */
@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    /**
     * DAO para acceder a los datos de las regiones en la base de datos.
     */
    @Autowired
    private RegionService regionService;

    /**
     * Fuente de mensajes internacionalizados.
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * Muestra la lista de todas las regiones.
     *
     * @return Nombre de la vista que renderiza la lista de regiones.
     */
    @GetMapping
    public ResponseEntity<Page<RegionDTO>> listRegions(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        logger.info("Listando regiones (REST) page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<RegionDTO> page = regionService.list(pageable);

        logger.info("Se han cargado {} regiones en la pagina {}.", page.getNumberOfElements(), page.getNumber());

        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity<RegionDTO> createRegion (@Valid @RequestBody RegionCreateDTO dto) {
        // 1) Delegamos la creacion al servicio
        RegionDTO created = regionService.create(dto);

        // 2) Construimos la cabecera Location con la URL del recurso recien creado: /api/regions/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> updateRegion(@PathVariable Long id,
                                                  @Valid @RequestBody RegionUpdateDTO dto) {
        logger.info("Actualizando region con ID {} (REST)", id);

        //Buena practica: asegurar consistencia entre path y body
        dto.setId(id);

        RegionDTO updated = regionService.update(dto);

        logger.info("Region con ID {} actualizada con exito.", id);

        return ResponseEntity.ok(updated);
    }

    /**
     * Elimina una región por su ID.
     *
     * @param id                 ID de la región a eliminar.
     * @return Redirección a la lista de regiones.
     */
    @DeleteMapping("/delete")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        logger.info("Eliminando region (REST) con ID {}", id);

        // 1) Delegamos en el servicio:
        //      - si existe: elimina
        //      - si no existe: lanza ResourceNotFOundException que se convertira a 404 en @RestControllerAdvice
        regionService.delete(id);

        logger.info("Region con ID {} eliminada con exito.", id);

        // 2) En REST, lo habitual en un DELETE correcto es 204 No Content (sin body)
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<RegionDetailDTO> getRegionById(@PathVariable Long id) {
        logger.info("Mostrando detalle (REST) de la reguion con ID {}", id);

        RegionDetailDTO regionDTO = regionService.getDetail(id);

        return ResponseEntity.ok(regionDTO);
    }

}
