
package y4;

import battleship.interfaces.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/*
2017-05-19 - kl. 6.28 - Chr.
denne klasse er til funktioner der reagere på den information vi får fra spilleren.
*/

/**
 *
 * @author Christian
 */
public class EnemyReact {
    
    
//    HeatMapBasic hm = new HeatMapBasic();
    
    public void run(){      
        
//        System.out.println("EnemyReact");
//        int[] fleet = {5,4,3,3,2};
//        int[] mysea = hm.getEmptySea();//reactTestSea1();
//        hm.printSea(mysea);
//        int[] myHeatMap = efficientHeatMap(mysea, fleet);
//        hm.printHeatmap(1, myHeatMap);
        
    }
    
    //denne er til den algoritme der skal korrigere skudalgoritmen 
    // ud fra hvordan modstanderen placere skibe.
    //if the algoritm doesnt find any interesting place to shoot:
    // return -1. 
    public int indexFromEnemyShipMatch(int[] sea, int[] enemyShipMatch, int roundNumber, ArrayList<Position> shotsFired){
        int output = -1;
        ArrayList<Integer> hardCodedIndexes = new ArrayList<Integer>();
        for (int i = 0; i < enemyShipMatch.length; i++) {
            // hvis et punkt i enemyShipMatch har samme værdi som roundCount,
            // betyder det at modstanderen altid placere skibe der.
            // i så fald skal vi altid huske at skyde der.
            if (sea[i] > 2 && enemyShipMatch[i] >= roundNumber) {
                        //count them only as hardcoded if the ships have been placed in the same spot more than twice.
                hardCodedIndexes.add(i);
            }
        }
        
        //hvis der ikke er nogen oplagte punkter =
        // lav en fancy algoritme her. og tilføj punkterne til hardCodedIndexes.
        for (int i = 0; i < enemyShipMatch.length; i++) {
            if (sea[i] > 2 && enemyShipMatch[i] > roundNumber/1.5){//skibene placeres det samme sted 75 % af tiden
                hardCodedIndexes.add(i);
            }
        }
        
        //remove the indexes from hardCodedIndexes if they are in "shotsfired"
        List<Integer> copyList = new ArrayList<>(hardCodedIndexes);
        for (int i = 0; i < copyList.size(); i++) {
            int candidateMove = copyList.get(i);
            Position candPos = this.getPosFromIndex(candidateMove);
            if (shotsFired.contains(candPos)) {
                hardCodedIndexes.remove(candidateMove);
            }
        }
        
        if (hardCodedIndexes.size() > 0) {
            output = hardCodedIndexes.get(0);
        }
        
        
        return output;
    }
    
    public Position getPosFromIndex(int index) {
        //int Xcoordinate = 0;
        int Xcoordinate = index % 10;
        //int Ycoordinate = 0;
        int Ycoordinate = 9 - (index / 10);

        Position pos = new Position(Xcoordinate, Ycoordinate);

        return pos;
    }
    
    public int getIndexFromPos(Position pos) {
        int output = 0;
        int Xcoordinate = pos.x;
        int Ycoordinate = pos.y;
        output = (9 - Ycoordinate) * 10 + Xcoordinate;
        return output;
    }
    
