package com.turkcell.gameplus.controller;

import com.turkcell.gameplus.model.Quest;
import com.turkcell.gameplus.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

    private final QuestRepository questRepository;

    @GetMapping
    public ResponseEntity<List<Quest>> getAllQuests() {
        List<Quest> quests = questRepository.findAll();
        return ResponseEntity.ok(quests);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Quest>> getActiveQuests() {
        List<Quest> quests = questRepository.findByIsActiveTrue();
        return ResponseEntity.ok(quests);
    }
}

