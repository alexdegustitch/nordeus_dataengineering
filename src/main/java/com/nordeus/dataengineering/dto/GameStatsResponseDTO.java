package com.nordeus.dataengineering.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameStatsResponseDTO {

    private int numberOfTransactions;
    private int numberOfLogins;
    private int numberOfActiveUsers;
    private double totalRevenue;

}
