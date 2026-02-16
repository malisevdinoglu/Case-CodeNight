package com.turkcell.gameplus.service;

import com.opencsv.exceptions.CsvException;
import com.turkcell.gameplus.model.*;
import com.turkcell.gameplus.repository.*;
import com.turkcell.gameplus.util.CsvUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService {

    private final CsvUtil csvUtil;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final ActivityEventRepository activityEventRepository;
    private final QuestRepository questRepository;
    private final BadgeRepository badgeRepository;

    @Value("${csv.seed.data.path}")
    private String seedDataPath;

    @Transactional
    public void importFromCsv() throws IOException, CsvException {
        log.info("Starting CSV import...");
        
        importUsers();
        importGames();
        importQuests();
        importBadges();
        importActivityEvents();
        
        log.info("CSV import completed successfully");
    }

    private void importUsers() throws IOException, CsvException {
        String filePath = seedDataPath + "users.csv";
        List<String[]> records = csvUtil.readCsv(filePath);
        
        for (String[] record : records) {
            if (record.length >= 4) {
                User user = new User();
                user.setUserId(record[0]);
                user.setName(record[1]);
                user.setCity(record[2]);
                user.setSegment(record[3]);
                userRepository.save(user);
            }
        }
        log.info("Imported {} users", records.size());
    }

    private void importGames() throws IOException, CsvException {
        String filePath = seedDataPath + "games.csv";
        List<String[]> records = csvUtil.readCsv(filePath);
        
        for (String[] record : records) {
            if (record.length >= 3) {
                Game game = new Game();
                game.setGameId(record[0]);
                game.setGameName(record[1]);
                game.setGenre(record[2]);
                gameRepository.save(game);
            }
        }
        log.info("Imported {} games", records.size());
    }

    private void importQuests() throws IOException, CsvException {
        String filePath = seedDataPath + "quests.csv";
        List<String[]> records = csvUtil.readCsv(filePath);
        
        for (String[] record : records) {
            if (record.length >= 7) {
                Quest quest = new Quest();
                quest.setQuestId(record[0]);
                quest.setQuestName(record[1]);
                quest.setQuestType(record[2]);
                quest.setCondition(record[3]);
                quest.setRewardPoints(Integer.parseInt(record[4]));
                quest.setPriority(Integer.parseInt(record[5]));
                quest.setIsActive(Boolean.parseBoolean(record[6]));
                questRepository.save(quest);
            }
        }
        log.info("Imported {} quests", records.size());
    }

    private void importBadges() throws IOException, CsvException {
        String filePath = seedDataPath + "badges.csv";
        List<String[]> records = csvUtil.readCsv(filePath);
        
        for (String[] record : records) {
            if (record.length >= 4) {
                Badge badge = new Badge();
                badge.setBadgeId(record[0]);
                badge.setBadgeName(record[1]);
                badge.setCondition(record[2]);
                badge.setLevel(Integer.parseInt(record[3]));
                badgeRepository.save(badge);
            }
        }
        log.info("Imported {} badges", records.size());
    }

    private void importActivityEvents() throws IOException, CsvException {
        String filePath = seedDataPath + "activity_events.csv";
        List<String[]> records = csvUtil.readCsv(filePath);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (String[] record : records) {
            if (record.length >= 9) {
                ActivityEvent event = new ActivityEvent();
                event.setEventId(record[0]);
                
                User user = userRepository.findById(record[1]).orElse(null);
                if (user != null) {
                    event.setUser(user);
                    event.setUserId(record[1]);
                }
                
                event.setDate(LocalDate.parse(record[2], formatter));
                
                Game game = gameRepository.findById(record[3]).orElse(null);
                if (game != null) {
                    event.setGame(game);
                    event.setGameId(record[3]);
                }
                
                event.setLoginCount(Integer.parseInt(record[4]));
                event.setPlayMinutes(Integer.parseInt(record[5]));
                event.setPvpWins(Integer.parseInt(record[6]));
                event.setCoopMinutes(Integer.parseInt(record[7]));
                event.setTopupTry(Double.parseDouble(record[8]));
                
                activityEventRepository.save(event);
            }
        }
        log.info("Imported {} activity events", records.size());
    }
}

