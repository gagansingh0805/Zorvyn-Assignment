package com.assignment.backend.record.repository;

import com.assignment.backend.record.enums.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.assignment.backend.record.model.Record;

import java.time.LocalDate;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long>, JpaSpecificationExecutor<Record> {

    List<Record> findByType(RecordType type);

    List<Record> findByCategory(String category);

    List<Record> findByDateBetween(LocalDate start, LocalDate end);

    List<Record> findByUserId(Long userId);

    List<Record> findByTypeAndCategory(RecordType type, String category);

    List<Record> findByTypeAndCategoryAndDateBetween(
            RecordType type,
            String category,
            LocalDate start,
            LocalDate end
    );

}
