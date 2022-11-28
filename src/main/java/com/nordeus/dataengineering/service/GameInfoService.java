package com.nordeus.dataengineering.service;

import com.nordeus.dataengineering.dto.GameStatsByCountryResponseDTO;
import com.nordeus.dataengineering.dto.GameStatsRequestDTO;
import com.nordeus.dataengineering.dto.GameStatsResponseDTO;
import com.nordeus.dataengineering.repository.LoginEventRepository;
import com.nordeus.dataengineering.repository.TransactionEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GameInfoService {

    @Autowired
    private LoginEventRepository loginEventRepository;

    @Autowired
    private TransactionEventRepository transactionEventRepository;


    public GameStatsResponseDTO getGameStats(LocalDate date) {
        GameStatsResponseDTO gameStatsResponseDTO = new GameStatsResponseDTO();
        LoginEventRepository.GameInfo gameInfo;
        if (date == null) {
            gameInfo = loginEventRepository.getGameInfo();
        } else {
            gameInfo = loginEventRepository.getGameInfoForDate(date, date.plusDays(1));
        }
        gameStatsResponseDTO.setNumberOfLogins(gameInfo.getNumberOfLogins());
        gameStatsResponseDTO.setNumberOfActiveUsers(gameInfo.getNumberOfActiveUsers());
        gameStatsResponseDTO.setNumberOfTransactions(gameInfo.getNumberOfTransactions());
        gameStatsResponseDTO.setTotalRevenue(gameInfo.getTotalRevenue());
        return gameStatsResponseDTO;
    }

    public List<GameStatsByCountryResponseDTO> getGameStatsByCountry(LocalDate date) {
        List<GameStatsByCountryResponseDTO> gameStatsByCountryResponseDTOList = new ArrayList<>();

        List<LoginEventRepository.GameInfoCountries> gameInfoCountriesList;
        if (date == null) {
            gameInfoCountriesList = loginEventRepository.getGameInfoByCountry();
        } else {
            gameInfoCountriesList = loginEventRepository.getGameInfoByCountryForDate(date, date.plusDays(1));
        }

        for(LoginEventRepository.GameInfoCountries curr: gameInfoCountriesList){
            GameStatsByCountryResponseDTO gameStatsByCountryResponseDTO = new GameStatsByCountryResponseDTO();
            gameStatsByCountryResponseDTO.setCountry(curr.getCountry());
            gameStatsByCountryResponseDTO.setNumberOfLogins(curr.getNumberOfLogins());
            gameStatsByCountryResponseDTO.setNumberOfActiveUsers(curr.getNumberOfActiveUsers());
            gameStatsByCountryResponseDTO.setNumberOfTransactions(curr.getNumberOfTransactions());
            gameStatsByCountryResponseDTO.setTotalRevenue(curr.getTotalRevenue());
            gameStatsByCountryResponseDTOList.add(gameStatsByCountryResponseDTO);
        }

        return gameStatsByCountryResponseDTOList;
    }


}
