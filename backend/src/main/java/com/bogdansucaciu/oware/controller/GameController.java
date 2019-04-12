package com.bogdansucaciu.oware.controller;

import com.bogdansucaciu.oware.service.GameService;
import com.bogdansucaciu.oware.exception.GameException;
import com.bogdansucaciu.oware.model.Game;
import com.bogdansucaciu.oware.rest.NewGameRequest;
import com.bogdansucaciu.oware.rest.UpdateGameRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController (GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/newgame")
    public Game startNewGame(@RequestBody NewGameRequest newGameRequest) {
        return gameService.startNewGame(newGameRequest);
    }

    @GetMapping("/game/{id}")
    public Game getGame(@PathVariable(value = "id") Long gameId) throws GameException {
        return gameService.getGame(gameId);
    }

    @PutMapping("/game/{id}")
    public Game updateGame(@PathVariable(value = "id") Long gameId, @RequestBody UpdateGameRequest update) throws GameException {
        return gameService.updateGame(gameId, update);
    }
}
