package com.assignment.backend.record.specification;

import com.assignment.backend.record.enums.RecordType;
import com.assignment.backend.record.model.Record;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class RecordSpecification {

    private RecordSpecification() {
    }

    public static Specification<Record> hasType(RecordType type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    public static Specification<Record> hasCategory(String category) {
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    public static Specification<Record> dateBetween(LocalDate start, LocalDate end) {
        return (root, query, cb) -> cb.between(root.get("date"), start, end);
    }
}
