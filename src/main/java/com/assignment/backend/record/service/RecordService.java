package com.assignment.backend.record.service;

import com.assignment.backend.record.dto.request.RecordRequestDTO;
import com.assignment.backend.record.dto.response.RecordResponseDTO;
import com.assignment.backend.record.enums.RecordType;
import com.assignment.backend.record.exception.ResourceNotFoundException;
import com.assignment.backend.record.mapper.RecordMapper;
import com.assignment.backend.record.model.Record;
import com.assignment.backend.record.repository.RecordRepository;
import com.assignment.backend.record.specification.RecordSpecification;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {

    private static final Logger log = LoggerFactory.getLogger(RecordService.class);

    private final RecordRepository recordRepository;

    public RecordResponseDTO createRecord(RecordRequestDTO req) {
        log.info("Creating a new record with category {}", req.getCategory());
        Record r = RecordMapper.toEntity(req);
        r.setCreatedAt(LocalDateTime.now());
        return RecordMapper.toDTO(recordRepository.save(r));
    }

    public Page<RecordResponseDTO> getAllRecords(Pageable pageable) {
        log.info("Fetching records page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return recordRepository.findAll(pageable)
                .map(RecordMapper::toDTO);
    }

    public RecordResponseDTO updateRecord(Long id, RecordRequestDTO req) {
        log.info("Updating record id={}", id);
        Record r = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        r.setAmount(req.getAmount());
        r.setType(req.getType());
        r.setCategory(req.getCategory());
        r.setDate(req.getDate());
        r.setNotes(req.getNotes());

        return RecordMapper.toDTO(recordRepository.save(r));
    }

    public void deleteRecord(Long id) {
        log.info("Deleting record id={}", id);
        if (!recordRepository.existsById(id)) {
            throw new ResourceNotFoundException("Record not found");
        }
        recordRepository.deleteById(id);
    }

    public Page<RecordResponseDTO> filterRecords(
            RecordType type,
            String category,
            LocalDate start,
            LocalDate end,
            Pageable pageable
    ) {
        log.info("Filtering records type={}, category={}, start={}, end={}", type, category, start, end);

        if ((start == null) != (end == null)) {
            throw new IllegalArgumentException("Both start and end dates are required together");
        }
        if (start != null && start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        Specification<Record> specification = (root, query, cb) -> cb.conjunction();

        if (type != null) {
            specification = specification.and(RecordSpecification.hasType(type));
        }

        if (category != null && !category.isBlank()) {
            specification = specification.and(RecordSpecification.hasCategory(category));
        }

        if (start != null && end != null) {
            specification = specification.and(RecordSpecification.dateBetween(start, end));
        }

        return recordRepository.findAll(specification, pageable)
                .map(RecordMapper::toDTO);
    }
}