    //krav: ramte fælter skal være < 1.
    // de andre skal være 1.
    public int[] efficientHeatMap(int[] sea, ArrayList<Integer> fleet){
        int[] outputCounterSea = new int[100];
        int[] emptysea = sea;
        Random r = new Random();
        int myRandom = 0;
        
        ArrayList<int[]> possiblePlaces = new ArrayList<int[]>();
        
        for (int i = 0; i < fleet.size(); i++){
            int[][] nestedArrayIndexes = combinations(emptysea, fleet.get(i));
            int[][] copyToManipulate = combinations(emptysea, fleet.get(i));
            for (int j = 0; j < nestedArrayIndexes.length; j++) {
                for (int k = 0; k < nestedArrayIndexes[j].length; k++) {
                    if(sea[nestedArrayIndexes[j][k]] < 1 ){
                        copyToManipulate[j] = null;
                    }
                }
                if (copyToManipulate[j] != null) {
                    possiblePlaces.add(copyToManipulate[j]);
                }
            }
            // nu er possiblePlaces en arrList over alle mulige skibsplaceringer.
            // nu skal punkterne lægges til outputCounterSea;
            for (int j = 0; j < possiblePlaces.size(); j++) {
                for (int k = 0; k < possiblePlaces.get(j).length; k++) {
                    outputCounterSea[possiblePlaces.get(j)[k]]++;
                }
            }
            possiblePlaces.clear();
        }
        return outputCounterSea;
    }
    
            
    /*
    int[] fleet = {5,4,3,3,2};
    int[] mysea = reactTestSea1();
    mysea = efficientDistributeShips(mysea, fleet);
    hm.printSea(mysea);        
    */
    public int[] efficientDistributeShips(int[] sea, int[] fleet){
        int[] emptysea = sea;//hm.getEmptySea();
        //ArrayList<Integer> fleet = hm.getVirginFleet();
        
        Random r = new Random();
        int myRandom = 0;
        ArrayList<Integer> usedPoints = new ArrayList<Integer>();
        ArrayList<int[]> possiblePlaces = new ArrayList<int[]>();
        int[] chosenPlace = null;
        
        for (int i = 0; i < fleet.length; i++){
            int[][] nestedArrayIndexes = combinations(emptysea, fleet[i]);
            int[][] copyToManipulate = combinations(emptysea, fleet[i]);
            for (int j = 0; j < nestedArrayIndexes.length; j++) {
                for (int k = 0; k < nestedArrayIndexes[j].length; k++) {
                    if(usedPoints.contains(nestedArrayIndexes[j][k]) || 
                            emptysea[nestedArrayIndexes[j][k]] < 1 ){
                        copyToManipulate[j] = null;
                    }
                }
                if (copyToManipulate[j] != null) {
                    possiblePlaces.add(copyToManipulate[j]);
                }
            }
            // nu er possiblePlaces en arrList over alle mulige skibsplaceringer.
            // nu skal der vælges en tilfældig.
            myRandom = r.nextInt(possiblePlaces.size());
            chosenPlace = possiblePlaces.get(myRandom);
            for (int j = 0; j < chosenPlace.length; j++) {
                emptysea[chosenPlace[j]] = 2;
                usedPoints.add(chosenPlace[j]);
            }
            possiblePlaces.clear();
            
        }
        return emptysea;
    }
    
    //attemptAtEvenlyDistributedHeatmap
    
    //returns a sea with a fleet that hopefully has an even density distribution
    /*
    public int[] attemptAtRandomSeaDistribution(){
        int[] randomSeaNumber;
        ArrayList<Integer> fleet = hm.getVirginFleet();
        int[] seaSpaces_ThisShip;
        ArrayList<Integer> seaSpacesToBeFilled = new ArrayList<Integer>();
        
        int myCoor = 0;
        int myShip = 0;
        for(int i = 0; i < fleet.size(); i++){
            randomSeaNumber = ramdomArrayOneToHundred();
            myShip = fleet.get(i);
            myCoor = coordinatePlusOneFromHM(randomSeaNumber, myShip, true);
            seaSpaces_ThisShip = this.CorrListFromCorrAndShiplength(myCoor, myShip);
            
            
        }
    }
    */
    
    /*
    int[] randomSeaNumber = ramdomArrayOneToHundred();
    int myCoor = coordinatePlusOneFromHM(randomSeaNumber, 4, true);
    hm.printHeatmap(1, randomSeaNumber);
    System.out.println(myCoor);
    */
    
    public int[] ramdomArrayOneToHundred(){
        int[] output = new int[100];
        int temp = 1;
        int temp2 = 0;
        int myRand;
        
        for (int i = 0; i < output.length; i++) {
            output[i] = temp;
            temp++;
        }
        
        Random r = new Random();
        
        for (int i = 0; i < output.length; i++) {
            myRand = r.nextInt(100);
            temp = output[myRand];
            temp2 = output[i];
            output[i] = temp;
            output[myRand] = temp2; 
        }
        
        return output;
    }
    
    
    //**********************************************************
    // 2017-05-22 kl. 6.35 - chr - Nedenfor er metoden til at
    //                      en skibsplacering fra et heatmap:
    //  public int coordinatePlusOneFromHM(int[] heatmap, int shipsize, boolean lowestValue){
    //************************************************************
    
