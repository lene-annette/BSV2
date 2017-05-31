
package g04;

import java.util.ArrayList;

/**
 *
 * @author Christian, Gert, Lene
 */


/*
 type 1 --21 skud
  +  .  .  .  .  +  .  .  .  .
  .  .  .  .  +  .  .  .  +  .
  .  .  .  +  .  .  .  +  .  .
  .  .  +  .  .  .  +  .  .  .
  .  +  .  .  .  +  .  .  .  +
  +  .  .  .  +  .  .  .  +  .
  .  .  .  +  .  .  .  +  .  .
  .  .  +  .  .  .  +  .  .  .
  .  +  .  .  .  +  .  .  .  .
  .  .  .  .  +  .  .  .  .  .
*/

/*
 type 2 -- 21 skud // ca 1 ud af ti
  +  .  .  .  .  +  .  .  .  .
  .  .  .  .  +  .  .  .  +  .
  .  .  +  .  .  .  +  .  .  .
  .  .  .  +  .  .  .  +  .  .
  .  +  .  .  .  +  .  .  .  +
  +  .  .  .  +  .  .  .  +  .
  .  .  +  .  .  .  +  .  .  .
  .  .  .  +  .  .  .  +  .  .
  .  +  .  .  .  +  .  .  .  .
  .  .  .  .  +  .  .  .  .  .
*/

/*
 10 runs: (ingen adskillelse mellem de første ramte og de sidst ramte)

 2  1  1  1 [6][6] 1  1  1  3 
 1 [6] 1  1 [6][6] 1  1 [6] 1 
 1  1 [6][6] 1  1 [6][6] 1  1 
 1  1 [6][6] 1  1 [6][6] 1  1 
[6][6] 1  1 [6][6] 1  1 [6][6]
[6][6] 1  1 [6][6] 1  1 [6][6]
 1  1 [6][6] 1  1 [6][6] 1  1 
 1  1 [6][6] 1  1 [6][6] 1  1 
 1 [6] 1  1 [6][6] 1  1 [6] 1 
 4  1  1  1 [6][6] 1  1  1  5 
*/

/*
    type 1: -- 21 shots -- with shotNumbers ("+" == -1) 
  .  .  .  .  . -6  .  .  .  .
  .  .  .  . -10  .  .  . -3  .
  .  .  . -16  .  .  . -8  .  .
  .  . -17  .  .  . -19  .  .  .
  . -13  .  .  . -21  .  .  . -5
 -4  .  .  . -20  .  .  . -12  .
  .  .  . -18  .  .  . -14  .  .
  .  . -9  .  .  . -15  .  .  .
  . -2  .  .  . -11  .  .  .  .
  .  .  .  . -7  .  .  .  .  +
*/

/*
    Type 2: -- 21 shots --  with shotNumbers
  .  .  .  .  . -4  .  .  .  .
  .  .  .  . -13  .  .  . -2  .
  .  . -9  .  .  . -16  .  .  .
  .  .  . -18  .  .  . -17  .  .
  . -11  .  .  . -20  .  .  . -5
 -6  .  .  . -21  .  .  . -10  .
  .  . -15  .  .  . -19  .  .  .
  .  .  . -14  .  .  . -8  .  .
  . -3  .  .  . -12  .  .  .  .
  .  .  .  . -7  .  .  .  .  +
*/

/*
        //10 runs -"meta"heatmap- with accumulated shotnumbers (highest num == 21, lowest num == 1)
    2    .    .    .   27   31    .    .    .    2
    .   15    .    .   62   58    .    .   14    .
    .    .   43   78    .    .   80   44    .    .
    .    .   77   94    .    .   93   83    .    .
   31   61    .    .  104  104    .    .   60   29
   29   61    .    .  103  103    .    .   54   29
    .    .   77   94    .    .   93   81    .    .
    .    .   43   74    .    .   78   44    .    .
    .   13    .    .   55   57    .    .   12    .
    5    .    .    .   25   27    .    .    .    5
        int[] sea = this.inverseHeatMapWithShotNumebers();
        hm.printSea(sea);
*/

public class HeatMapInverse {

