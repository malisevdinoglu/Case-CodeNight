package com.turkcell.gameplus.service;

import com.turkcell.gameplus.model.ActivityEvent;
import com.turkcell.gameplus.model.User;
import com.turkcell.gameplus.model.UserState;
import com.turkcell.gameplus.model.UserStateId;
import com.turkcell.gameplus.repository.ActivityEventRepository;
import com.turkcell.gameplus.repository.UserRepository;
import com.turkcell.gameplus.repository.UserStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    private final ActivityEventRepository activityEventRepository;
    private final UserStateRepository userStateRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserState calculateUserState(String userId, LocalDate asOfDate) {
        log.debug("Calculating user state for user: {} as of date: {}", userId, asOfDate);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Check if UserState already exists
        UserState existingState = userStateRepository.findByUserIdAndAsOfDate(userId, asOfDate).orElse(null);
        UserState userState = existingState != null ? existingState : new UserState();
        
        userState.setUser(user);
        userState.setUserId(userId);
        userState.setAsOfDate(asOfDate);

        // Calculate today's metrics
        List<ActivityEvent> todayEvents = activityEventRepository.findByUserIdAndDate(userId, asOfDate);
        calculateTodayMetrics(userState, todayEvents);

        // Calculate last 7 days metrics
        LocalDate startDate = asOfDate.minusDays(6);
        List<ActivityEvent> last7DaysEvents = activityEventRepository.findByUserIdAndDateBetween(userId, startDate, asOfDate);
        calculateLast7DaysMetrics(userState, last7DaysEvents);

        // Calculate streak
        calculateStreak(userState, userId, asOfDate);

        // Save or update UserState
        UserState savedState = userStateRepository.save(userState);
        log.debug("User state calculated and saved for user: {}", userId);
        
        return savedState;
    }

    private void calculateTodayMetrics(UserState userState, List<ActivityEvent> todayEvents) {
        int loginCountToday = 0;
        int playMinutesToday = 0;
        int pvpWinsToday = 0;
        int coopMinutesToday = 0;
        double topupTryToday = 0.0;

        for (ActivityEvent event : todayEvents) {
            loginCountToday += event.getLoginCount() != null ? event.getLoginCount() : 0;
            playMinutesToday += event.getPlayMinutes() != null ? event.getPlayMinutes() : 0;
            pvpWinsToday += event.getPvpWins() != null ? event.getPvpWins() : 0;
            coopMinutesToday += event.getCoopMinutes() != null ? event.getCoopMinutes() : 0;
            topupTryToday += event.getTopupTry() != null ? event.getTopupTry() : 0.0;
        }

        userState.setLoginCountToday(loginCountToday);
        userState.setPlayMinutesToday(playMinutesToday);
        userState.setPvpWinsToday(pvpWinsToday);
        userState.setCoopMinutesToday(coopMinutesToday);
        userState.setTopupTryToday(topupTryToday);
    }

    private void calculateLast7DaysMetrics(UserState userState, List<ActivityEvent> last7DaysEvents) {
        int playMinutes7d = 0;
        double topupTry7d = 0.0;
        int logins7d = 0;

        for (ActivityEvent event : last7DaysEvents) {
            playMinutes7d += event.getPlayMinutes() != null ? event.getPlayMinutes() : 0;
            topupTry7d += event.getTopupTry() != null ? event.getTopupTry() : 0.0;
            if (event.getLoginCount() != null && event.getLoginCount() > 0) {
                logins7d++;
            }
        }

        userState.setPlayMinutes7d(playMinutes7d);
        userState.setTopupTry7d(topupTry7d);
        userState.setLogins7d(logins7d);
    }

    private void calculateStreak(UserState userState, String userId, LocalDate asOfDate) {
        int streakDays = 0;
        LocalDate currentDate = asOfDate;
        
        // Get events ordered by date descending
        List<ActivityEvent> events = activityEventRepository
                .findByUserIdAndDateLessThanEqualOrderByDateDesc(userId, asOfDate);
        
        // Count consecutive days with login_count >= 1 going backwards
        for (ActivityEvent event : events) {
            if (event.getLoginCount() != null && event.getLoginCount() >= 1) {
                // Check if this event is on the expected date (consecutive)
                if (event.getDate().equals(currentDate) || 
                    event.getDate().equals(currentDate.minusDays(streakDays))) {
                    streakDays++;
                    currentDate = event.getDate().minusDays(1);
                } else {
                    // Streak broken
                    break;
                }
            } else {
                // No login on this day, streak broken
                break;
            }
        }
        
        userState.setLoginStreakDays(streakDays);
    }
}

