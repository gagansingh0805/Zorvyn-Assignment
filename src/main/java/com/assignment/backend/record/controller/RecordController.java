package com.assignment.backend.record.controller;

import com.assignment.backend.record.dto.request.RecordRequestDTO;
import com.assignment.backend.record.dto.response.RecordResponseDTO;
import com.assignment.backend.record.enums.RecordType;
import com.assignment.backend.record.service.RecordService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;


    @PostMapping
    public ResponseEntity<RecordResponseDTO> createRecord(
            @Valid @RequestBody RecordRequestDTO req
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recordService.createRecord(req));
    }


    @GetMapping
    public ResponseEntity<Page<RecordResponseDTO>> getAllRecords(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(recordService.getAllRecords(pageable));
    }


    @PutMapping("/{id}")
    public ResponseEntity<RecordResponseDTO> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody RecordRequestDTO req
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(recordService.updateRecord(id, req));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/filter")
    public ResponseEntity<Page<RecordResponseDTO>> filterRecords(
            @RequestParam(required = false) RecordType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end,
            Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(recordService.filterRecords(type, category, start, end, pageable));
    }
}
