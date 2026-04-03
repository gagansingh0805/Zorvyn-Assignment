package com.assignment.backend.security;

import com.assignment.backend.record.enums.RecordType;
import com.assignment.backend.user.enums.Role;
import com.assignment.backend.user.enums.UserStatus;
import com.assignment.backend.user.model.User;
import com.assignment.backend.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityAccessTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long adminId;
    private Long analystId;
    private Long viewerId;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        adminId = userRepository.save(buildUser("admin@example.com", Role.ADMIN)).getId();
        analystId = userRepository.save(buildUser("analyst@example.com", Role.ANALYST)).getId();
        viewerId = userRepository.save(buildUser("viewer@example.com", Role.VIEWER)).getId();
    }

    @Test
    void viewerCannotReadRecords() throws Exception {
        mockMvc.perform(get("/records")
                        .header("X-User-Id", viewerId))
                .andExpect(status().isForbidden());
    }

    @Test
    void analystCanReadRecords() throws Exception {
        mockMvc.perform(get("/records")
                        .header("X-User-Id", analystId))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanCreateRecord() throws Exception {
        Map<String, Object> body = Map.of(
                "amount", 1200,
                "type", RecordType.INCOME,
                "category", "Salary",
                "date", LocalDate.now().toString(),
                "notes", "April salary"
        );

        mockMvc.perform(post("/records")
                        .header("X-User-Id", adminId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    private User buildUser(String email, Role role) {
        return User.builder()
                .name(email)
                .email(email)
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();
    }
}
