package com.nordeus.dataengineering.service;


import com.nordeus.dataengineering.model.ExchangeRateModel;
import com.nordeus.dataengineering.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateModel saveExchangeRate(ExchangeRateModel exchangeRateModel){
        return exchangeRateRepository.save(exchangeRateModel);
    }
}