    HeatMapBasic hm = new HeatMapBasic();

    
    private int[] inverseHeatMapWithShotNumebers(){
        int[] InverseMap = hm.getEmptySea();
        int[] shootingPatern = null; //inverseHeatTemplate();
        
        for (int i = 0; i < 10; i++) {
            shootingPatern = this.inverseHeatTemplateWithShotNums(21);
            for (int j = 0; j < shootingPatern.length; j++) {
                if (shootingPatern[j] < 1) {
                    InverseMap[j] = InverseMap[j] + -shootingPatern[j];
                }
            }
        }
        return InverseMap;
    }
    
    private int[] inverseHeatMap(){
        int[] InverseMap = hm.getEmptySea();
        int[] shootingPatern = null; 
        
        for (int i = 0; i < 10; i++) {
            shootingPatern = inverseHeatTemplate();
            for (int j = 0; j < shootingPatern.length; j++) {
                if (shootingPatern[j] < 1) {
                    InverseMap[j]++;
                }
            }
        }
        return InverseMap;
    }
    
    private int[] inverseHeatTemplateWithShotNums(int shotNumMax) {

        int[] heat = hm.generateVirginHeatmap();
        int[] oldsea = hm.getEmptySea();
        int[] sea = hm.getEmptySea();
        boolean hope = true;
        int counter = 0;
        

        while (this.spaceForFive(sea) > 1) {
            ++counter;
            int nextPointToShoot = 0;

            heat = hm.simpleHeatMap(sea, hm.getVirginFleet());
            nextPointToShoot = hm.getIntFromHeatMap(heat);
            
            System.arraycopy(sea, 0, oldsea, 0, sea.length);
            sea[nextPointToShoot] = -shotNumMax;
            shotNumMax = shotNumMax -1;
        }
        return oldsea;
    }
    
    private int[] inverseHeatTemplate() {

        int[] heat = hm.generateVirginHeatmap();
        int[] oldsea = hm.getEmptySea();
        int[] sea = hm.getEmptySea();
        boolean hope = true;
        int counter = 0;
        

        while (this.spaceForFive(sea) > 1) {
            ++counter;
            int nextPointToShoot = 0;

            heat = hm.simpleHeatMap(sea, hm.getVirginFleet());
            nextPointToShoot = hm.getIntFromHeatMap(heat);
            
            System.arraycopy(sea, 0, oldsea, 0, sea.length);
            sea[nextPointToShoot] = -1; 
        }
        return oldsea;
    }

    public int spaceForFive(int[] sea) {
        int numOfTimesThereIsSpace = 0;
        boolean output = false;
        int horizontal = 1;
        boolean hor = false;
        for (int i = 1; i < sea.length; i++) {
            if (sea[i] == 1 && sea[i - 1] == 1) {
                horizontal++;
            } else if (sea[i] != 1) {
                horizontal = 1;
            }

            if (horizontal >= 5) {
                numOfTimesThereIsSpace++;                
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
            } else if (sea[i] != 1) {
                vertical = 1;
            }

            if (vertical >= 5) {
                numOfTimesThereIsSpace++;                
                ver = true;
            }
            if (i >= 90 && i != 99) {
                int temp = i - 90;
                i = 1 + temp;
            }
            //extra test
            if (i/10 == 0 ) {
                vertical = 1;
            }
        }

        return numOfTimesThereIsSpace;
    }

    private int[] testSea1() { 
        int[] fixedSea
                = {-1, -1, -1, -1, -3, -3, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -5, -1, -1, -1,
                    -7, -7, -7, -1, -1, -1, -5, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -5, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -1, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -1, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -1, -1, 1, 1,
                    1, 1, 1, -1, -1, 1, 1, 1, -1, -11};

        return fixedSea;
    }

    private int[] testSea2() { 
        int[] fixedSea
                = {-1, -1, -1, -1, -3, -3, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -5, -1, -1, -1,
                    -7, -7, -7, -1, -1, -1, -5, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -5, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -1, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -1, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -1, -1, 1, 1,
                    1, 1, -1, -1, 1, 1, 1, 1, 1, -11};

        return fixedSea;
    }

