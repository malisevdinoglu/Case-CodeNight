package com.turkcell.gameplus.service;

import com.turkcell.gameplus.model.LeaderboardEntry;
import com.turkcell.gameplus.model.LeaderboardEntryId;
import com.turkcell.gameplus.model.User;
import com.turkcell.gameplus.repository.LeaderboardEntryRepository;
import com.turkcell.gameplus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardService {

    private final LeaderboardEntryRepository leaderboardEntryRepository;
    private final UserRepository userRepository;
    private final PointsLedgerService pointsLedgerService;

    @Transactional
    public List<LeaderboardEntry> generateLeaderboard(LocalDate asOfDate) {
        log.info("Generating leaderboard for date: {}", asOfDate);

        // Get total points for all users
        Map<String, Integer> userPoints = pointsLedgerService.getTotalPointsForAllUsers();
        log.debug("Found {} users with points", userPoints.size());

        // Sort by total_points DESC, then user_id ASC
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(userPoints.entrySet());
        sortedEntries.sort((e1, e2) -> {
            int pointsCompare = e2.getValue().compareTo(e1.getValue());
            if (pointsCompare != 0) {
                return pointsCompare;
            }
            return e1.getKey().compareTo(e2.getKey());
        });

        // Create leaderboard entries
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        int rank = 1;

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            String userId = entry.getKey();
            Integer totalPoints = entry.getValue();

            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                log.warn("User not found: {}", userId);
                continue;
            }

            LeaderboardEntry leaderboardEntry = new LeaderboardEntry();
            leaderboardEntry.setRank(rank);
            leaderboardEntry.setUser(user);
            leaderboardEntry.setUserId(userId);
            leaderboardEntry.setTotalPoints(totalPoints);
            leaderboardEntry.setAsOfDate(asOfDate);

            leaderboard.add(leaderboardEntry);
            rank++;
        }

        // Save all entries
        leaderboardEntryRepository.saveAll(leaderboard);
        log.info("Generated leaderboard with {} entries", leaderboard.size());

        return leaderboard;
    }

    public List<LeaderboardEntry> getLeaderboard(LocalDate asOfDate, Integer limit) {
        List<LeaderboardEntry> entries = leaderboardEntryRepository.findByAsOfDateOrdered(asOfDate);

        if (entries.isEmpty() && LocalDate.now().equals(asOfDate)) {
            // Fallback to latest available date
            LeaderboardEntry latestEntry =
                    leaderboardEntryRepository.findTopByOrderByAsOfDateDesc();
            if (latestEntry != null) {
                log.info("Leaderboard empty for today, falling back to latest date: {}",
                        latestEntry.getAsOfDate());
                return leaderboardEntryRepository.findByAsOfDateOrdered(latestEntry.getAsOfDate())
                        .stream().limit(limit != null && limit > 0 ? limit : Long.MAX_VALUE)
                        .toList();
            }

            log.info("Leaderboard empty for today and no previous data, generating now...");
            return generateLeaderboard(asOfDate).stream()
                    .sorted(Comparator.comparingInt(LeaderboardEntry::getRank))
                    .limit(limit != null && limit > 0 ? limit : Long.MAX_VALUE).toList();
        }

        if (limit != null && limit > 0) {
            return entries.stream().limit(limit).toList();
        }

        return entries;
    }
}

