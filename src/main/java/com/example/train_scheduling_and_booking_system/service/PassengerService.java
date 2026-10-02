package com.example.train_scheduling_and_booking_system.service;

import com.example.train_scheduling_and_booking_system.dto.*;
import com.example.train_scheduling_and_booking_system.entity.Companion;
import com.example.train_scheduling_and_booking_system.entity.Role;
import com.example.train_scheduling_and_booking_system.entity.User;
import com.example.train_scheduling_and_booking_system.exception.BadRequestException;
import com.example.train_scheduling_and_booking_system.exception.DuplicateResourceException;
import com.example.train_scheduling_and_booking_system.exception.ResourceNotFoundException;
import com.example.train_scheduling_and_booking_system.repository.CompanionRepository;
import com.example.train_scheduling_and_booking_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassengerService {

    private static final Logger log = LoggerFactory.getLogger(PassengerService.class);

    private final UserRepository userRepository;
    private final CompanionRepository companionRepository;
    private final PasswordEncoder passwordEncoder;

    public PassengerService(UserRepository userRepository,
                            CompanionRepository companionRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companionRepository = companionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserProfileResponse getProfile(String username) {
        User user = getUserByUsername(username);
        return mapToProfileResponse(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(String username, ProfileUpdateRequest request) {
        User user = getUserByUsername(username);

        // Check if phone number is changing and if new number already belongs to another user
        if (!user.getPhoneNumber().equals(request.getPhoneNumber().trim())) {
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber().trim())) {
                throw new DuplicateResourceException("Phone number '" + request.getPhoneNumber() + "' is already in use");
            }
            user.setPhoneNumber(request.getPhoneNumber().trim());
        }

        // Check if email is changing and if new email already belongs to another user
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String trimmedEmail = request.getEmail().trim();
            if (user.getEmail() == null || !user.getEmail().equalsIgnoreCase(trimmedEmail)) {
                if (userRepository.existsByEmail(trimmedEmail)) {
                    throw new DuplicateResourceException("Email '" + trimmedEmail + "' is already in use");
                }
                user.setEmail(trimmedEmail);
            }
        } else {
            user.setEmail(null);
        }

        user.setFullName(request.getFullName().trim());
        User updated = userRepository.save(user);
        log.info("Profile updated for user: {}", username);
        return mapToProfileResponse(updated);
    }

    @Transactional
    public void changePassword(String username, PasswordChangeRequest request) {
        User user = getUserByUsername(username);

        // Explicit verification of current password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Current password provided does not match our records");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash())) {
            throw new BadRequestException("New password cannot be the same as the current password");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed successfully for user: {}", username);
    }

    public List<CompanionResponse> getCompanions(String username) {
        User user = getUserByUsername(username);
        List<Companion> companions = companionRepository.findAllByUserUserId(user.getUserId());
        return companions.stream()
                .map(this::mapToCompanionResponse)
                .collect(Collectors.toList());
    }

    public CompanionResponse getCompanionById(Long companionId, String username) {
        User user = getUserByUsername(username);
        Companion companion = companionRepository.findByCompanionIdAndUserUserId(companionId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Companion not found with ID: " + companionId));
        return mapToCompanionResponse(companion);
    }

    @Transactional
    public CompanionResponse createCompanion(CompanionRequest request, String username) {
        User user = getUserByUsername(username);

        Companion companion = Companion.builder()
                .user(user)
                .fullName(request.getFullName().trim())
                .nicOrPassport(request.getNicOrPassport().trim())
                .concessionType(request.getConcessionType())
                .concessionRef(request.getConcessionRef() != null ? request.getConcessionRef().trim() : null)
                .build();

        Companion saved = companionRepository.save(companion);
        log.info("Created companion ID {} for user {}", saved.getCompanionId(), username);
        return mapToCompanionResponse(saved);
    }

    @Transactional
    public CompanionResponse updateCompanion(Long companionId, CompanionRequest request, String username) {
        User user = getUserByUsername(username);

        Companion companion = companionRepository.findByCompanionIdAndUserUserId(companionId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Companion not found with ID: " + companionId));

        companion.setFullName(request.getFullName().trim());
        companion.setNicOrPassport(request.getNicOrPassport().trim());
        companion.setConcessionType(request.getConcessionType());
        companion.setConcessionRef(request.getConcessionRef() != null ? request.getConcessionRef().trim() : null);

        Companion updated = companionRepository.save(companion);
        log.info("Updated companion ID {} for user {}", updated.getCompanionId(), username);
        return mapToCompanionResponse(updated);
    }

    @Transactional
    public void deleteCompanion(Long companionId, String username) {
        User user = getUserByUsername(username);

        Companion companion = companionRepository.findByCompanionIdAndUserUserId(companionId, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Companion not found with ID: " + companionId));

        companionRepository.delete(companion);
        log.info("Deleted companion ID {} for user {}", companionId, username);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    private UserProfileResponse mapToProfileResponse(User user) {
        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()))
                .build();
    }

    private CompanionResponse mapToCompanionResponse(Companion companion) {
        return CompanionResponse.builder()
                .companionId(companion.getCompanionId())
                .userId(companion.getUser().getUserId())
                .fullName(companion.getFullName())
                .nicOrPassport(companion.getNicOrPassport())
                .concessionType(companion.getConcessionType())
                .concessionRef(companion.getConcessionRef())
                .createdAt(companion.getCreatedAt())
                .build();
    }
}
