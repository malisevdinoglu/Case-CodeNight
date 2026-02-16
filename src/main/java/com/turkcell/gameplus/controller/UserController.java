package com.turkcell.gameplus.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.gameplus.dto.UserDetailDto;
import com.turkcell.gameplus.dto.UserDto;
import com.turkcell.gameplus.model.*;
import com.turkcell.gameplus.repository.*;
import com.turkcell.gameplus.service.BadgeService;
import com.turkcell.gameplus.service.NotificationService;
import com.turkcell.gameplus.service.PointsLedgerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final PointsLedgerService pointsLedgerService;
    private final UserStateRepository userStateRepository;
    private final QuestAwardRepository questAwardRepository;
    private final BadgeService badgeService;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(user -> {
                    Integer totalPoints = pointsLedgerService.getTotalPoints(user.getUserId());
                    return new UserDto(
                            user.getUserId(),
                            user.getName(),
                            user.getCity(),
                            user.getSegment(),
                            totalPoints
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailDto> getUserDetail(@PathVariable String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Integer totalPoints = pointsLedgerService.getTotalPoints(userId);

        // Get latest user state
        UserState latestState = userStateRepository.findAll().stream()
                .filter(state -> state.getUserId().equals(userId))
                .max((s1, s2) -> s1.getAsOfDate().compareTo(s2.getAsOfDate()))
                .orElse(null);

        // Get quest awards
        List<QuestAward> questAwards = questAwardRepository.findByUserIdOrderByAsOfDateDesc(userId);

        // Get badges
        List<BadgeAward> badges = badgeService.getUserBadges(userId);

        // Get notifications
        List<Notification> notifications = notificationService.getUserNotifications(userId);

        // Build DTO
        UserDetailDto detailDto = new UserDetailDto();
        detailDto.setUserId(user.getUserId());
        detailDto.setName(user.getName());
        detailDto.setCity(user.getCity());
        detailDto.setSegment(user.getSegment());
        detailDto.setTotalPoints(totalPoints);

        if (latestState != null) {
            detailDto.setUserState(new com.turkcell.gameplus.dto.UserDetailDto.UserStateDto(
                    latestState.getAsOfDate(),
                    latestState.getLoginCountToday(),
                    latestState.getPlayMinutesToday(),
                    latestState.getPvpWinsToday(),
                    latestState.getCoopMinutesToday(),
                    latestState.getTopupTryToday(),
                    latestState.getPlayMinutes7d(),
                    latestState.getTopupTry7d(),
                    latestState.getLogins7d(),
                    latestState.getLoginStreakDays()
            ));
        }

        // Convert quest awards
        List<com.turkcell.gameplus.dto.UserDetailDto.QuestAwardDto> questAwardDtos = questAwards.stream()
                .map(award -> {
                    List<String> triggered = parseJsonList(award.getTriggeredQuests());
                    List<String> suppressed = parseJsonList(award.getSuppressedQuests());
                    return new com.turkcell.gameplus.dto.UserDetailDto.QuestAwardDto(
                            award.getAwardId(),
                            award.getAsOfDate(),
                            award.getSelectedQuest() != null ? award.getSelectedQuest().getQuestName() : "",
                            award.getRewardPoints(),
                            triggered,
                            suppressed
                    );
                })
                .collect(Collectors.toList());
        detailDto.setQuestAwards(questAwardDtos);

        // Convert badges
        List<com.turkcell.gameplus.dto.UserDetailDto.BadgeAwardDto> badgeDtos = badges.stream()
                .map(badge -> new com.turkcell.gameplus.dto.UserDetailDto.BadgeAwardDto(
                        badge.getBadgeId(),
                        badge.getBadge() != null ? badge.getBadge().getBadgeName() : "",
                        badge.getBadge() != null ? badge.getBadge().getLevel() : 0
                ))
                .collect(Collectors.toList());
        detailDto.setBadges(badgeDtos);

        // Convert notifications
        List<com.turkcell.gameplus.dto.UserDetailDto.NotificationDto> notificationDtos = notifications.stream()
                .map(notif -> new com.turkcell.gameplus.dto.UserDetailDto.NotificationDto(
                        notif.getNotificationId(),
                        notif.getChannel(),
                        notif.getMessage(),
                        notif.getSentAt() != null ? notif.getSentAt().toString() : ""
                ))
                .collect(Collectors.toList());
        detailDto.setNotifications(notificationDtos);

        return ResponseEntity.ok(detailDto);
    }

    private List<String> parseJsonList(String json) {
        if (json == null || json.isEmpty()) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.warn("Error parsing JSON list: {}", json, e);
            return List.of();
        }
    }
}

