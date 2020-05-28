///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package ahmedMLib.codingame;

import ahmedMLib.Errors.ErrorInMethod;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Ahmed Mazher
 */
@SuppressWarnings("serial")
public abstract class CodinGameEngine {

    protected int turn = 0;
    protected final List<CodinGameTeam> teams = new LinkedList<>() ;

    public CodinGameEngine(List<CodinGameTeam> teams) {
        this.teams.addAll(Collections.unmodifiableList(teams));
    }
    
    public final void playGame() {
        setupGameData();
        
        if (teams == null || teams.isEmpty()) {
            throw new ErrorInMethod("playGame() CodinGameEngine", "team list are null or empty");
        }
        for (CodinGameTeam a : teams) {
            a.initializeTeam(getInitializationGameData(a));
        }
        int canPlayCount = teams.size();
        while (!gameEnds() && canPlayCount > 0 && turn <= getMaxTurnCount()) {
            HashMap<CodinGameTeam, String> playerMessages = new HashMap<>();
            for (CodinGameTeam a : teams) {
                if (canPlay(a)) {
                    String msg = a.playTurn(getGameData(a));
                    playerMessages.put(a, msg);
                }
            }
            for (Map.Entry<CodinGameTeam, String> msgEntry : playerMessages.entrySet()) {
                String errMsg = updateGameData(msgEntry.getValue(), msgEntry.getKey());
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

    protected abstract Scanner getInitializationGameData(CodinGameTeam t);

    protected abstract Scanner getGameData(CodinGameTeam t);

    /**
     * this method update game data and should return error message if occurred
     * or "" if no error
     *
     * @param outputMsg
     * @param t
     * @return this method should return error message if occurred or "" if no
     * error
     */
    protected abstract String updateGameData(String outputMsg, CodinGameTeam t);

    protected abstract void resolveGameTrun();

    protected abstract boolean gameEnds();

    protected abstract int getMaxTurnCount();

    protected abstract boolean canPlay(CodinGameTeam t);

    protected abstract void setCanNotPlay(CodinGameTeam t);

}
