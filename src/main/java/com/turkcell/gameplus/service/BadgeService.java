package com.turkcell.gameplus.service;

import com.turkcell.gameplus.model.Badge;
import com.turkcell.gameplus.model.BadgeAward;
import com.turkcell.gameplus.model.BadgeAwardId;
import com.turkcell.gameplus.model.User;
import com.turkcell.gameplus.repository.BadgeAwardRepository;
import com.turkcell.gameplus.repository.BadgeRepository;
import com.turkcell.gameplus.repository.UserRepository;
import com.turkcell.gameplus.util.ConditionEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final BadgeAwardRepository badgeAwardRepository;
    private final UserRepository userRepository;
    private final ConditionEvaluator conditionEvaluator;
    private final PointsLedgerService pointsLedgerService;

    @Transactional
    public List<BadgeAward> evaluateBadges(String userId, LocalDate asOfDate) {
        log.debug("Evaluating badges for user: {} as of date: {}", userId, asOfDate);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Get total points for user
        Integer totalPoints = pointsLedgerService.getTotalPoints(userId);
        log.debug("User {} has {} total points", userId, totalPoints);

        // Load all badges
        List<Badge> badges = badgeRepository.findAll();
        log.debug("Found {} badges to evaluate", badges.size());

        // Get existing badge awards for user
        List<BadgeAward> existingAwards = badgeAwardRepository.findByUserId(userId);
        List<String> existingBadgeIds = existingAwards.stream()
                .map(BadgeAward::getBadgeId)
                .toList();

        // Evaluate badges and create awards for newly earned ones
        List<BadgeAward> newAwards = new ArrayList<>();
        for (Badge badge : badges) {
            // Skip if user already has this badge
            if (existingBadgeIds.contains(badge.getBadgeId())) {
                continue;
            }

            // Evaluate badge condition
            if (conditionEvaluator.evaluateTotalPointsCondition(badge.getCondition(), totalPoints)) {
                log.info("Badge earned: {} by user: {}", badge.getBadgeName(), userId);
                
                BadgeAward award = new BadgeAward();
                award.setUser(user);
                award.setUserId(userId);
                award.setBadge(badge);
                award.setBadgeId(badge.getBadgeId());

                BadgeAward savedAward = badgeAwardRepository.save(award);
                newAwards.add(savedAward);
            }
        }

        log.info("User {} earned {} new badges", userId, newAwards.size());
        return newAwards;
    }

    public List<BadgeAward> getUserBadges(String userId) {
        return badgeAwardRepository.findByUserId(userId);
    }
}

