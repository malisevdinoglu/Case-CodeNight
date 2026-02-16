package com.turkcell.gameplus.util;

import com.turkcell.gameplus.model.UserState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ConditionEvaluator {

    private static final Pattern CONDITION_PATTERN = Pattern.compile(
            "([a-zA-Z_0-9]+)\\s*(>=|<=|==|>|<)\\s*(\\d+(?:\\.\\d+)?)"
    );

    public boolean evaluate(String condition, UserState userState) {
        if (condition == null || condition.trim().isEmpty()) {
            return false;
        }

        try {
            Matcher matcher = CONDITION_PATTERN.matcher(condition.trim());
            if (!matcher.matches()) {
                log.warn("Condition pattern does not match: {}", condition);
                return false;
            }

            String fieldName = matcher.group(1);
            String operator = matcher.group(2);
            String valueStr = matcher.group(3);

            // Get field value from UserState using reflection
            Object fieldValue = getFieldValue(userState, fieldName);
            if (fieldValue == null) {
                log.warn("Field {} not found or null in UserState", fieldName);
                return false;
            }

            // Parse the threshold value
            double threshold;
            double actualValue;

            if (fieldValue instanceof Integer) {
                threshold = Double.parseDouble(valueStr);
                actualValue = ((Integer) fieldValue).doubleValue();
            } else if (fieldValue instanceof Double) {
                threshold = Double.parseDouble(valueStr);
                actualValue = (Double) fieldValue;
            } else {
                log.warn("Unsupported field type: {}", fieldValue.getClass());
                return false;
            }

            // Evaluate condition
            return evaluateOperator(actualValue, operator, threshold);

        } catch (Exception e) {
            log.error("Error evaluating condition: {}", condition, e);
            return false;
        }
    }

    private Object getFieldValue(UserState userState, String fieldName) {
        try {
            // Map field names from condition to actual field names
            String actualFieldName = mapFieldName(fieldName);
            
            Field field = UserState.class.getDeclaredField(actualFieldName);
            field.setAccessible(true);
            return field.get(userState);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Error accessing field: {}", fieldName, e);
            return null;
        }
    }

    private String mapFieldName(String conditionFieldName) {
        // Map condition field names to UserState field names
        switch (conditionFieldName) {
            case "login_count_today":
                return "loginCountToday";
            case "play_minutes_today":
                return "playMinutesToday";
            case "pvp_wins_today":
                return "pvpWinsToday";
            case "coop_minutes_today":
                return "coopMinutesToday";
            case "topup_try_today":
                return "topupTryToday";
            case "play_minutes_7d":
                return "playMinutes7d";
            case "topup_try_7d":
                return "topupTry7d";
            case "logins_7d":
                return "logins7d";
            case "login_streak_days":
                return "loginStreakDays";
            case "total_points":
                // This will be handled separately as it's not in UserState
                return null;
            default:
                return conditionFieldName;
        }
    }

    private boolean evaluateOperator(double actualValue, String operator, double threshold) {
        switch (operator) {
            case ">=":
                return actualValue >= threshold;
            case "<=":
                return actualValue <= threshold;
            case "==":
                return Math.abs(actualValue - threshold) < 0.0001; // Float comparison
            case ">":
                return actualValue > threshold;
            case "<":
                return actualValue < threshold;
            default:
                log.warn("Unsupported operator: {}", operator);
                return false;
        }
    }

    public boolean evaluateTotalPointsCondition(String condition, int totalPoints) {
        if (condition == null || condition.trim().isEmpty()) {
            return false;
        }

        try {
            Matcher matcher = CONDITION_PATTERN.matcher(condition.trim());
            if (!matcher.matches()) {
                return false;
            }

            String fieldName = matcher.group(1);
            if (!"total_points".equals(fieldName)) {
                return false;
            }

            String operator = matcher.group(2);
            double threshold = Double.parseDouble(matcher.group(3));

            return evaluateOperator(totalPoints, operator, threshold);

        } catch (Exception e) {
            log.error("Error evaluating total_points condition: {}", condition, e);
            return false;
        }
    }
}

