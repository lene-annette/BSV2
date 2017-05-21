
package y4;

import java.util.ArrayList;
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
    
    /*
    2017-05-19 kl. 10.15 - Chr.
    1. opgave: (givet et hav og et skib): lav en array over alle mulige skibsplaceringer. (med plusOne, + for positiv, - for negativ)
    2. opgave: lave en søster array hvor hver værdi ikke er et index, men dette index's værdi!!!
    3 opgave: lav en array: hver værdi er summen af værdierne i opgave 2.
    
    */
    
    HeatMapBasic hm = new HeatMapBasic();
    
    public void run(){      
        
        ArrayList<Integer> ship2 = this.spaceForShipIndexPlusOneER(this.getEmptySea(), 3);
        System.out.println(ship2);
        System.out.println("ship2: "+ship2.size());
        
        ArrayList<Integer> myComb = combinations(this.getEmptySea(), 3);
        System.out.println("combinations: " + myComb);     
        for (int i = 0; i < myComb.size(); i++) {
            //System.out.println("Index: "+ i +" myComb IndexValue: " + myComb[i]);
        }
        
    }
    
    /**
     * 1. opgave: lav en array over alle mulige skibsplaceringer. (med plusOne, + for positiv, - for negativ)
     * 2. opgave: lave en søster array hvor hver værdi ikke er et index, men dette index's værdi!!!
     * 3 opgave: lav en array: hver værdi er summen af værdierne i opgave 2.
     *
     * @param enemyMoveMap
     * @param shiplength 
     */
    private ArrayList<Integer> combinations(int[] sea, int shiplength){  //int[] enemyMoveMap,
        ArrayList<Integer> opg1 = this.spaceForShipIndexPlusOneER(sea, shiplength);
        // opg1 is now a list of the possible indexes ( getEmptySea(), 3 );
        // opg1: 160 -- [1, 2, 3, 4, 5, 6, 7, 8, 11, 12, 13, 14, 15, 16, 17, 18, 21, 22, 23, 24, 25, 26, 27, 28, 31, 32, 33, 34, 35, 36, 37, 38, 41, 42, 43, 44, 45, 46, 47, 48, 51, 52, 53, 54, 55, 56, 57, 58, 61, 62, 63, 64, 65, 66, 67, 68, 71, 72, 73, 74, 75, 76, 77, 78, 81, 82, 83, 84, 85, 86, 87, 88, 91, 92, 93, 94, 95, 96, 97, 98, -1, -11, -21, -31, -41, -51, -61, -71, -2, -12, -22, -32, -42, -52, -62, -72, -3, -13, -23, -33, -43, -53, -63, -73, -4, -14, -24, -34, -44, -54, -64, -74, -5, -15, -25, -35, -45, -55, -65, -75, -6, -16, -26, -36, -46, -56, -66, -76, -7, -17, -27, -37, -47, -57, -67, -77, -8, -18, -28, -38, -48, -58, -68, -78, -9, -19, -29, -39, -49, -59, -69, -79, -10, -20, -30, -40, -50, -60, -70, -80]
        int[] opg2 = new int[opg1.size()];
        
        
        
        return opg1;
    }
    
    // tag et koordinat og en skibslængde, 
    // og find de tilsvarende indexpunkter
    public ArrayList<Integer> CorrListFromCorrAndShiplength(int CorrPlusOne, int shiplength){
        ArrayList<Integer> output = new ArrayList<Integer>();
        int firstCoordinate = 0;
        
        if (CorrPlusOne > 0){
            firstCoordinate = CorrPlusOne-1;
            for (int i = 0; i < shiplength; i++) {
                output.add(firstCoordinate+i);
            }
        }else if(CorrPlusOne < 0){
            firstCoordinate = Math.abs(CorrPlusOne)-1;
            for (int i = 0; i < shiplength; i++) {
                output.add(firstCoordinate+(i*10));
            }
        }else{
            System.out.println("EnemyReact: Fejl i CorrListFromCorrAndShiplength: CorrPlusOne == 0 !!!! (skal være > 0 eller < 0).");
        }
        
        return output;
    }
    
    
    public int[] shipindexesFromCorr(int topLeftCoor, int shipsize){
        int[] output = null;
        if (shipsize < 0) {
        
        }else{
    
        }
        
        return output;
    } 
    
    
    public ArrayList<Integer> spaceForShipIndexPlusOneER(int[] sea, int shiplength) {
        ArrayList<Integer> numOfTimesThereIsSpace = new ArrayList<Integer>();
        boolean output = false;
        int horizontal = 1;
        boolean hor = false;
        for (int i = 1; i < sea.length; i++) {
            if (sea[i] == 1 && sea[i - 1] == 1) {
                horizontal++;
            } else if (sea[i] != 1) {
                horizontal = 1;
            }

            if (horizontal >= shiplength) {
                numOfTimesThereIsSpace.add(i - (shiplength - 1)       +1 ); // +1 to avoid "+0" and "-0"
                hor = true;
            }
            if (i % 10 == 9) {
                i++;
                horizontal = 1;
            }
        }

        int vertical = 1;
        boolean ver = false;
        for (int i = 10; i < sea.length; i += 10) {
            if (sea[i] == 1 && sea[i - 10] == 1) {
                vertical++;
                //System.out.println(i);
            } else if (sea[i] != 1) {
                vertical = 1;
            }

            if (vertical >= shiplength) {
                numOfTimesThereIsSpace.add(-(i - (10*(shiplength - 1)))      -1); //  // +1 to avoid "+0" and "-0"             
                ver = true;
            }
            if (i >= 90 && i != 99) {
                //System.out.println("90's: "+i);
                int temp = i - 90;
                i = 1 + temp;
            }
            //extra test
            if (i/10 == 0 ) {
                vertical = 1;
            }
            //System.out.println(i);
        }

        return numOfTimesThereIsSpace;
    }
    
    public int[] getEmptySea() {
        int[] output = new int[100];
        for (int i = 0; i < output.length; i++) {
            output[i] = 1;
        }
        return output;
    }
    
    private int[] reactTestSea1() {
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
    
    private int[] reactTestSea2() {
        int[] fixedSea
                =  {   100,  200,  300,  400,  500,  600,  700,  800,  900, 1000,
                      1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900, 2000,
                      2100, 2200, 2300, 2400, 2500, 2600, 2700, 2800, 2900, 3000,
                      3100, 3200, 3300, 3400, 3500, 3600, 3700, 3800, 3900, 4000,
                      4100, 4200, 4300, 4400, 4500, 4600, 4700, 4800, 4900, 5000,
                      5100, 5200, 5300, 5400, 5500, 5600, 5700, 5800, 5900, 6000,
                      6100, 6200, 6300, 6400, 6500, 6600, 6700, 6800, 6900, 7000,
                      7100, 7200, 7300, 7400, 7500, 7600, 7700, 7800, 7900, 8000,
                      8100, 8200, 8300, 8400, 8500, 8600, 8700, 8800, 8900, 9000,
                      9100, 9200, 9300, 9400, 9500, 9600, 9700, 9800, 9900,10000};

        return fixedSea;
    }
    
}


/*
    // 2017 - 05-17 - kl. 10.36 - daarlig kode.

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
    
*/