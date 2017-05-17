/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y4;

import battleship.interfaces.BattleshipsPlayer;
import tournament.player.PlayerFactory;
import tournament.game.*;

/**
 *
 * @author lene_
 */
public class Y4 implements PlayerFactory<BattleshipsPlayer> {

    @Override
    public BattleshipsPlayer getNewInstance() {
        return new AlgShooter();
    }

    @Override
    public String getID() {
        return "Y4";
    }

    @Override
    public String getName() {
        return "Group 4 Battleship AI";
    }

    @Override
    public String[] getAuthors() {
        return new String[] {"Lene"};
    }
    
}
