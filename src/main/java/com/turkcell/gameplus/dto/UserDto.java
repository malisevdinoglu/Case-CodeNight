package com.turkcell.gameplus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String name;
    private String city;
    private String segment;
    private Integer totalPoints;
}