    private int[] testSea3() { 
        int[] fixedSea
                = {-1, -1, -1, -1, -3, -3, -1, -1, -1, -1,
                    1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -5, -1, -1, -1,
                    -7, -7, -7, -1, -1, -1, -5, -1, -1, -11,
                    1, -1, -9, -1, -1, -1, -5, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -1, -1, -1, -11,
                    1, -1, -9, -1, -1, -1, -1, -1, -1, -11,
                    1, -1, -9, -1, -1, -1, -1, -1, 1, -1,
                    1, 1, -1, -1, -1, 1, -1, 1, 1, -11};

        return fixedSea;
    }

    private int[] testSea4() { 
        int[] fixedSea
                = {-1, -1, -1, -1, -3, -3, -1, -1, 1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, 1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, 1, -1,
                    -1, -1, -1, -1, -1, -1, -5, -1, 1, -1,
                    -7, -7, -7, -1, -1, -1, -5, -1, 1, -11,
                    -1, -1, -9, -1, -1, -1, -5, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -1, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -1, -1, -1, -11,
                    -1, -1, -9, -1, -1, -1, -1, -1, 1, 1,
                    -1, 1, -1, -1, -1, 1, -1, 1, 1, -11};

        return fixedSea;
    }
    
    private int[] testSea5() {
        int[] fixedSea
                =  { 1, 1, 1, 1, 1,-1, 1, 1, 1,-1,
                     1, 1, 1, 1,-1, 1, 1, 1,-1, 1,
                     1, 1, 1,-1, 1, 1, 1,-1, 1, 1,
                     1, 1,-1, 1, 1, 1,-1, 1, 1, 1,
                     1,-1, 1, 1, 1,-1, 1, 1, 1,-1,
                    -1, 1, 1, 1,-1, 1, 1, 1,-1, 1,
                     1, 1, 1,-1, 1, 1, 1,-1, 1, 1,
                     1, 1,-1, 1, 1, 1,-1, 1, 1, 1,
                     1,-1, 1, 1, 1,-1, 1, 1, 1, 1,
                    -1, 1, 1, 1,-1, 1, 1, 1, 1, 1};

        return fixedSea;
    }
    
    private int[] antiHeatTemplatePS() {
        int[] fixedSea
                =  { -1,  1,  1,  1, -1, -1,  1,  1,  1, -1,
                      1, -1,  1,  1, -1, -1,  1,  1, -1,  1,
                      1,  1, -1, -1,  1,  1, -1, -1,  1,  1,
                      1,  1, -1, -1,  1,  1, -1, -1,  1,  1,
                     -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,
                     -1, -1,  1,  1, -1, -1,  1,  1, -1, -1,
                      1,  1, -1, -1,  1,  1, -1, -1,  1,  1,
                      1,  1, -1, -1,  1,  1, -1, -1,  1,  1,
                      1, -1,  1,  1, -1, -1,  1,  1, -1,  1,
                     -1,  1,  1,  1, -1, -1,  1,  1,  1, -1};

        return fixedSea;
    }
    
    public int[] simpleAntiHeatPS() {
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

/*
        //10 runs -"meta"heatmap- with accumulated shotnumbers (highest num == 21, lowest num == 1)
    2    .    .    .   27   31    .    .    .    2
    .   15    .    .   62   58    .    .   14    .
    .    .   43   78    .    .   80   44    .    .
    .    .   77   94    .    .   93   83    .    .
   31   61    .    .  104  104    .    .   60   29
   29   61    .    .  103  103    .    .   54   29
    .    .   77   94    .    .   93   81    .    .
    .    .   43   74    .    .   78   44    .    .
    .   13    .    .   55   57    .    .   12    .
    5    .    .    .   25   27    .    .    .    5
        //int[] sea = this.inverseHeatMapWithShotNumebers();
        //hm.printSea(sea);
*/

/*
  +  .  .  .  .  +  .  .  .  .
  .  .  .  .  +  .  .  .  +  .
  .  .  .  +  .  .  .  +  .  .
  .  .  +  .  .  .  +  .  .  .
  .  +  .  .  .  +  .  .  .  +
  +  .  .  .  +  .  .  .  +  .
  .  .  .  +  .  .  .  +  .  .
  .  .  +  .  .  .  +  .  .  .
  .  +  .  .  .  +  .  .  .  .
  .  .  .  .  +  .  .  .  .  .
*/
