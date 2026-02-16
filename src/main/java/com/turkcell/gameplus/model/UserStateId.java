package com.turkcell.gameplus.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStateId implements Serializable {
    private String userId;
    private LocalDate asOfDate;
}

