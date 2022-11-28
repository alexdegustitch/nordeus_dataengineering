package com.nordeus.dataengineering.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserStatsResponseDTO {
    private String name;
    private String country;
    private int numberOfLogins;
    private int numberOfTransactions;
    private long dayDiff;
    private double totalRevenue;
}
