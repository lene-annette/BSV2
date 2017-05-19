/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y4;

import java.util.Arrays;

/*
2017-05-19 - kl. 6.28 - Chr.
denne klasse er til funktioner der reagere på den information vi får fra spilleren.
*/

/**
 *
 * @author Christian
 */
public class EnemyReact {
    
    HeatMapBasic hm = new HeatMapBasic();
    
    public void run(){
        
        hm.printSea(reactTestSea());
        System.out.println("");
        
        int[][] result = this.combinations(reactTestSea(), 5);
        for (int i = 0; i < result.length; i++) {
            System.out.println(Arrays.toString(result[i]));
        }
        
    }
    
    
    /**
     * return a double array where each array.length == shiplength and contains the indexes of a possible
     * ship location. The intention is that the double array gets passed to a function that fills each nested array
     * with the index values.
     * 
     * @param enemyMoveMap
     * @param shiplength 
     */
    private int[][] combinations(int[] enemyMoveMap, int shiplength){ //int[] enemyMoveMap, 
        int numberOfCombinations = ((10-shiplength+1)*10) + ((10-shiplength+1)*10);
        int[][] output = new int[numberOfCombinations][];
        int[] shipCoor = new int[shiplength];
        
        int shipsPrLine = 10-shiplength+1;
        //add ships in rows to output
        for (int i = 0; i < 10; i++) {//each row in the enemyMoveMap.
            for (int j = 0; j < shipsPrLine; j++) { //each possible ship location on an empty sea
                for (int k = 0; k < shipCoor.length; k++) { //each point int the ship
                    if (enemyMoveMap[(10*i) + (j+k)] > 0) {//(10*i) + (j+k) is each index in enemyMoveMap
                        shipCoor[k] = (10*i) + (j+k);
                    }
                }
                //add each shipCoor to output.
                output[(10*i)+j] = shipCoor;
                
            }
        }
        
        //add ships in column to output

        return output;
        //System.out.println(numberOfCombinations);
    }
    
    private int[] reactTestSea() {
        int[] fixedSea
                =  { -1,  1,  1,  1,  1,  1,  1,  1,  1, -1,
                      1, -1,  1,  1, -1, -1,  1,  1, -1,  1,
                      1,  1, -1, -1,  1,  1, -1, -1,  1,  1,
                      1,  1, -1, -1,  1,  1, -1, -1,  1,  1,
                      1, -1,  1,  1, -1, -1,  1,  1, -1,  1,
                      1, -1,  1,  1, -1, -1,  1,  1, -1,  1,
                      1,  1, -1, -1,  1,  1, -1, -1,  1,  1,
                      1,  1, -1, -1,  1,  1, -1, -1,  1,  1,
                      1, -1,  1,  1, -1, -1,  1,  1, -1,  1,
                     -1,  1,  1,  1,  1,  1,  1,  1,  1, -1};

        return fixedSea;
    }
    
}
