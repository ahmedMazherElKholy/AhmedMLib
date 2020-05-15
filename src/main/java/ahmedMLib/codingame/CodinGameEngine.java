///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package ahmedMLib.codingame;

import ahmedMLib.Errors.ErrorInMethod;
import java.util.HashMap;
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
        Map<Integer,Team> teams = getTeams();
        if (teams == null || teams.isEmpty()) {
            throw new ErrorInMethod("playGame() CodinGameEngine", "team list are null or empty");
        }
        for (Team a : teams.values()) {
            a.initializeGame(getInitializationGameData(a));
        }
        int canPlayCount = teams.size();
        while (!gameEnds() && canPlayCount > 0 && turn <= getMaxTurnCount()) {
            HashMap<Integer, String> playerMessages = new HashMap<>();
            for (Team a : teams.values()) {
                if (a.canPlay()) {
                    a.playTurn(getGameData(a));
                }
            }
            for (Map.Entry<Integer, String> msg : playerMessages.entrySet()) {
                String errMsg = updateGameData(msg.getValue(), teams.get(msg.getKey()));
                System.err.println("message of team " + msg.getKey() + " " + msg);
                if (!errMsg.isEmpty()) {
                    teams.get(msg.getKey()).setCanNotPlay();
                    canPlayCount--;
                    System.out.println(errMsg);
                }
            }
            resolveGameTrun();
            turn++;
        }
    }

    protected abstract void setupGameData();

    protected abstract Scanner getInitializationGameData(Team t);

    protected abstract Scanner getGameData(Team t);

    /**
     * this method update game data and should return error message
     * if occurred or "" if no error
     *
     * @param outputMsg
     * @param t
     * @return this method should return error message if occurred or "" if no
     * error
     */
    protected abstract String updateGameData(String outputMsg, Team t);

    protected abstract void resolveGameTrun();

    protected abstract boolean gameEnds();
    
    protected abstract int getMaxTurnCount();

    protected abstract Map<Integer ,Team> getTeams();

}
