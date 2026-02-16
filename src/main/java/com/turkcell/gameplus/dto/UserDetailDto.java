package com.turkcell.gameplus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDto {
    private String userId;
    private String name;
    private String city;
    private String segment;
    private Integer totalPoints;
    private UserStateDto userState;
    private List<QuestAwardDto> questAwards;
    private List<BadgeAwardDto> badges;
    private List<NotificationDto> notifications;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStateDto {
        private LocalDate asOfDate;
        private Integer loginCountToday;
        private Integer playMinutesToday;
        private Integer pvpWinsToday;
        private Integer coopMinutesToday;
        private Double topupTryToday;
        private Integer playMinutes7d;
        private Double topupTry7d;
        private Integer logins7d;
        private Integer loginStreakDays;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestAwardDto {
        private String awardId;
        private LocalDate asOfDate;
        private String selectedQuestName;
        private Integer rewardPoints;
        private List<String> triggeredQuests;
        private List<String> suppressedQuests;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BadgeAwardDto {
        private String badgeId;
        private String badgeName;
        private Integer level;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationDto {
        private String notificationId;
        private String channel;
        private String message;
        private String sentAt;
    }
}
