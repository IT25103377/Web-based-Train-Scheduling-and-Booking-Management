package com.example.train_scheduling_and_booking_system.service;

import com.example.train_scheduling_and_booking_system.dto.ContentUpdateRequest;
import com.example.train_scheduling_and_booking_system.dto.PortalContentResponse;
import com.example.train_scheduling_and_booking_system.entity.PortalContent;
import com.example.train_scheduling_and_booking_system.entity.User;
import com.example.train_scheduling_and_booking_system.exception.ResourceNotFoundException;
import com.example.train_scheduling_and_booking_system.repository.PortalContentRepository;
import com.example.train_scheduling_and_booking_system.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminContentService {

    private static final Logger log = LoggerFactory.getLogger(AdminContentService.class);

    private final PortalContentRepository portalContentRepository;
    private final UserRepository userRepository;

    public AdminContentService(PortalContentRepository portalContentRepository, UserRepository userRepository) {
        this.portalContentRepository = portalContentRepository;
        this.userRepository = userRepository;
    }

    public List<PortalContentResponse> getPublicLandingContent() {
        return portalContentRepository.findAllByCategory("LANDING_PAGE").stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PortalContentResponse> getAllContent() {
        return portalContentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PortalContentResponse getContentByKey(String contentKey) {
        PortalContent content = portalContentRepository.findByContentKey(contentKey)
                .orElseThrow(() -> new ResourceNotFoundException("Portal content not found with key: " + contentKey));
        return mapToResponse(content);
    }

    @Transactional
    public PortalContentResponse updateOrCreateContent(String contentKey, ContentUpdateRequest request, String adminUsername) {
        User adminUser = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found with username: " + adminUsername));

        PortalContent content = portalContentRepository.findByContentKey(contentKey)
                .orElseGet(() -> PortalContent.builder()
                        .contentKey(contentKey.trim())
                        .build());

        content.setTitle(request.getTitle().trim());
        content.setContentValue(request.getContentValue().trim());
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            content.setCategory(request.getCategory().trim());
        } else if (content.getCategory() == null) {
            content.setCategory("LANDING_PAGE");
        }
        content.setUpdatedBy(adminUser);

        PortalContent saved = portalContentRepository.save(content);
        log.info("Portal content '{}' updated/created by admin '{}'", contentKey, adminUsername);
        return mapToResponse(saved);
    }

    private PortalContentResponse mapToResponse(PortalContent content) {
        return PortalContentResponse.builder()
                .contentId(content.getContentId())
                .contentKey(content.getContentKey())
                .title(content.getTitle())
                .contentValue(content.getContentValue())
                .category(content.getCategory())
                .updatedByUsername(content.getUpdatedBy() != null ? content.getUpdatedBy().getUsername() : "SYSTEM")
                .updatedAt(content.getUpdatedAt())
                .build();
    }
}
