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
// 2017 - 05 - 28 - Performance:


***********************************************************
10.000 spil mellem R3 og R4
************************************************************
AlgShooter : 54.8873
Enemy : 52.2749
Win% : 44.9%

R4 - experiment: 5155 wins.
R3 - experiment: 4490 wins.
THE WINNER IS: R4 - experiment

*****************************************************************
indexesFromEnemyShipMatch (0,4)  ved. 1.000 
5 spil vindes ud af 10 
******************************************************************
AlgShooter : 51.522
Enemy : 53.622
Win% : 59.2%

R4 - experiment: 592 wins.
Codename: Litra D: 359 wins.
THE WINNER IS: R4 - experiment
---------------------------------------------------------------
AlgShooter : 52.758
Enemy : 50.65
Win% : 39.0%

R4 - experiment: 390 wins.
Codename: Litra D: 572 wins.
THE WINNER IS: Codename: Litra D
-------------------------------------------------------------------------
AlgShooter : 51.785
Enemy : 51.151
Win% : 45.1%

R4 - experiment: 451 wins.
Codename: Litra D: 500 wins.
THE WINNER IS: Codename: Litra D
------------------------------------------------------------------------------
AlgShooter : 52.546
Enemy : 51.164
Win% : 41.5%

R4 - experiment: 415 wins.
Codename: Litra D: 539 wins.
THE WINNER IS: Codename: Litra D
------------------------------------------------------------------------------
AlgShooter : 53.237
Enemy : 53.044
Win% : 44.5%

R4 - experiment: 445 wins.
Codename: Litra D: 503 wins.
THE WINNER IS: Codename: Litra D
-------------------------------------------------------------------------------
AlgShooter : 52.775
Enemy : 53.309
Win% : 49.0%

R4 - experiment: 490 wins.
Codename: Litra D: 468 wins.
THE WINNER IS: R4 - experiment
---------------------------------------------------------------------------
AlgShooter : 53.635
Enemy : 52.694
Win% : 41.4%

R4 - experiment: 414 wins.
Codename: Litra D: 537 wins.
THE WINNER IS: Codename: Litra D
--------------------------------------------------------------------------------
AlgShooter : 51.544
Enemy : 53.241
Win% : 56.1%

R4 - experiment: 561 wins.
Codename: Litra D: 383 wins.
THE WINNER IS: R4 - experiment
-----------------------------------------------------------------------------
AlgShooter : 52.226
Enemy : 52.667
Win% : 49.1%

R4 - experiment: 491 wins.
Codename: Litra D: 474 wins.
THE WINNER IS: R4 - experiment
-----------------------------------------------------------------------------
AlgShooter : 52.14
Enemy : 53.266
Win% : 55.5%

R4 - experiment: 555 wins.
Codename: Litra D: 402 wins.
THE WINNER IS: R4 - experiment
------------------------------------------------------------------------------



*****************************************************************
optimering af "indexesFromEnemyShipMatch" funktion mod X4 ved 100.000 spil
******************************************************************


******************************* 0,41 cutoff
AlgShooter : 53.36535
Enemy : 54.34817
Win% : 50.128%

R4 - experiment: 50128 wins.
Codename: Litra D: 45332 wins.
THE WINNER IS: R4 - experiment

******************************* 0,405 cutoff
AlgShooter : 52.75389
Enemy : 54.78753
Win% : 56.894%

R4 - experiment: 56894 wins.
Codename: Litra D: 38520 wins.
THE WINNER IS: R4 - experiment


******************************* 0,4025 cutoff
AlgShooter : 53.13098
Enemy : 53.75157
Win% : 48.502%

R4 - experiment: 48502 wins.
Codename: Litra D: 47185 wins.
THE WINNER IS: R4 - experiment

******************************* 0,4 cutoff
AlgShooter : 52.66198
Enemy : 54.39925
Win% : 56.352%

R4 - experiment: 56352 wins.
Codename: Litra D: 39218 wins.
THE WINNER IS: R4 - experiment

******************************* 0,4 cutoff
AlgShooter : 52.89792
Enemy : 53.42764
Win% : 49.316%

R4 - experiment: 49316 wins.
Codename: Litra D: 46369 wins.
THE WINNER IS: R4 - experiment

******************************* 0,395 cutoff
AlgShooter : 53.27435
Enemy : 54.48966
Win% : 50.94%

R4 - experiment: 50940 wins.
Codename: Litra D: 44681 wins.
THE WINNER IS: R4 - experiment

******************************* 0,39 cutoff
AlgShooter : 53.26404
Enemy : 54.78622
Win% : 51.796%

R4 - experiment: 51796 wins.
Codename: Litra D: 43796 wins.
THE WINNER IS: R4 - experiment

*/