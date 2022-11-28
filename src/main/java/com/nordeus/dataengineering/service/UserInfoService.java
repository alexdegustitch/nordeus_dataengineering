package com.nordeus.dataengineering.service;


import com.nordeus.dataengineering.dto.UserStatsResponseDTO;
import com.nordeus.dataengineering.model.UserModel;
import com.nordeus.dataengineering.repository.LoginEventRepository;
import com.nordeus.dataengineering.repository.TransactionEventRepository;
import com.nordeus.dataengineering.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class UserInfoService {
    @Autowired
    private LoginEventRepository loginEventRepository;

    @Autowired
    private TransactionEventRepository transactionEventRepository;

    @Autowired
    private UserRepository userRepository;


    public UserStatsResponseDTO getUserStats(String userId, LocalDate date) {
        UserModel userModel = userRepository.findById(userId).orElse(null);
        if (userModel == null)
            return null;

        TransactionEventRepository.TransactionInfo transactionInfo;
        UserStatsResponseDTO userStatsResponseDTO = new UserStatsResponseDTO();
        userStatsResponseDTO.setCountry(userModel.getCountry());
        userStatsResponseDTO.setName(userModel.getName());

        long dayDiff;
        int loginCount;

        if (date == null) {
            loginCount = loginEventRepository.getUserLoginCount(userId);
            LocalDateTime lastLogin = loginEventRepository.getUserLastLogin(userId);
            if (lastLogin == null) {
                dayDiff = -1;
            } else {
                dayDiff = loginEventRepository.getDiffDays(lastLogin);
            }
            transactionInfo = transactionEventRepository.getUserTransactionInfo(userId);

        } else {
            loginCount = loginEventRepository.getUserLoginCountForDate(userId, date, date.plusDays(1));
            LocalDateTime lastLogin = loginEventRepository.getUserLastLoginBeforeDate(userId, date.plusDays(1));
            if(lastLogin == null){
                dayDiff = -1;
            }else{
                dayDiff = ChronoUnit.DAYS.between(lastLogin.toLocalDate(), date);
            }
            transactionInfo = transactionEventRepository.getUserTransactionInfoForDate(userId, date, date.plusDays(1));
        }

        userStatsResponseDTO.setDayDiff(dayDiff);
        userStatsResponseDTO.setNumberOfLogins(loginCount);
        userStatsResponseDTO.setNumberOfTransactions(transactionInfo.getNumberOfTransactions());
        userStatsResponseDTO.setTotalRevenue(transactionInfo.getTotalRevenue());


        return userStatsResponseDTO;
    }
}
