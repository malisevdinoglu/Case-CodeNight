package com.turkcell.gameplus.service;

import com.turkcell.gameplus.model.*;
import com.turkcell.gameplus.repository.*;
import com.turkcell.gameplus.util.CsvUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataExportService {

    private final CsvUtil csvUtil;
    private final UserStateRepository userStateRepository;
    private final QuestAwardRepository questAwardRepository;
    private final PointsLedgerEntryRepository pointsLedgerEntryRepository;
    private final BadgeAwardRepository badgeAwardRepository;
    private final NotificationRepository notificationRepository;
    private final LeaderboardEntryRepository leaderboardEntryRepository;

    @Value("${csv.output.path}")
    private String outputPath;

    public void exportToCsv() throws IOException {
        log.info("Starting CSV export...");
        
        exportUserStates();
        exportQuestAwards();
        exportPointsLedger();
        exportBadgeAwards();
        exportNotifications();
        exportLeaderboard();
        
        log.info("CSV export completed successfully");
    }

    private void exportUserStates() throws IOException {
        String filePath = outputPath + "user_state.csv";
        List<UserState> states = userStateRepository.findAll();
        List<String[]> data = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (UserState state : states) {
            String[] row = {
                state.getUserId(),
                state.getAsOfDate().format(formatter),
                String.valueOf(state.getLoginCountToday()),
                String.valueOf(state.getPlayMinutesToday()),
                String.valueOf(state.getPvpWinsToday()),
                String.valueOf(state.getCoopMinutesToday()),
                String.valueOf(state.getTopupTryToday()),
                String.valueOf(state.getPlayMinutes7d()),
                String.valueOf(state.getTopupTry7d()),
                String.valueOf(state.getLogins7d()),
                String.valueOf(state.getLoginStreakDays())
            };
            data.add(row);
        }
        
        String[] header = {"user_id", "as_of_date", "login_count_today", "play_minutes_today", 
                          "pvp_wins_today", "coop_minutes_today", "topup_try_today", 
                          "play_minutes_7d", "topup_try_7d", "logins_7d", "login_streak_days"};
        csvUtil.writeCsv(filePath, data, header);
        log.info("Exported {} user states", data.size());
    }

    private void exportQuestAwards() throws IOException {
        String filePath = outputPath + "quest_awards.csv";
        List<QuestAward> awards = questAwardRepository.findAll();
        List<String[]> data = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (QuestAward award : awards) {
            String[] row = {
                award.getAwardId(),
                award.getUserId(),
                award.getAsOfDate().format(dateFormatter),
                award.getTriggeredQuests(),
                award.getSelectedQuestId(),
                String.valueOf(award.getRewardPoints()),
                award.getSuppressedQuests(),
                award.getTimestamp() != null ? award.getTimestamp().format(timestampFormatter) : ""
            };
            data.add(row);
        }
        
        String[] header = {"award_id", "user_id", "as_of_date", "triggered_quests", 
                          "selected_quest", "reward_points", "suppressed_quests", "timestamp"};
        csvUtil.writeCsv(filePath, data, header);
        log.info("Exported {} quest awards", data.size());
    }

    private void exportPointsLedger() throws IOException {
        String filePath = outputPath + "points_ledger.csv";
        List<PointsLedgerEntry> entries = pointsLedgerEntryRepository.findAll();
        List<String[]> data = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (PointsLedgerEntry entry : entries) {
            String[] row = {
                entry.getLedgerId(),
                entry.getUserId(),
                String.valueOf(entry.getPointsDelta()),
                entry.getSource(),
                entry.getSourceRef(),
                entry.getCreatedAt() != null ? entry.getCreatedAt().format(formatter) : ""
            };
            data.add(row);
        }
        
        String[] header = {"ledger_id", "user_id", "points_delta", "source", "source_ref", "created_at"};
        csvUtil.writeCsv(filePath, data, header);
        log.info("Exported {} ledger entries", data.size());
    }

    private void exportBadgeAwards() throws IOException {
        String filePath = outputPath + "badge_awards.csv";
        List<BadgeAward> awards = badgeAwardRepository.findAll();
        List<String[]> data = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (BadgeAward award : awards) {
            String[] row = {
                award.getUserId(),
                award.getBadgeId(),
                award.getAwardedAt() != null ? award.getAwardedAt().format(formatter) : ""
            };
            data.add(row);
        }
        
        String[] header = {"user_id", "badge_id", "awarded_at"};
        csvUtil.writeCsv(filePath, data, header);
        log.info("Exported {} badge awards", data.size());
    }

    private void exportNotifications() throws IOException {
        String filePath = outputPath + "notifications.csv";
        List<Notification> notifications = notificationRepository.findAll();
        List<String[]> data = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (Notification notification : notifications) {
            String[] row = {
                notification.getNotificationId(),
                notification.getUserId(),
                notification.getChannel(),
                notification.getMessage(),
                notification.getSentAt() != null ? notification.getSentAt().format(formatter) : ""
            };
            data.add(row);
        }
        
        String[] header = {"notification_id", "user_id", "channel", "message", "sent_at"};
        csvUtil.writeCsv(filePath, data, header);
        log.info("Exported {} notifications", data.size());
    }

    private void exportLeaderboard() throws IOException {
        String filePath = outputPath + "leaderboard.csv";
        List<LeaderboardEntry> entries = leaderboardEntryRepository.findAll();
        List<String[]> data = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (LeaderboardEntry entry : entries) {
            String[] row = {
                String.valueOf(entry.getRank()),
                entry.getUserId(),
                String.valueOf(entry.getTotalPoints()),
                entry.getAsOfDate().format(formatter)
            };
            data.add(row);
        }
        
        String[] header = {"rank", "user_id", "total_points", "as_of_date"};
        csvUtil.writeCsv(filePath, data, header);
        log.info("Exported {} leaderboard entries", data.size());
    }
}

