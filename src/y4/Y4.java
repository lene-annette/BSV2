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
        return new String[] {"Lene","Christian","Gert"};
    }
    
}

/*
// 2017 - 05 - 27 - Performance:

1.000 skud:
***************************************X4 -- Lose
AlgShooter : 51.868
Enemy : 50.956
Win% : 46.8%

Group 4 Battleship AI: 468 wins.
Codename: Litra D: 491 wins.
THE WINNER IS: Codename: Litra D
*****************************************R2 ---Win
AlgShooter : 53.717
Enemy : 28.272
Win% : 1.7%

Group 4 Battleship AI: 983 wins.
R2 - EnemyShotRound: 17 wins.
THE WINNER IS: Group 4 Battleship AI

10.000 skud:

******************************************X4 ---Win
AlgShooter : 52.951
Enemy : 54.0211
Win% : 51.92%

Group 4 Battleship AI: 5192 wins.
Codename: Litra D: 4383 wins.
THE WINNER IS: Group 4 Battleship AI
******************************************X1 ---Win
AlgShooter : 44.9139
Enemy : 54.7564
Win% : 79.42%

Group 4 Battleship AI: 7942 wins.
Codename: Litra A: 1840 wins.
THE WINNER IS: Group 4 Battleship AI



******************************R2




*/