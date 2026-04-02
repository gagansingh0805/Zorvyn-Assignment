package com.assignment.backend.record.mapper;

import com.assignment.backend.record.dto.request.RecordRequestDTO;
import com.assignment.backend.record.dto.response.RecordResponseDTO;
import com.assignment.backend.record.model.Record;

public class RecordMapper {

    public static Record toEntity(RecordRequestDTO d) {
        Record r  = new Record();
        r.setAmount(d.getAmount());
        r.setType(d.getType());
        r.setCategory(d.getCategory());
        r.setDate(d.getDate());
        r.setNotes(d.getNotes());
        return r;
    }

    public static RecordResponseDTO toDTO(Record r) {
        RecordResponseDTO d = new RecordResponseDTO();
        d.setId(r.getId());
        d.setAmount(r.getAmount());
        d.setType(r.getType());
        d.setCategory(r.getCategory());
        d.setDate(r.getDate());
        d.setNotes(r.getNotes());
        d.setCreatedAt(r.getCreatedAt());
        return d;
    }
}