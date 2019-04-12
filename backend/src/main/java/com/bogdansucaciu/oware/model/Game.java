package com.bogdansucaciu.oware.model;

import com.bogdansucaciu.oware.enums.TurnEnum;
import com.bogdansucaciu.oware.enums.WinEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    private TurnEnum turn;

    private WinEnum win;

    @OneToOne(cascade = ALL, fetch = EAGER)
    @JoinColumn
    private Player player1;

    @OneToOne(cascade = ALL, fetch = EAGER)
    @JoinColumn
    private Player player2;

}
