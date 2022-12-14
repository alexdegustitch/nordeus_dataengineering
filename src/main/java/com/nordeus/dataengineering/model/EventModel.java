package com.nordeus.dataengineering.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**

 * Entity class for event table

 * @version 1.0

 * @author Aleksandar Paripovic

 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "event")
public abstract class EventModel {

    @Id
    @Column(name = "event_id")
    private long eventId;

    @Column(name = "event_timestamp")
    private LocalDateTime eventTimestamp;
}