    /*
    
    public void run(){      
        
        ArrayList<Integer> opg1 = this.spaceForShipIndexPlusOneER(this.getEmptySea(), 5);
        System.out.println(opg1);
        
        int[][] nestedArray = combinations(this.getEmptySea(), 5);
        System.out.println(Arrays.deepToString(nestedArray));
        
        System.out.println();
        System.out.println("values:");
        int[][]values = getValuesFromSeaToNestedArray(this.reactTestSea2(), 5);  
        System.out.println(Arrays.deepToString(values));
        
        int[] sumOfValues = sumOfValues(values);
        System.out.println("Sum of values: " + Arrays.toString(sumOfValues));
        
        
        //*******************
        HeatMapBasic hm = new HeatMapBasic();
        int[] testHM = hm.generateVirginHeatmap();
        hm.printHeatmap(1, testHM);
        //************************
        
        System.out.println();
        int myIntCoor = coordinatePlusOneFromHM(testHM, 2, true);
        System.out.println("myIntCoor: " + myIntCoor);
    }
    */
    
    //public int[] sumOfValues(int[][] nestedArray){
    //get sum of values put into a spaceForShipIndexPlusOneER -- array.
    //values:
    //[[100, 200, 300, 400, 500], [200, 300, 400, 500, 600], [300, 400, 500, 600, 700], [400, 500, 600, 700, 800], [500, 600, 700, 800, 900], [600, 700, 800, 900, 1000], [1100, 1200, 1300, 1400, 1500], [1200, 1300, 1400, 1500, 1600], [1300, 1400, 1500, 1600, 1700], [1400, 1500, 1600, 1700, 1800], [1500, 1600, 1700, 1800, 1900], [1600, 1700, 1800, 1900, 2000], [2100, 2200, 2300, 2400, 2500], [2200, 2300, 2400, 2500, 2600], [2300, 2400, 2500, 2600, 2700], [2400, 2500, 2600, 2700, 2800], [2500, 2600, 2700, 2800, 2900], [2600, 2700, 2800, 2900, 3000], [3100, 3200, 3300, 3400, 3500], [3200, 3300, 3400, 3500, 3600], [3300, 3400, 3500, 3600, 3700], [3400, 3500, 3600, 3700, 3800], [3500, 3600, 3700, 3800, 3900], [3600, 3700, 3800, 3900, 4000], [4100, 4200, 4300, 4400, 4500], [4200, 4300, 4400, 4500, 4600], [4300, 4400, 4500, 4600, 4700], [4400, 4500, 4600, 4700, 4800], [4500, 4600, 4700, 4800, 4900], [4600, 4700, 4800, 4900, 5000], [5100, 5200, 5300, 5400, 5500], [5200, 5300, 5400, 5500, 5600], [5300, 5400, 5500, 5600, 5700], [5400, 5500, 5600, 5700, 5800], [5500, 5600, 5700, 5800, 5900], [5600, 5700, 5800, 5900, 6000], [6100, 6200, 6300, 6400, 6500], [6200, 6300, 6400, 6500, 6600], [6300, 6400, 6500, 6600, 6700], [6400, 6500, 6600, 6700, 6800], [6500, 6600, 6700, 6800, 6900], [6600, 6700, 6800, 6900, 7000], [7100, 7200, 7300, 7400, 7500], [7200, 7300, 7400, 7500, 7600], [7300, 7400, 7500, 7600, 7700], [7400, 7500, 7600, 7700, 7800], [7500, 7600, 7700, 7800, 7900], [7600, 7700, 7800, 7900, 8000], [8100, 8200, 8300, 8400, 8500], [8200, 8300, 8400, 8500, 8600], [8300, 8400, 8500, 8600, 8700], [8400, 8500, 8600, 8700, 8800], [8500, 8600, 8700, 8800, 8900], [8600, 8700, 8800, 8900, 9000], [9100, 9200, 9300, 9400, 9500], [9200, 9300, 9400, 9500, 9600], [9300, 9400, 9500, 9600, 9700], [9400, 9500, 9600, 9700, 9800], [9500, 9600, 9700, 9800, 9900], [9600, 9700, 9800, 9900, 10000], [100, 1100, 2100, 3100, 4100], [1100, 2100, 3100, 4100, 5100], [2100, 3100, 4100, 5100, 6100], [3100, 4100, 5100, 6100, 7100], [4100, 5100, 6100, 7100, 8100], [5100, 6100, 7100, 8100, 9100], [200, 1200, 2200, 3200, 4200], [1200, 2200, 3200, 4200, 5200], [2200, 3200, 4200, 5200, 6200], [3200, 4200, 5200, 6200, 7200], [4200, 5200, 6200, 7200, 8200], [5200, 6200, 7200, 8200, 9200], [300, 1300, 2300, 3300, 4300], [1300, 2300, 3300, 4300, 5300], [2300, 3300, 4300, 5300, 6300], [3300, 4300, 5300, 6300, 7300], [4300, 5300, 6300, 7300, 8300], [5300, 6300, 7300, 8300, 9300], [400, 1400, 2400, 3400, 4400], [1400, 2400, 3400, 4400, 5400], [2400, 3400, 4400, 5400, 6400], [3400, 4400, 5400, 6400, 7400], [4400, 5400, 6400, 7400, 8400], [5400, 6400, 7400, 8400, 9400], [500, 1500, 2500, 3500, 4500], [1500, 2500, 3500, 4500, 5500], [2500, 3500, 4500, 5500, 6500], [3500, 4500, 5500, 6500, 7500], [4500, 5500, 6500, 7500, 8500], [5500, 6500, 7500, 8500, 9500], [600, 1600, 2600, 3600, 4600], [1600, 2600, 3600, 4600, 5600], [2600, 3600, 4600, 5600, 6600], [3600, 4600, 5600, 6600, 7600], [4600, 5600, 6600, 7600, 8600], [5600, 6600, 7600, 8600, 9600], [700, 1700, 2700, 3700, 4700], [1700, 2700, 3700, 4700, 5700], [2700, 3700, 4700, 5700, 6700], [3700, 4700, 5700, 6700, 7700], [4700, 5700, 6700, 7700, 8700], [5700, 6700, 7700, 8700, 9700], [800, 1800, 2800, 3800, 4800], [1800, 2800, 3800, 4800, 5800], [2800, 3800, 4800, 5800, 6800], [3800, 4800, 5800, 6800, 7800], [4800, 5800, 6800, 7800, 8800], [5800, 6800, 7800, 8800, 9800], [900, 1900, 2900, 3900, 4900], [1900, 2900, 3900, 4900, 5900], [2900, 3900, 4900, 5900, 6900], [3900, 4900, 5900, 6900, 7900], [4900, 5900, 6900, 7900, 8900], [5900, 6900, 7900, 8900, 9900], [1000, 2000, 3000, 4000, 5000], [2000, 3000, 4000, 5000, 6000], [3000, 4000, 5000, 6000, 7000], [4000, 5000, 6000, 7000, 8000], [5000, 6000, 7000, 8000, 9000], [6000, 7000, 8000, 9000, 10000]]
    //Sum of values: [1500, 2000, 2500, 3000, 3500, 4000, 6500, 7000, 7500, 8000, 8500, 9000, 11500, 12000, 12500, 13000, 13500, 14000, 16500, 17000, 17500, 18000, 18500, 19000, 21500, 22000, 22500, 23000, 23500, 24000, 26500, 27000, 27500, 28000, 28500, 29000, 31500, 32000, 32500, 33000, 33500, 34000, 36500, 37000, 37500, 38000, 38500, 39000, 41500, 42000, 42500, 43000, 43500, 44000, 46500, 47000, 47500, 48000, 48500, 49000, 10500, 15500, 20500, 25500, 30500, 35500, 11000, 16000, 21000, 26000, 31000, 36000, 11500, 16500, 21500, 26500, 31500, 36500, 12000, 17000, 22000, 27000, 32000, 37000, 12500, 17500, 22500, 27500, 32500, 37500, 13000, 18000, 23000, 28000, 33000, 38000, 13500, 18500, 23500, 28500, 33500, 38500, 14000, 19000, 24000, 29000, 34000, 39000, 14500, 19500, 24500, 29500, 34500, 39500, 15000, 20000, 25000, 30000, 35000, 40000]
    
    
    //int[][] getValuesFromSeaToNestedArray((this.reactTestSea2(), 5)
    //[[100, 200, 300, 400, 500], [200, 300, 400, 500, 600], [300, 400, 500, 600, 700], [400, 500, 600, 700, 800], [500, 600, 700, 800, 900], [600, 700, 800, 900, 1000], [1100, 1200, 1300, 1400, 1500], [1200, 1300, 1400, 1500, 1600], [1300, 1400, 1500, 1600, 1700], [1400, 1500, 1600, 1700, 1800], [1500, 1600, 1700, 1800, 1900], [1600, 1700, 1800, 1900, 2000], [2100, 2200, 2300, 2400, 2500], [2200, 2300, 2400, 2500, 2600], [2300, 2400, 2500, 2600, 2700], [2400, 2500, 2600, 2700, 2800], [2500, 2600, 2700, 2800, 2900], [2600, 2700, 2800, 2900, 3000], [3100, 3200, 3300, 3400, 3500], [3200, 3300, 3400, 3500, 3600], [3300, 3400, 3500, 3600, 3700], [3400, 3500, 3600, 3700, 3800], [3500, 3600, 3700, 3800, 3900], [3600, 3700, 3800, 3900, 4000], [4100, 4200, 4300, 4400, 4500], [4200, 4300, 4400, 4500, 4600], [4300, 4400, 4500, 4600, 4700], [4400, 4500, 4600, 4700, 4800], [4500, 4600, 4700, 4800, 4900], [4600, 4700, 4800, 4900, 5000], [5100, 5200, 5300, 5400, 5500], [5200, 5300, 5400, 5500, 5600], [5300, 5400, 5500, 5600, 5700], [5400, 5500, 5600, 5700, 5800], [5500, 5600, 5700, 5800, 5900], [5600, 5700, 5800, 5900, 6000], [6100, 6200, 6300, 6400, 6500], [6200, 6300, 6400, 6500, 6600], [6300, 6400, 6500, 6600, 6700], [6400, 6500, 6600, 6700, 6800], [6500, 6600, 6700, 6800, 6900], [6600, 6700, 6800, 6900, 7000], [7100, 7200, 7300, 7400, 7500], [7200, 7300, 7400, 7500, 7600], [7300, 7400, 7500, 7600, 7700], [7400, 7500, 7600, 7700, 7800], [7500, 7600, 7700, 7800, 7900], [7600, 7700, 7800, 7900, 8000], [8100, 8200, 8300, 8400, 8500], [8200, 8300, 8400, 8500, 8600], [8300, 8400, 8500, 8600, 8700], [8400, 8500, 8600, 8700, 8800], [8500, 8600, 8700, 8800, 8900], [8600, 8700, 8800, 8900, 9000], [9100, 9200, 9300, 9400, 9500], [9200, 9300, 9400, 9500, 9600], [9300, 9400, 9500, 9600, 9700], [9400, 9500, 9600, 9700, 9800], [9500, 9600, 9700, 9800, 9900], [9600, 9700, 9800, 9900, 10000], [100, 1100, 2100, 3100, 4100], [1100, 2100, 3100, 4100, 5100], [2100, 3100, 4100, 5100, 6100], [3100, 4100, 5100, 6100, 7100], [4100, 5100, 6100, 7100, 8100], [5100, 6100, 7100, 8100, 9100], [200, 1200, 2200, 3200, 4200], [1200, 2200, 3200, 4200, 5200], [2200, 3200, 4200, 5200, 6200], [3200, 4200, 5200, 6200, 7200], [4200, 5200, 6200, 7200, 8200], [5200, 6200, 7200, 8200, 9200], [300, 1300, 2300, 3300, 4300], [1300, 2300, 3300, 4300, 5300], [2300, 3300, 4300, 5300, 6300], [3300, 4300, 5300, 6300, 7300], [4300, 5300, 6300, 7300, 8300], [5300, 6300, 7300, 8300, 9300], [400, 1400, 2400, 3400, 4400], [1400, 2400, 3400, 4400, 5400], [2400, 3400, 4400, 5400, 6400], [3400, 4400, 5400, 6400, 7400], [4400, 5400, 6400, 7400, 8400], [5400, 6400, 7400, 8400, 9400], [500, 1500, 2500, 3500, 4500], [1500, 2500, 3500, 4500, 5500], [2500, 3500, 4500, 5500, 6500], [3500, 4500, 5500, 6500, 7500], [4500, 5500, 6500, 7500, 8500], [5500, 6500, 7500, 8500, 9500], [600, 1600, 2600, 3600, 4600], [1600, 2600, 3600, 4600, 5600], [2600, 3600, 4600, 5600, 6600], [3600, 4600, 5600, 6600, 7600], [4600, 5600, 6600, 7600, 8600], [5600, 6600, 7600, 8600, 9600], [700, 1700, 2700, 3700, 4700], [1700, 2700, 3700, 4700, 5700], [2700, 3700, 4700, 5700, 6700], [3700, 4700, 5700, 6700, 7700], [4700, 5700, 6700, 7700, 8700], [5700, 6700, 7700, 8700, 9700], [800, 1800, 2800, 3800, 4800], [1800, 2800, 3800, 4800, 5800], [2800, 3800, 4800, 5800, 6800], [3800, 4800, 5800, 6800, 7800], [4800, 5800, 6800, 7800, 8800], [5800, 6800, 7800, 8800, 9800], [900, 1900, 2900, 3900, 4900], [1900, 2900, 3900, 4900, 5900], [2900, 3900, 4900, 5900, 6900], [3900, 4900, 5900, 6900, 7900], [4900, 5900, 6900, 7900, 8900], [5900, 6900, 7900, 8900, 9900], [1000, 2000, 3000, 4000, 5000], [2000, 3000, 4000, 5000, 6000], [3000, 4000, 5000, 6000, 7000], [4000, 5000, 6000, 7000, 8000], [5000, 6000, 7000, 8000, 9000], [6000, 7000, 8000, 9000, 10000]]
    
    
    //int[][] nestedArray = combinations(this.getEmptySea(), 5);
    //[[0, 1, 2, 3, 4], [1, 2, 3, 4, 5], [2, 3, 4, 5, 6], [3, 4, 5, 6, 7], [4, 5, 6, 7, 8], [5, 6, 7, 8, 9], [10, 11, 12, 13, 14],.....
    // ....., [94, 95, 96, 97, 98], [95, 96, 97, 98, 99], [0, 10, 20, 30, 40], [10, 20, 30, 40, 50], [20, 30, 40, 50, 60], [30, 40, 50, 60, 70],....
    
    
    //ArrayList<Integer> opg1 = this.spaceForShipIndexPlusOneER(this.getEmptySea(), 5);
    // opg1 is now a list of the possible indexes ( getEmptySea(), 5 );
    // opg1: 160 -- [1, 2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 26, 31, 32, 33, 34, 35, 36, 41, 42, 43, 44, 45, 46, 51, 52, 53, 54, 55, 56, 61, 62, 63, 64, 65, 66, 71, 72, 73, 74, 75, 76, 81, 82, 83, 84, 85, 86, 91, 92, 93, 94, 95, 96, -1, -11, -21, -31, -41, -51, -2, -12, -22, -32, -42, -52, -3, -13, -23, -33, -43, -53, -4, -14, -24, -34, -44, -54, -5, -15, -25, -35, -45, -55, -6, -16, -26, -36, -46, -56, -7, -17, -27, -37, -47, -57, -8, -18, -28, -38, -48, -58, -9, -19, -29, -39, -49, -59, -10, -20, -30, -40, -50, -60]

    


