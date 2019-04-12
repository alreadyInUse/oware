package com.bogdansucaciu.oware.service;

import com.bogdansucaciu.oware.exception.GameException;
import com.bogdansucaciu.oware.model.Game;
import com.bogdansucaciu.oware.repository.GameRepository;
import com.bogdansucaciu.oware.rest.NewGameRequest;
import com.bogdansucaciu.oware.rest.UpdateGameRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GameServiceTest {

    private Game game;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameEngine gameEngine;

    @InjectMocks
    private GameService gameService;

    @Before
    public void setup(){
        initMocks(this);
        game = Game.builder()
                .id(1L)
                .build();
    }

    @Test
    public void shouldStartNewGame(){
        NewGameRequest newGameRequest = NewGameRequest.builder()
                .player1Name("Player 1")
                .player2Name("Player 2")
                .build();
        Game game = Game.builder()
                .id(1L)
                .build();
        when(gameRepository.save(any())).thenReturn(game);

        Game actual = gameService.startNewGame(newGameRequest);

        assertEquals(Long.valueOf(1), actual.getId());
    }

    @Test
    public void shouldGetGame() throws GameException {
        Game game = Game.builder()
                .id(1L)
                .build();
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        Game actual = gameService.getGame(1L);

        assertEquals(Long.valueOf(1), actual.getId());
    }

    @Test(expected = GameException.class)
    public void shouldThrowGameExceptionWhenGettingAGame() throws GameException {
        gameService.getGame(1L);
    }

    @Test
    public void shouldUpdateGame() throws GameException {
        UpdateGameRequest updateGameRequest = UpdateGameRequest.builder()
                .pitId(1)
                .pitIndex(1)
                .player(1)
                .build();
        when(gameEngine.updateGame(1L, updateGameRequest)).thenReturn(game);

        gameService.updateGame(1L, updateGameRequest);

        verify(gameEngine, times(1)).updateGame(1L, updateGameRequest);
    }
}
