/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahmedlib.codingame;

import java.util.Scanner;

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
public abstract class Team {
    
    final int teamNum;
    boolean canPlay;

    public Team(int teamNum) {
        this.canPlay = true;
        this.teamNum = teamNum;
    }

    public int getTeamNum() {
        return teamNum;
    }

    protected abstract void initializeGame(Scanner iniData);

    protected abstract String playTurn(Scanner turnData);
    
}