    /*
     int myIntCoor = coordinatePlusOne(testHM, 2, true);
    322  460  560  626  637  645  660  619  480  305 
    443  594  743  804  782  774  778  704  630  490 
    559  680  783  862  818  888  837  783  744  611 
    642  748  863  920  888  948  867  843  778  624 
    688  746  920  964  950 [995] 897  861  786  642 
    672  724  913  958  967  977  920  882  795  643 
    660  728  868  922  944  967  904  852  754  616 
    579  690  812  860  899  919  895  802  721  572 
    461  577  667  743  759  796  770  698  638  473 
    299  470  570  638  656  661  653  593  493  335 

    myIntCoor: -81
    */
    //takes a heatmap and a shipsize and a boolean "lowest"
    // return the coordinatePlusOne for the shipplacement with lowest aveage values
    // if an index is < 0: then a ship wont be placed on it, and its coordinates wont come out.
    public int coordinatePlusOneFromHM(int[] heatmap, int shipsize, boolean lowestValue, boolean[] allowesspaces){
        int desiredCoor = 0;
        int theCoorIndex = 0;
        int theCoorValue = 0;
        
        ArrayList<Integer> opg1 = this.spaceForShipIndexPlusOneER(this.getEmptySea(), shipsize);
        int[][] nestedArray = combinations(this.getEmptySea(), shipsize);
        int[][]values = getValuesFromSeaToNestedArray(heatmap, shipsize);
        int[] sumOfValues = sumOfValues(values);
        
//        //adon to make sure a ship wont be placed on -1.
//        //*******************************
//        for (int i = 0; i < values.length; i++) {
//            for (int j = 0; j < values[i].length; j++) {
//                if (values[i][j] < 0) {
//                    sumOfValues[i] = Integer.MIN_VALUE; //arbitrært tal under 0
//                }
//            }
//        }
//        //*********************************
          
        if (lowestValue) {
            theCoorValue = Integer.MAX_VALUE;
            for (int i = 0; i < sumOfValues.length; i++) {
                if (sumOfValues[i] < theCoorValue) {//&& allowesspaces[i] == true)//&& sumOfValues[i] > -1)
                    boolean acceptableIndex = true;
                    for (int j = 0; j < shipsize; j++) {
                        if (allowesspaces[nestedArray[i][j]] = false) {
                            acceptableIndex = false;
                        }
                    }
                    if (acceptableIndex) {
                        theCoorValue = sumOfValues[i];
                        theCoorIndex = i;
                    }
                    
                    
                }
            }
        }else{
            theCoorValue = 0;
            for (int i = 0; i < sumOfValues.length; i++) {
                if (sumOfValues[i] < theCoorValue) {//&& allowesspaces[i] == true)//&& sumOfValues[i] > -1)
                    boolean acceptableIndex = true;
                    for (int j = 0; j < shipsize; j++) {
                        if (allowesspaces[nestedArray[i][j]] = false) {
                            acceptableIndex = false;
                        }
                    }
                    if (acceptableIndex) {
                        theCoorValue = sumOfValues[i];
                        theCoorIndex = i;
                    }
                    
                    
                }
            }
        }
        desiredCoor = opg1.get(theCoorIndex);
        return desiredCoor;
        
    }
    
