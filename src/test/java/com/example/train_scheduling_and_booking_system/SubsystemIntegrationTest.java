package com.example.train_scheduling_and_booking_system;

import com.example.train_scheduling_and_booking_system.dto.*;
import com.example.train_scheduling_and_booking_system.entity.ConcessionType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubsystemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("1. Auth: Register new passenger and verify JWT token & default ROLE_PASSENGER")
    void testPassengerRegistrationAndLogin() throws Exception {
        String uniqueUsername = "passenger_" + System.currentTimeMillis();
        String uniquePhone = "+94" + (100000000 + (long) (Math.random() * 899999999));

        RegisterRequest registerReq = RegisterRequest.builder()
                .username(uniqueUsername)
                .password("Secret@123")
                .fullName("Jane Doe")
                .phoneNumber(uniquePhone)
                .email(uniqueUsername + "@test.com")
                .build();

        // Register
        MvcResult regResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.roles").isArray())
                .andReturn();

        ApiResponse<AuthResponse> regResponse = objectMapper.readValue(
                regResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        assertThat(regResponse.getData().getRoles()).contains("ROLE_PASSENGER");

        // Duplicate registration should fail
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("already taken")));

        // Login
        LoginRequest loginReq = LoginRequest.builder()
                .username(uniqueUsername)
                .password("Secret@123")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value(uniqueUsername));
    }

    @Test
    @DisplayName("2. Staff Logins: Coordinator, Supervisor, Finance Officer, Station Staff, Admin")
    void testStaffMembersLogin() throws Exception {
        String[][] staffCredentials = {
                {"coordinator", "Coord@123", "ROLE_SCHEDULE_COORDINATOR"},
                {"supervisor", "Super@123", "ROLE_CUSTOMER_SERVICE_SUPERVISOR"},
                {"finance", "Finance@123", "ROLE_FINANCE_OFFICER"},
                {"opsmanager", "Ops@123", "ROLE_OPERATIONS_MANAGER"},
                {"stationmaster", "Station@123", "ROLE_STATION_MASTER"},
                {"stationstaff", "Staff@123", "ROLE_STATION_STAFF"},
                {"admin", "Admin@123", "ROLE_ADMIN"}
        };

        for (String[] cred : staffCredentials) {
            LoginRequest staffLogin = LoginRequest.builder()
                    .username(cred[0])
                    .password(cred[1])
                    .build();

            MvcResult result = mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(staffLogin)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.token").isNotEmpty())
                    .andReturn();

            ApiResponse<AuthResponse> response = objectMapper.readValue(
                    result.getResponse().getContentAsString(),
                    new TypeReference<>() {}
            );
            assertThat(response.getData().getRoles()).contains(cred[2]);
        }
    }

    @Test
    @DisplayName("3. Profile & Password Management: Passenger & Staff Profile Updates and Password Changes")
    void testProfileAndChangePassword() throws Exception {
        // Test with coordinator staff account
        LoginRequest coordLogin = LoginRequest.builder()
                .username("coordinator")
                .password("Coord@123")
                .build();

        MvcResult coordResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coordLogin)))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<AuthResponse> coordAuth = objectMapper.readValue(
                coordResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        String coordToken = "Bearer " + coordAuth.getData().getToken();

        // 1. Get Profile
        mockMvc.perform(get("/api/user/profile").header("Authorization", coordToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("coordinator"));

        // 2. Update Profile
        ProfileUpdateRequest updateProfileReq = ProfileUpdateRequest.builder()
                .fullName("Senior Timetable Coordinator")
                .phoneNumber("+94770000001")
                .email("coordinator_lead@trainbooking.com")
                .build();

        mockMvc.perform(put("/api/user/profile")
                        .header("Authorization", coordToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProfileReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullName").value("Senior Timetable Coordinator"))
                .andExpect(jsonPath("$.data.email").value("coordinator_lead@trainbooking.com"));

        // 3. Change Password
        PasswordChangeRequest changePassReq = PasswordChangeRequest.builder()
                .oldPassword("Coord@123")
                .newPassword("Coord@NewPass456")
                .build();

        mockMvc.perform(put("/api/user/change-password")
                        .header("Authorization", coordToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePassReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed successfully"));

        // Verify login with new password
        LoginRequest newPassLogin = LoginRequest.builder()
                .username("coordinator")
                .password("Coord@NewPass456")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassLogin)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("4. Travel Companion Management: Complete CRUD Flow")
    void testCompanionCrud() throws Exception {
        // Register passenger
        String username = "companion_user_" + System.currentTimeMillis();
        String phone = "+94" + (200000000 + (long) (Math.random() * 799999999));

        RegisterRequest registerReq = RegisterRequest.builder()
                .username(username)
                .password("Pass@123")
                .fullName("Family Organizer")
                .phoneNumber(phone)
                .build();

        MvcResult regResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated())
                .andReturn();

        ApiResponse<AuthResponse> auth = objectMapper.readValue(
                regResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        String token = "Bearer " + auth.getData().getToken();

        // 1. Add Companion
        CompanionRequest companionReq = CompanionRequest.builder()
                .fullName("Alice Perera")
                .nicOrPassport("NIC199855443322")
                .concessionType(ConcessionType.STUDENT)
                .concessionRef("SLIIT-IT-8844")
                .build();

        MvcResult compResult = mockMvc.perform(post("/api/passenger/companions")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companionReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.fullName").value("Alice Perera"))
                .andExpect(jsonPath("$.data.concessionType").value("STUDENT"))
                .andReturn();

        ApiResponse<CompanionResponse> compResp = objectMapper.readValue(
                compResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        Long companionId = compResp.getData().getCompanionId();

        // 2. List Companions
        mockMvc.perform(get("/api/passenger/companions").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].companionId").value(companionId));

        // 3. Get Companion by ID
        mockMvc.perform(get("/api/passenger/companions/" + companionId).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullName").value("Alice Perera"));

        // 4. Update Companion
        CompanionRequest compUpdateReq = CompanionRequest.builder()
                .fullName("Alice Perera Updated")
                .nicOrPassport("NIC199855443322")
                .concessionType(ConcessionType.SENIOR)
                .concessionRef("NIC-SEN-1955")
                .build();

        mockMvc.perform(put("/api/passenger/companions/" + companionId)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(compUpdateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullName").value("Alice Perera Updated"))
                .andExpect(jsonPath("$.data.concessionType").value("SENIOR"));

        // 5. Delete Companion
        mockMvc.perform(delete("/api/passenger/companions/" + companionId).header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Companion deleted successfully"));

        // Verify empty list
        mockMvc.perform(get("/api/passenger/companions").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("5. Booking History: Fetch Passenger Bookings (Read-only)")
    void testPassengerBookingHistory() throws Exception {
        // Login as pre-seeded demo passenger
        LoginRequest loginReq = LoginRequest.builder()
                .username("passenger_demo")
                .password("Pass@123")
                .build();

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<AuthResponse> auth = objectMapper.readValue(
                loginResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        String token = "Bearer " + auth.getData().getToken();

        // Fetch user bookings history
        mockMvc.perform(get("/api/bookings/my-bookings").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].bookingReference").isNotEmpty())
                .andExpect(jsonPath("$.data[0].trainName").isNotEmpty())
                .andExpect(jsonPath("$.data[0].status").value("CONFIRMED"));
    }
}

