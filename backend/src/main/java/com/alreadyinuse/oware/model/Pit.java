package com.alreadyinuse.oware.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Pit {

    @Id
    @GeneratedValue
    private Integer pitId;

    private Integer pitIndex;

    private Integer stones;

    private Boolean isSmall;

}