    /*
    public int coordinatePlusOneFromHM(int[] heatmap, int shipsize, boolean lowestValue){
        int desiredCoor = 0;
        int theCoorIndex = 0;
        int theCoorValue = 0;
        
        ArrayList<Integer> opg1 = this.spaceForShipIndexPlusOneER(this.getEmptySea(), shipsize);
        int[][]values = getValuesFromSeaToNestedArray(heatmap, shipsize);
        int[] sumOfValues = sumOfValues(values);
        
        if (lowestValue) {
            theCoorValue = Integer.MAX_VALUE;
            for (int i = 0; i < sumOfValues.length; i++) {
                if (sumOfValues[i] < theCoorValue) {
                    theCoorValue = sumOfValues[i];
                    theCoorIndex = i;
                    
                }
            }
        }else{
            theCoorValue = 0;
            for (int i = 0; i < sumOfValues.length; i++) {
                if (sumOfValues[i] > theCoorValue) {
                    theCoorValue = sumOfValues[i];
                    theCoorIndex = i;
                }
            }
        }
        desiredCoor = opg1.get(theCoorIndex);
        return desiredCoor;
        
    }
    */
    
    private int[] sumOfValues(int[][] nestedArray){
        ArrayList<Integer> opg1 = this.spaceForShipIndexPlusOneER(this.getEmptySea(), nestedArray[0].length);
        int[] output = new int[opg1.size()];
        int sum = 0;
        for (int i = 0; i < nestedArray.length; i++) {
            sum = 0;
            for (int j = 0; j < nestedArray[i].length; j++) {
                sum += nestedArray[i][j];
            }
            output[i] = sum;
            
        }
        
        return output;
    }
    
