package com.nordeus.dataengineering.service;

import com.nordeus.dataengineering.model.CurrencyModel;
import com.nordeus.dataengineering.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService{

    @Autowired
    private CurrencyRepository currencyRepository;

    public CurrencyModel saveCurrency(CurrencyModel currencyModel){
        return currencyRepository.save(currencyModel);
    }
}
