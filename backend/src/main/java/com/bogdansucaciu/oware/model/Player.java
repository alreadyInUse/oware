package com.bogdansucaciu.oware.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue
    private Long playerId;

    private String name;

    @OneToMany(cascade= ALL, targetEntity=Pit.class, fetch= EAGER)
    @JoinColumn
    private List<Pit> smallPits;

    @OneToOne(cascade = ALL, fetch = EAGER)
    @JoinColumn
    private Pit bigPit;

}
