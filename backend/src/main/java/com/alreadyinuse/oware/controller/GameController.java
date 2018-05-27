package com.alreadyinuse.oware.controller;

import com.alreadyinuse.oware.service.GameService;
import com.alreadyinuse.oware.exception.GameException;
import com.alreadyinuse.oware.model.Game;
import com.alreadyinuse.oware.rest.NewGameRequest;
import com.alreadyinuse.oware.rest.UpdateGameRequest;
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
