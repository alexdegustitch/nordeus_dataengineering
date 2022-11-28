package com.nordeus.dataengineering.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**

 * Entity class for currency table

 * @version 1.0

 * @author Aleksandar Paripovic

 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "currency")
public class CurrencyModel {

    @Id
    @Column(name = "currency_iso_code")
    private String currencyISOCode;
}
