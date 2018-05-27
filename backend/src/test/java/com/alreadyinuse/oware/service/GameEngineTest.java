package com.alreadyinuse.oware.service;

import com.alreadyinuse.oware.exception.GameException;
import com.alreadyinuse.oware.model.Game;
import com.alreadyinuse.oware.model.Pit;
import com.alreadyinuse.oware.model.Player;
import com.alreadyinuse.oware.repository.GameRepository;
import com.alreadyinuse.oware.rest.UpdateGameRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GameEngineTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameEngine gameEngine;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldUpdateGame() throws GameException {
        UpdateGameRequest update = UpdateGameRequest.builder()
                .pitId(2)
                .pitIndex(1)
                .player(1)
                .build();
        Game game = Game.builder()
                .id(1L)
                .win(0)
                .turn(1)
                .player1(setupPlayer(1))
                .player2(setupPlayer(2))
                .build();
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        Game actual = gameEngine.updateGame(1L, update);

        assertEquals(Integer.valueOf(2), actual.getTurn());
        assertEquals(Integer.valueOf(0), actual.getWin());
        assertEquals(Integer.valueOf(1), actual.getPlayer1().getBigPit().getStones());
        assertEquals(Integer.valueOf(0), findStonesOnPitById(actual.getPlayer1(), 2));
    }

    private Integer findStonesOnPitById(Player player, Integer pitId){
        return player.getSmallPits().stream()
                .filter(pit -> pit.getPitId() == 2)
                .findFirst().get()
                .getStones();
    }

    private Player setupPlayer(Integer player) {
        return Player.builder()
                .playerId(1L)
                .name("Player " + player)
                .smallPits(buildSmallPits(player))
                .bigPit(Pit.builder()
                        .pitIndex(0)
                        .pitId(player*10)
                        .stones(0)
                        .isSmall(false)
                        .build())
                .build();
    }

    private List<Pit> buildSmallPits(Integer player) {
        return IntStream.range(1, 7)
                .boxed()
                .map(i -> Pit.builder()
                        .pitId((i+1) * player)
                        .pitIndex(i)
                        .isSmall(true)
                        .stones(6).build())
                .collect(toList());
    }
}