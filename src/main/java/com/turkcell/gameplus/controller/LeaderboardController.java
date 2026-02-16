package com.turkcell.gameplus.controller;

import com.turkcell.gameplus.model.LeaderboardEntry;
import com.turkcell.gameplus.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(
            @RequestParam(required = false) String date,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        
        LocalDate asOfDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        List<LeaderboardEntry> leaderboard = leaderboardService.getLeaderboard(asOfDate, limit);
        return ResponseEntity.ok(leaderboard);
    }
}

