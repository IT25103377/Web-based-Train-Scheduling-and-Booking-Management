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
    @DisplayName("Public Endpoint: Fetch Landing CMS Content without authentication")
    void testGetPublicLandingContent() throws Exception {
        mockMvc.perform(get("/api/public/landing-content"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].contentKey").exists());
    }

    @Test
    @DisplayName("Auth: Register new passenger and verify JWT token & default ROLE_PASSENGER")
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
    @DisplayName("Passenger Profile & Travel Companion Full CRUD Flow")
    void testPassengerProfileAndCompanionCrud() throws Exception {
        // 1. Register a passenger
        String username = "traveler_" + System.currentTimeMillis();
        String phone = "+94" + (200000000 + (long) (Math.random() * 799999999));

        RegisterRequest registerReq = RegisterRequest.builder()
                .username(username)
                .password("Travel@123")
                .fullName("John Traveler")
                .phoneNumber(phone)
                .email(username + "@travel.com")
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
        String passengerToken = "Bearer " + auth.getData().getToken();

        // 2. Get Profile
        mockMvc.perform(get("/api/passenger/profile")
                        .header("Authorization", passengerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.fullName").value("John Traveler"));

        // 3. Update Profile
        ProfileUpdateRequest updateReq = ProfileUpdateRequest.builder()
                .fullName("John Traveler Updated")
                .phoneNumber(phone)
                .email(username + "_new@travel.com")
                .build();

        mockMvc.perform(put("/api/passenger/profile")
                        .header("Authorization", passengerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullName").value("John Traveler Updated"));

        // 4. Change Password with correct and invalid old password
        PasswordChangeRequest invalidChange = PasswordChangeRequest.builder()
                .oldPassword("WrongPassword")
                .newPassword("NewTravel@123")
                .build();

        mockMvc.perform(put("/api/passenger/change-password")
                        .header("Authorization", passengerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidChange)))
                .andExpect(status().isBadRequest());

        PasswordChangeRequest validChange = PasswordChangeRequest.builder()
                .oldPassword("Travel@123")
                .newPassword("NewTravel@123")
                .build();

        mockMvc.perform(put("/api/passenger/change-password")
                        .header("Authorization", passengerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validChange)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed successfully"));

        // 5. Add Travel Companion
        CompanionRequest companionReq = CompanionRequest.builder()
                .fullName("Alice Traveler")
                .nicOrPassport("NIC987654321V")
                .concessionType(ConcessionType.STUDENT)
                .concessionRef("STU-2026-99")
                .build();

        MvcResult compResult = mockMvc.perform(post("/api/passenger/companions")
                        .header("Authorization", passengerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companionReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.fullName").value("Alice Traveler"))
                .andExpect(jsonPath("$.data.concessionType").value("STUDENT"))
                .andReturn();

        ApiResponse<CompanionResponse> compResponse = objectMapper.readValue(
                compResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        Long companionId = compResponse.getData().getCompanionId();

        // 6. List Companions
        mockMvc.perform(get("/api/passenger/companions")
                        .header("Authorization", passengerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].companionId").value(companionId));

        // 7. Update Companion
        CompanionRequest compUpdateReq = CompanionRequest.builder()
                .fullName("Alice Traveler Smith")
                .nicOrPassport("NIC987654321V")
                .concessionType(ConcessionType.SENIOR)
                .concessionRef("SEN-7788")
                .build();

        mockMvc.perform(put("/api/passenger/companions/" + companionId)
                        .header("Authorization", passengerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(compUpdateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fullName").value("Alice Traveler Smith"))
                .andExpect(jsonPath("$.data.concessionType").value("SENIOR"));

        // 8. Delete Companion
        mockMvc.perform(delete("/api/passenger/companions/" + companionId)
                        .header("Authorization", passengerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Companion deleted successfully"));

        // Verify companion list is now empty
        mockMvc.perform(get("/api/passenger/companions")
                        .header("Authorization", passengerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Admin CMS: Admin can update landing page content, passenger is forbidden")
    void testAdminCmsAndRbacProtection() throws Exception {
        // 1. Admin login
        LoginRequest adminLogin = LoginRequest.builder()
                .username("admin")
                .password("Admin@123")
                .build();

        MvcResult adminResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<AuthResponse> adminAuth = objectMapper.readValue(
                adminResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        String adminToken = "Bearer " + adminAuth.getData().getToken();

        // 2. Admin updates ALERT_BANNER
        ContentUpdateRequest updateReq = ContentUpdateRequest.builder()
                .title("Urgent System Maintenance")
                .contentValue("Heavy monsoon warning. All coastal lines running at reduced speeds.")
                .category("LANDING_PAGE")
                .build();

        mockMvc.perform(put("/api/admin/content/ALERT_BANNER")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.contentKey").value("ALERT_BANNER"))
                .andExpect(jsonPath("$.data.title").value("Urgent System Maintenance"));

        // 3. Register passenger
        String passUsername = "passenger_rbac_" + System.currentTimeMillis();
        String passPhone = "+94" + (300000000 + (long) (Math.random() * 699999999));
        RegisterRequest regReq = RegisterRequest.builder()
                .username(passUsername)
                .password("Pass@12345")
                .fullName("Regular Passenger")
                .phoneNumber(passPhone)
                .build();

        MvcResult passResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isCreated())
                .andReturn();

        ApiResponse<AuthResponse> passAuth = objectMapper.readValue(
                passResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        String passToken = "Bearer " + passAuth.getData().getToken();

        // 4. Passenger attempting to access admin CMS endpoint -> 403 Forbidden
        mockMvc.perform(put("/api/admin/content/ALERT_BANNER")
                        .header("Authorization", passToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isForbidden());

        // 5. Unauthenticated request to admin endpoint -> 401 Unauthorized
        mockMvc.perform(get("/api/admin/content"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("RBAC: Coordinator & Station Staff Access Restrictions")
    void testCoordinatorAndStationStaffRbac() throws Exception {
        // Coordinator login
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

        // Station staff login
        LoginRequest staffLogin = LoginRequest.builder()
                .username("stationstaff")
                .password("Staff@123")
                .build();

        MvcResult staffResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(staffLogin)))
                .andExpect(status().isOk())
                .andReturn();

        ApiResponse<AuthResponse> staffAuth = objectMapper.readValue(
                staffResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        String staffToken = "Bearer " + staffAuth.getData().getToken();

        // 1. Coordinator can access schedules and routes
        mockMvc.perform(get("/api/schedules").header("Authorization", coordToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        mockMvc.perform(get("/api/routes").header("Authorization", coordToken))
                .andExpect(status().isOk());

        // 2. Coordinator cannot access station endpoints (403 Forbidden)
        mockMvc.perform(get("/api/station/daily-schedule/FOT").header("Authorization", coordToken))
                .andExpect(status().isForbidden());

        // 3. Station staff can access station endpoints
        mockMvc.perform(get("/api/station/daily-schedule/FOT").header("Authorization", staffToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.stationCode").value("FOT"));

        mockMvc.perform(post("/api/station/verify-ticket/TCK-9901").header("Authorization", staffToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("VERIFIED"));

        // 4. Station staff cannot access schedules endpoint (403 Forbidden)
        mockMvc.perform(get("/api/schedules").header("Authorization", staffToken))
                .andExpect(status().isForbidden());
    }
}
