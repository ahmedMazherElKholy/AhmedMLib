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
public interface Team {

    void initializeTeam(Scanner iniData);

    boolean canPlay();

    void setCanNotPlay();

    String playTurn(Scanner turnData);

}
