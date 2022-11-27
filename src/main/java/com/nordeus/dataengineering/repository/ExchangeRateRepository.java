package com.nordeus.dataengineering.repository;

import com.nordeus.dataengineering.model.ExchangeRateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRateModel, Long> {
}