    private int[][] getValuesFromSeaToNestedArray(int[] sea, int shiplength){
//        ArrayList<Integer> IndexPlusOneArray = this.spaceForShipIndexPlusOneER(this.getEmptySea(), 5);
        int[][] nestedArray = combinations(this.getEmptySea(), shiplength);
        int[][] output = combinations(this.getEmptySea(), shiplength);
        int tempInt = 0;
        
        for (int i = 0; i < nestedArray.length; i++) {
            for (int j = 0; j < nestedArray[i].length; j++) {
                tempInt = nestedArray[i][j];
                output[i][j] = sea[tempInt];
            }
        }        
        return output;
    }
    
    /**
     * 1. opgave: lav en array over alle mulige skibsplaceringer. (med plusOne, + for positiv, - for negativ)
     * 2. opgave: lave en søster array hvor hver værdi ikke er et index, men dette index's værdi!!!
     * 3 opgave: lav en array: hver værdi er summen af værdierne i opgave 2.
     *
     * @param enemyMoveMap
     * @param shiplength 
     */
    private int[][] combinations(int[] sea, int shiplength){  //int[] enemyMoveMap,
        
        // opg1 is now a list of the possible indexes ( getEmptySea(), 3 );
        // opg1: 160 -- [1, 2, 3, 4, 5, 6, 7, 8, 11, 12, 13, 14, 15, 16, 17, 18, 21, 22, 23, 24, 25, 26, 27, 28, 31, 32, 33, 34, 35, 36, 37, 38, 41, 42, 43, 44, 45, 46, 47, 48, 51, 52, 53, 54, 55, 56, 57, 58, 61, 62, 63, 64, 65, 66, 67, 68, 71, 72, 73, 74, 75, 76, 77, 78, 81, 82, 83, 84, 85, 86, 87, 88, 91, 92, 93, 94, 95, 96, 97, 98, -1, -11, -21, -31, -41, -51, -61, -71, -2, -12, -22, -32, -42, -52, -62, -72, -3, -13, -23, -33, -43, -53, -63, -73, -4, -14, -24, -34, -44, -54, -64, -74, -5, -15, -25, -35, -45, -55, -65, -75, -6, -16, -26, -36, -46, -56, -66, -76, -7, -17, -27, -37, -47, -57, -67, -77, -8, -18, -28, -38, -48, -58, -68, -78, -9, -19, -29, -39, -49, -59, -69, -79, -10, -20, -30, -40, -50, -60, -70, -80]
        ArrayList<Integer> opg1 = this.spaceForShipIndexPlusOneER(this.getEmptySea(), shiplength);
        int[] opg2 = new int[opg1.size()];
        int[][] opg3 = new int[opg1.size()][];
        int[] temp = new int[shiplength];
        //CorrListFromCorrAndShiplength(int CorrPlusOne, int shiplength){
        for (int i = 0; i < opg1.size(); i++) {
            temp = CorrListFromCorrAndShiplength(opg1.get(i), shiplength);
            opg3[i] = temp;
        }
               
        
        return opg3;
    }
    
