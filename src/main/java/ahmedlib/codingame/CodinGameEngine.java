///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package ahmedlib.codingame;

import ahmedlib.Errors.ErrorInMethod;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Ahmed Mazher
 */
@SuppressWarnings("serial")
public abstract class CodinGameEngine {

    protected final List<Team> teams = new ArrayList<>();
    int turn = 0;

    public CodinGameEngine(List<Team> teams) {
        if (teams == null || teams.isEmpty()) {
            throw new ErrorInMethod("constructing CodinGameEngine", "team list are null or empty");
        }
        this.teams.addAll(teams);
    }

    public final void playGame() {
        setupGameData();
        for (Team a : teams) {
            a.initializeGame(getInitializationGameData(a.teamNum));
        }
        int canPlayCount = teams.size();
        while (!gameEnds() && canPlayCount > 0) {
            HashMap<Integer, String> playerMessages = new HashMap<>();
            for (Team a : teams) {
                if (a.canPlay) {
                    String msg = a.playTurn(getGameData(a.teamNum));
                }
            }
            for (Map.Entry<Integer, String> msg : playerMessages.entrySet()) {
                String errMsg = updateGameData(msg.getValue(), msg.getKey());
                System.err.println("message of team " + msg.getKey() + " " + msg);
                if (!errMsg.isEmpty()) {
                    getTeamByNum(msg.getKey()).canPlay = false;
                    canPlayCount--;
                    System.out.println(errMsg);
                }
            }
            resolveGameTrun();
            turn++;
        }
    }

    protected abstract void setupGameData();

    protected abstract Scanner getInitializationGameData(int teamNum);

    protected abstract Scanner getGameData(int teamNum);

    /**
     * this method update game data and should return error message
     * if occurred or "" if no error
     *
     * @param outputMsg
     * @param teamNum
     * @return this method should return error message if occurred or "" if no
     * error
     */
    protected abstract String updateGameData(String outputMsg, int teamNum);

    protected abstract void resolveGameTrun();

    protected abstract boolean gameEnds();

    Team getTeamByNum(int num) {
        for (Team team : teams) {
            if (team.teamNum == num) {
                return team;
            }
        }
        return null;
    }

}
