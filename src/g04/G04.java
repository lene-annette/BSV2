/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package g04;

import battleship.interfaces.BattleshipsPlayer;
import tournament.player.PlayerFactory;
import tournament.game.*;

/**
 *
 * @author Christian, Gert, Lene
 */
public class G04 implements PlayerFactory<BattleshipsPlayer> {

    @Override
    public BattleshipsPlayer getNewInstance() {
        return new AlgShooter();
    }

    @Override
    public String getID() {
        return "G04";
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
// 2017 - 05 - 29 - Performance:


**********************************************
1000 spil mod R4 ************** vinder 2 udd af 3
**************************************
--------------------------------------------
AlgShooter : 52.189
Enemy : 51.566
Win% : 45.9%

Group 4 Battleship AI: 503 wins.
R4 - experiment: 459 wins.
THE WINNER IS: Group 4 Battleship AI
------------------------------------------
AlgShooter : 51.996
Enemy : 51.949
Win% : 47.8%

Group 4 Battleship AI: 493 wins.
R4 - experiment: 478 wins.
THE WINNER IS: Group 4 Battleship AI
--------------------------------------------------
AlgShooter : 51.944
Enemy : 52.322
Win% : 51.2%

Group 4 Battleship AI: 464 wins.
R4 - experiment: 512 wins.
THE WINNER IS: R4 - experiment
-----------------------------------------------



****************** 1000 spil mod X4:
--- 5 sejre ud af 5.

---------------------------------------------
AlgShooter : 51.236
Enemy : 56.009
Win% : 69.6%

Group 4 Battleship AI: 696 wins.
Codename: Litra D: 274 wins.
THE WINNER IS: Group 4 Battleship AI
-------------------------------------------------
AlgShooter : 51.924
Enemy : 56.672
Win% : 67.8%

Group 4 Battleship AI: 678 wins.
Codename: Litra D: 285 wins.
THE WINNER IS: Group 4 Battleship AI
-------------------------------------------------------
AlgShooter : 51.706
Enemy : 56.059
Win% : 68.2%

Group 4 Battleship AI: 682 wins.
Codename: Litra D: 292 wins.
THE WINNER IS: Group 4 Battleship AI
-------------------------------------------------------------
AlgShooter : 51.965
Enemy : 56.695
Win% : 66.9%

Group 4 Battleship AI: 669 wins.
Codename: Litra D: 305 wins.
THE WINNER IS: Group 4 Battleship AI
-----------------------------------------------------------
AlgShooter : 52.58
Enemy : 55.87
Win% : 63.0%

Group 4 Battleship AI: 630 wins.
Codename: Litra D: 336 wins.
THE WINNER IS: Group 4 Battleship AI
-------------------------------------------------------------



*************************************************
1000 spil mod X1
*************************************************

------------------------------------------------------
AlgShooter : 45.223
Enemy : 55.441
Win% : 78.2%

Group 4 Battleship AI: 782 wins.
Codename: Litra A: 193 wins.
THE WINNER IS: Group 4 Battleship AI
-----------------------------------------------------





*/