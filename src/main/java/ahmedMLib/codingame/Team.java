/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ahmedMLib.codingame;

import java.util.Scanner;

/**
 *
 * @author Ahmed Mazher <ahmzel2012@gmail.com>
 */
public abstract class Team {

    protected abstract void initializeGame(Scanner iniData);

    protected abstract boolean canPlay();
    protected abstract void setCanNotPlay();

    protected abstract String playTurn(Scanner turnData);

}
