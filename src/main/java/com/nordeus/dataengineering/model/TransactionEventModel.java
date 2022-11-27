package com.nordeus.dataengineering.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transaction_event")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TransactionEventModel extends EventModel {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "currency_iso_code")
    private String currencyISOCode;

    @Column(name = "transaction_amount")
    private double transactionAmount;

    public TransactionEventModel(long eventId, LocalDateTime eventTimestamp, String userId, String currencyISOCode, double transactionAmount) {
        super(eventId, eventTimestamp);
        this.userId = userId;
        this.currencyISOCode = currencyISOCode;
        this.transactionAmount = transactionAmount;
    }
}
