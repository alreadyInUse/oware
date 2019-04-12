package com.bogdansucaciu.oware.service;

import com.bogdansucaciu.oware.repository.GameRepository;
import com.bogdansucaciu.oware.exception.GameException;
import com.bogdansucaciu.oware.model.Game;
import com.bogdansucaciu.oware.rest.NewGameRequest;
import com.bogdansucaciu.oware.model.Pit;
import com.bogdansucaciu.oware.model.Player;
import com.bogdansucaciu.oware.rest.UpdateGameRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.bogdansucaciu.oware.enums.TurnEnum.PLAYER_ONE_TURN;
import static com.bogdansucaciu.oware.enums.WinEnum.UNFINISHED;
import static java.util.stream.Collectors.toList;

@Service
public class GameService {

    private GameEngine gameEngine;

    private GameRepository gameRepository;

    private Function<Integer, Pit> toNewPit = i -> Pit.builder()
            .pitIndex(i)
            .stones(6)
            .isSmall(true)
            .build();

    @Autowired
    public GameService(GameEngine gameEngine, GameRepository gameRepository) {
        this.gameEngine = gameEngine;
        this.gameRepository = gameRepository;
    }

    public Game startNewGame(NewGameRequest newGameRequest) {
        Game newGame = Game.builder()
                .turn(PLAYER_ONE_TURN)
                .win(UNFINISHED)
                .player1(setupNewPlayer(newGameRequest.getPlayer1Name()))
                .player2(setupNewPlayer(newGameRequest.getPlayer2Name()))
                .build();

        return gameRepository.save(newGame);
    }

    public Game getGame(Long gameId) throws GameException {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameException("Game not found ! Please start a new game !"));
    }

    public Game updateGame(Long gameId, UpdateGameRequest update) throws GameException {
        Optional<Game> currentGame = gameRepository.findById(gameId);
        return currentGame.map(game -> gameEngine.updateGame(game, update))
                          .map(game -> { gameRepository.save(game);
                                        return game; })
                          .orElseThrow(() -> new GameException("An error occurred ! Please start a new game !"));
    }

    private Player setupNewPlayer(String name) {
        return Player.builder()
                .name(name)
                .smallPits(getNewSmallPits())
                .bigPit(getNewBigPit())
                .build();
    }

    private List<Pit> getNewSmallPits() {
        return IntStream.range(1, 7)
                .boxed()
                .map(toNewPit)
                .collect(toList());
    }

    private Pit getNewBigPit() {
        return Pit.builder()
                .pitIndex(0)
                .isSmall(false)
                .stones(0)
                .build();
    }
}
