-- Create tables for Game+ Quest League System

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100),
    segment VARCHAR(50)
);

-- Games table
CREATE TABLE IF NOT EXISTS games (
    game_id VARCHAR(50) PRIMARY KEY,
    game_name VARCHAR(100) NOT NULL,
    genre VARCHAR(50)
);

-- Activity Events table
CREATE TABLE IF NOT EXISTS activity_events (
    event_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    date DATE NOT NULL,
    game_id VARCHAR(50) NOT NULL,
    login_count INTEGER DEFAULT 0,
    play_minutes INTEGER DEFAULT 0,
    pvp_wins INTEGER DEFAULT 0,
    coop_minutes INTEGER DEFAULT 0,
    topup_try DECIMAL(10,2) DEFAULT 0,
    CONSTRAINT fk_activity_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_activity_game FOREIGN KEY (game_id) REFERENCES games(game_id)
);

-- Quests table
CREATE TABLE IF NOT EXISTS quests (
    quest_id VARCHAR(50) PRIMARY KEY,
    quest_name VARCHAR(200) NOT NULL,
    quest_type VARCHAR(50) NOT NULL,
    condition TEXT NOT NULL,
    reward_points INTEGER NOT NULL,
    priority INTEGER NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

-- Badges table
CREATE TABLE IF NOT EXISTS badges (
    badge_id VARCHAR(50) PRIMARY KEY,
    badge_name VARCHAR(100) NOT NULL,
    condition TEXT NOT NULL,
    level INTEGER NOT NULL
);

-- User States table
CREATE TABLE IF NOT EXISTS user_states (
    user_id VARCHAR(50) NOT NULL,
    as_of_date DATE NOT NULL,
    login_count_today INTEGER DEFAULT 0,
    play_minutes_today INTEGER DEFAULT 0,
    pvp_wins_today INTEGER DEFAULT 0,
    coop_minutes_today INTEGER DEFAULT 0,
    topup_try_today DECIMAL(10,2) DEFAULT 0,
    play_minutes_7d INTEGER DEFAULT 0,
    topup_try_7d DECIMAL(10,2) DEFAULT 0,
    logins_7d INTEGER DEFAULT 0,
    login_streak_days INTEGER DEFAULT 0,
    PRIMARY KEY (user_id, as_of_date),
    CONSTRAINT fk_user_state_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Quest Awards table
CREATE TABLE IF NOT EXISTS quest_awards (
    award_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    as_of_date DATE NOT NULL,
    triggered_quests TEXT,
    selected_quest VARCHAR(50),
    reward_points INTEGER DEFAULT 0,
    suppressed_quests TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_quest_award_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_quest_award_quest FOREIGN KEY (selected_quest) REFERENCES quests(quest_id)
);

-- Points Ledger table
CREATE TABLE IF NOT EXISTS points_ledger (
    ledger_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    points_delta INTEGER NOT NULL,
    source VARCHAR(50) NOT NULL,
    source_ref VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ledger_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Badge Awards table
CREATE TABLE IF NOT EXISTS badge_awards (
    user_id VARCHAR(50) NOT NULL,
    badge_id VARCHAR(50) NOT NULL,
    awarded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, badge_id),
    CONSTRAINT fk_badge_award_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_badge_award_badge FOREIGN KEY (badge_id) REFERENCES badges(badge_id)
);

-- Notifications table
CREATE TABLE IF NOT EXISTS notifications (
    notification_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    channel VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Leaderboard table
CREATE TABLE IF NOT EXISTS leaderboard (
    rank INTEGER NOT NULL,
    user_id VARCHAR(50) NOT NULL,
    total_points INTEGER NOT NULL,
    as_of_date DATE NOT NULL,
    PRIMARY KEY (rank, user_id, as_of_date),
    CONSTRAINT fk_leaderboard_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_activity_events_user_date ON activity_events(user_id, date);
CREATE INDEX IF NOT EXISTS idx_activity_events_date ON activity_events(date);
CREATE INDEX IF NOT EXISTS idx_user_states_user_date ON user_states(user_id, as_of_date);
CREATE INDEX IF NOT EXISTS idx_quest_awards_user_date ON quest_awards(user_id, as_of_date);
CREATE INDEX IF NOT EXISTS idx_points_ledger_user ON points_ledger(user_id);
CREATE INDEX IF NOT EXISTS idx_points_ledger_created_at ON points_ledger(created_at);
CREATE INDEX IF NOT EXISTS idx_badge_awards_user ON badge_awards(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_leaderboard_date ON leaderboard(as_of_date);
CREATE INDEX IF NOT EXISTS idx_leaderboard_points ON leaderboard(total_points DESC, user_id ASC);