    // tag et koordinat og en skibslængde, 
    // og find de tilsvarende indexpunkter
    private int[] CorrListFromCorrAndShiplength(int CorrPlusOne, int shiplength){
        int[] output = new int[shiplength];
        int firstCoordinate = 0;
        
        if (CorrPlusOne > 0){
            firstCoordinate = CorrPlusOne-1;
            for (int i = 0; i < shiplength; i++) {
                output[i] = firstCoordinate+i;
            }
        }else if(CorrPlusOne < 0){
            firstCoordinate = Math.abs(CorrPlusOne)-1;
            for (int i = 0; i < shiplength; i++) {
                output[i] = firstCoordinate+(i*10);//output.add(firstCoordinate+(i*10));
            }
        }else{
            System.out.println("EnemyReact: Fejl i CorrListFromCorrAndShiplength: CorrPlusOne == 0 !!!! (skal være > 0 eller < 0).");
        }
        
        return output;
    }
    
    
    
    
    private ArrayList<Integer> spaceForShipIndexPlusOneER(int[] sea, int shiplength) {
        ArrayList<Integer> numOfTimesThereIsSpace = new ArrayList<Integer>();
//        boolean output = false;
        int horizontal = 1;
//        boolean hor = false;
        for (int i = 1; i < sea.length; i++) {
            if (sea[i] == 1 && sea[i - 1] == 1) {
                horizontal++;
            } else if (sea[i] != 1) {
                horizontal = 1;
            }

            if (horizontal >= shiplength) {
                numOfTimesThereIsSpace.add(i - (shiplength - 1)       +1 ); // +1 to avoid "+0" and "-0"
//                hor = true;
            }
            if (i % 10 == 9) {
                i++;
                horizontal = 1;
            }
        }

        int vertical = 1;
//        boolean ver = false;
        for (int i = 10; i < sea.length; i += 10) {
            if (sea[i] == 1 && sea[i - 10] == 1) {
                vertical++;
                //System.out.println(i);
            } else if (sea[i] != 1) {
                vertical = 1;
            }

            if (vertical >= shiplength) {
                numOfTimesThereIsSpace.add(-(i - (10*(shiplength - 1)))      -1); //  // +1 to avoid "+0" and "-0"             
 //               ver = true;
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
    
    public Position getPosFromCoor(int Coordinate) {
        //int Xcoordinate = 0;
        int candidateMove = Coordinate;
        int Xcoordinate = 0;
        int Ycoordinate = 0;
        if (Coordinate < 0) {
            candidateMove = (-1*candidateMove) -1;
            Xcoordinate = candidateMove % 10;
            Ycoordinate = 9 - (candidateMove / 10);
        }else{
            candidateMove = candidateMove -1;
            Xcoordinate = candidateMove % 10;
            Ycoordinate = 9 - (candidateMove / 10);
        }     
        
        Position pos = new Position(Xcoordinate, Ycoordinate);

        return pos;
    }
    
    private int[] getEmptySea() {
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