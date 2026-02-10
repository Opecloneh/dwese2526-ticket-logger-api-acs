package org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.services;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.ProvinceDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.ProvinceDetailDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.dtos.ProvinceUpdateDTO;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Province;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.entities.Region;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.mappers.ProvinceMapper;
import org.iesalixar.daw2.acs.dwese2526_ticket_logger_api_acs.repositories.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private RegionService regionService;

    @Override
    public Page<ProvinceDTO> list(Pageable pageable) {
        return provinceRepository.findAll(pageable).map(ProvinceMapper::toDTO);
    }

    @Override
    public ProvinceUpdateDTO getForEdit(Long id) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("province", "id", id));
        return ProvinceMapper.toUpdateDTO(province);
    }

    @Override
    public ProvinceDTO create(ProvinceCreateDTO dto) {
        if (provinceRepository.existsByCode(dto.getCode())) {
            throw new DuplicateResourceException("province", "code", dto.getCode());
        }

        Region region = regionService.findById(dto.getRegionId());
        Province province = new Province(dto.getCode(), dto.getName(), region);
        province = provinceRepository.save(province);
        return ProvinceMapper.toDTO(province);
    }

    @Override
    public ProvinceDTO update(ProvinceUpdateDTO dto) {
        Province province = provinceRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("province", "id", dto.getId()));

        if (provinceRepository.existsByCodeAndIdNot(dto.getCode(), dto.getId())) {
            throw new DuplicateResourceException("province", "code", dto.getCode());
        }

        Region region = regionService.findById(dto.getRegionId());
        province.setCode(dto.getCode());
        province.setName(dto.getName());
        province.setRegion(region);

        province = provinceRepository.save(province);
        return ProvinceMapper.toDTO(province);
    }

    @Override
    public void delete(Long id) {
        if (!provinceRepository.existsById(id)) {
            throw new ResourceNotFoundException("province", "id", id);
        }
        provinceRepository.deleteById(id);
    }

    @Override
    public ProvinceDetailDTO getDetail(Long id) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("province", "id", id));
        return ProvinceMapper.toDetailDTO(province);
    }

    @Override
    public List<ProvinceDTO> listAll(Sort name) {
        return provinceRepository.findAll().stream()
                .map(ProvinceMapper::toDTO)
                .toList();
    }

    @Override
    public Object listRegionsForSelect() {
        return null;
    }
}
