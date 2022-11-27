package com.nordeus.dataengineering.repository;

import com.nordeus.dataengineering.model.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EventRepository extends JpaRepository<EventModel, Long> {
}
