package com.nordeus.dataengineering.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**

 * Entity class for login_event table

 * @version 1.0

 * @author Aleksandar Paripovic

 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "login_event")
@OnDelete(action = OnDeleteAction.CASCADE)
public class LoginEventModel extends EventModel {

    @Column(name = "user_id")
    private String userId;

    public LoginEventModel(long eventId, LocalDateTime eventTimestamp, String userId) {
        super(eventId, eventTimestamp);
        this.userId = userId;
    }

}