package com.turkcell.gameplus.service;

import com.turkcell.gameplus.model.PointsLedgerEntry;
import com.turkcell.gameplus.model.User;
import com.turkcell.gameplus.repository.PointsLedgerEntryRepository;
import com.turkcell.gameplus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointsLedgerService {

    private final PointsLedgerEntryRepository pointsLedgerEntryRepository;
    private final UserRepository userRepository;

    @Transactional
    public PointsLedgerEntry addPoints(String userId, Integer pointsDelta, String source, String sourceRef) {
        log.debug("Adding {} points to user {} from source {} (ref: {})", pointsDelta, userId, source, sourceRef);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        PointsLedgerEntry entry = new PointsLedgerEntry();
        entry.setLedgerId("L-" + UUID.randomUUID().toString());
        entry.setUser(user);
        entry.setUserId(userId);
        entry.setPointsDelta(pointsDelta);
        entry.setSource(source);
        entry.setSourceRef(sourceRef);

        PointsLedgerEntry savedEntry = pointsLedgerEntryRepository.save(entry);
        log.debug("Points ledger entry created: {}", savedEntry.getLedgerId());
        
        return savedEntry;
    }

    public Integer getTotalPoints(String userId) {
        Integer totalPoints = pointsLedgerEntryRepository.getTotalPointsByUserId(userId);
        return totalPoints != null ? totalPoints : 0;
    }

    public Map<String, Integer> getTotalPointsForAllUsers() {
        List<Object[]> results = pointsLedgerEntryRepository.getTotalPointsForAllUsers();
        Map<String, Integer> userPoints = new HashMap<>();
        
        for (Object[] result : results) {
            String userId = (String) result[0];
            Integer totalPoints = ((Number) result[1]).intValue();
            userPoints.put(userId, totalPoints);
        }
        
        return userPoints;
    }

    public List<PointsLedgerEntry> getUserLedgerHistory(String userId) {
        return pointsLedgerEntryRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}

