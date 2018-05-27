package com.alreadyinuse.oware.service;

import com.alreadyinuse.oware.enums.TurnEnum;
import com.alreadyinuse.oware.enums.WinEnum;
import com.alreadyinuse.oware.model.Game;
import com.alreadyinuse.oware.model.Pit;
import com.alreadyinuse.oware.model.Player;
import com.alreadyinuse.oware.repository.GameRepository;
import com.alreadyinuse.oware.rest.UpdateGameRequest;
import com.alreadyinuse.oware.exception.GameException;
import com.alreadyinuse.oware.helper.CircularList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.alreadyinuse.oware.enums.TurnEnum.PLAYER_ONE_TURN;
import static com.alreadyinuse.oware.enums.TurnEnum.PLAYER_TWO_TURN;
import static com.alreadyinuse.oware.enums.WinEnum.DRAW;
import static com.alreadyinuse.oware.enums.WinEnum.PLAYER_TWO_WINS;
import static com.alreadyinuse.oware.enums.WinEnum.PLAYRE_ONE_WINS;

@Service
public class GameEngine {

    public Game updateGame(Game currentGame, UpdateGameRequest update) {
        List<Pit> pitsList = createCircularList(currentGame);

        boolean turnChanged = true;

        //Get number of stones
        Player currentPlayer = getCurrentPlayer(currentGame);
        Pit chosenPit = getChosenPit(currentPlayer, update.getPitIndex());
        int stones = chosenPit.getStones();
        int startIndex = pitsList.indexOf(chosenPit);

        //Illegal move
        if(chosenPit.getStones() == 0 || notHisPit(currentPlayer, update.getPitId())) {
            return currentGame;
        }

        //Empty chosen pit
        emptyPit(currentPlayer, update.getPitIndex());

        //Sow the stones
        for(int i = 1; i <= stones; i++) {
            Pit tempPit = pitsList.get(startIndex - i);
            
            //Skip adversary's big pit
            if(!tempPit.getIsSmall() && !isOwnBigPit(tempPit, currentPlayer)) continue;

            //Last stone in own big bit | get another turn
            if(i == stones && isOwnBigPit(tempPit, currentPlayer)) turnChanged = false;

            //Last stone in own empty pit | capture stones from adversary
            if(i == stones && isOwnEmptyPit(tempPit, currentPlayer)) {
                Player otherPlayer = getOtherPlayer(currentGame);
                Pit pitToBeCaptured = otherPlayer.getSmallPits().get(6 - tempPit.getPitIndex());
                tempPit.setStones(pitToBeCaptured.getStones());
                pitToBeCaptured.setStones(0);
            }

            tempPit.setStones(tempPit.getStones() + 1);
        }

        currentGame.setTurn(changeTurn(currentGame.getTurn(), turnChanged));

        // Process end game
        if(isGameFinished(currentGame)) {
            updateBigPits(currentGame.getPlayer1());
            updateBigPits(currentGame.getPlayer2());
            currentGame.setWin(getWinner(currentGame.getPlayer1().getBigPit().getStones(), currentGame.getPlayer2().getBigPit().getStones()));
        }

        return currentGame;
    }

    private void updateBigPits(Player player) {
        Integer stonesFromSmallPits = gatherAllStonesFromSmallPits(player);
        player.getBigPit().setStones(player.getBigPit().getStones() + stonesFromSmallPits);
    }

    private boolean isOwnEmptyPit(Pit pit, Player currentPlayer) {
        return currentPlayer.getSmallPits().stream().anyMatch(ownPit -> ownPit.equals(pit)) && pit.getStones() == 0;
    }

    private boolean isOwnBigPit(Pit pit, Player currentPlayer) {
        return currentPlayer.getBigPit().equals(pit);
    }

    private TurnEnum changeTurn(TurnEnum turn, Boolean turnChanged) {
        return turnChanged ? turn.equals(PLAYER_ONE_TURN) ? PLAYER_TWO_TURN : PLAYER_ONE_TURN : turn;
    }

    private WinEnum getWinner(Integer player1Stones, Integer player2Stones) {
        return player1Stones.equals(player2Stones) ? DRAW :
                player1Stones > player2Stones ? PLAYRE_ONE_WINS : PLAYER_TWO_WINS;
    }

    private Integer gatherAllStonesFromSmallPits(Player player) {
        int totalStones = player.getSmallPits().stream().mapToInt(Pit::getStones).sum();

        player.getSmallPits().forEach(pit -> pit.setStones(0));

        return totalStones;
    }

    private Boolean isGameFinished(Game game) {
        return allPitsEmpty(game.getPlayer1()) || allPitsEmpty(game.getPlayer2());
    }

    private boolean notHisPit(Player player, Integer pitId) {
        return player.getSmallPits().stream().noneMatch(pit -> pit.getPitId().equals(pitId));
    }

    private Pit getChosenPit(Player player, Integer index) {
        return player.getSmallPits().stream().filter(pit -> pit.getPitIndex().equals(index)).findFirst().get();
    }

    private void emptyPit(Player player, Integer pitIndex) {
        player.getSmallPits().stream()
                .filter(pit -> pit.getPitIndex().equals(pitIndex))
                .findFirst()
                .get()
                .setStones(0);
    }

    private Player getCurrentPlayer(Game game) {
        return game.getTurn().equals(PLAYER_ONE_TURN) ? game.getPlayer1() : game.getPlayer2();
    }

    private Player getOtherPlayer(Game game) {
        return game.getTurn().equals(PLAYER_TWO_TURN) ? game.getPlayer1() : game.getPlayer2();
    }

    private List<Pit> createCircularList(Game game) {
        List<Pit> circularPits = new CircularList<>();
        circularPits.add(game.getPlayer1().getBigPit());
        circularPits.addAll(game.getPlayer1().getSmallPits());
        circularPits.add(game.getPlayer2().getBigPit());
        circularPits.addAll(game.getPlayer2().getSmallPits());
        return circularPits;
    }

    private boolean allPitsEmpty(Player player) {
        return player.getSmallPits()
                .stream()
                .allMatch(pit -> pit.getStones().equals(0));
    }
}
