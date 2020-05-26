///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package ahmedMLib.codingame;

import ahmedMLib.Errors.ErrorInMethod;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Ahmed Mazher
 */
@SuppressWarnings("serial")
public abstract class CodinGameEngine {

    private int turn = 0;

    public final void playGame() {
        setupGameData();
        List<CodinGamePlayer> teams = getTeams();
        if (teams == null || teams.isEmpty()) {
            throw new ErrorInMethod("playGame() CodinGameEngine", "team list are null or empty");
        }
        for (CodinGamePlayer a : teams) {
            a.initializePlayer(getInitializationGameData(a));
        }
        int canPlayCount = teams.size();
        while (!gameEnds() && canPlayCount > 0 && turn <= getMaxTurnCount()) {
            HashMap<CodinGamePlayer, String> playerMessages = new HashMap<>();
            for (CodinGamePlayer a : teams) {
                if (canPlay(a)) {
                    String msg = a.playTurn(getGameData(a));
                    playerMessages.put(a, msg);
                }
            }
            for (Map.Entry<CodinGamePlayer, String> msgEntry : playerMessages.entrySet()) {
                String errMsg = updateGameData(msgEntry.getValue(), msgEntry.getKey());
                System.err.println("message of team " + msgEntry.getKey() + " " + msgEntry);
                if (!errMsg.isEmpty()) {
                    setCanNotPlay(msgEntry.getKey());
                    canPlayCount--;
                    System.out.println(errMsg);
                }
            }
            resolveGameTrun();
            turn++;
        }
    }

    protected abstract void setupGameData();

    protected abstract Scanner getInitializationGameData(CodinGamePlayer t);

    protected abstract Scanner getGameData(CodinGamePlayer t);

    /**
     * this method update game data and should return error message if occurred
     * or "" if no error
     *
     * @param outputMsg
     * @param t
     * @return this method should return error message if occurred or "" if no
     * error
     */
    protected abstract String updateGameData(String outputMsg, CodinGamePlayer t);

    protected abstract void resolveGameTrun();

    protected abstract boolean gameEnds();

    protected abstract int getMaxTurnCount();

    protected abstract List<CodinGamePlayer> getTeams();

    protected abstract boolean canPlay(CodinGamePlayer t);

    protected abstract boolean setCanNotPlay(CodinGamePlayer t);

}
