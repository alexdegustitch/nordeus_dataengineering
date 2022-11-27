package com.nordeus.dataengineering.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class UserModel {

    @Id
    @Column(name = "user_id")
    private String userId;

    private String name;

    private String country;

    @Column(name = "device_os")
    private String deviceOs;



}
