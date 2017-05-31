package g04;

import battleship.interfaces.Position;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

/*
2017-05-19 - kl. 6.28 - Chr.
denne klasse er til funktioner der reagere på den information vi får fra spilleren.
 */
/**
 *
 * @author Christian, Gert, Lene
 */
public class EnemyReact {

    //denne er til den algoritme der skal korrigere skudalgoritmen 
    // ud fra hvordan modstanderen placere skibe.
    //if the algoritm doesnt find any interesting place to shoot:
    // return empty ArrayList. 
    public ArrayList<Integer> indexesFromEnemyShipMatch(int[] enemyShipMatch, int roundNumber, ArrayList<Position> shotsFired) {
        //int output = -1;
        ArrayList<Integer> hardCodedIndexes = new ArrayList<Integer>();
        ArrayList<Double> valueDebugger1 = new ArrayList<Double>();
        ArrayList<Double> valueDebugger2 = new ArrayList<Double>();
        ArrayList<Double> valueDebugger3 = new ArrayList<Double>();
        int[] enemyShipMatchCopy = new int[enemyShipMatch.length];
        System.arraycopy(enemyShipMatch, 0, enemyShipMatchCopy, 0, enemyShipMatch.length);
        //find the maximum value in enemyShipMatchCopy:
        int maxValue = Arrays.stream(enemyShipMatchCopy).max().getAsInt();
        
        try {
            
            while ( ((double) maxValue-1) / ((double) roundNumber-1) >= 0.4) { //tæller-1 for at fjerne emptySea 1'ere. nævner-1 fordi kortet er en runde bagud.
                                                        //>= 40.0 skulle være 0.4 (40%) men af en eller anden grund bliver resultaterne væsentligt højere
                valueDebugger1.add(((double) maxValue-1) / ((double) roundNumber-1));
                valueDebugger2.add(((double) maxValue-1));
                valueDebugger3.add(((double) roundNumber-1));
                
                for (int i = 0; i < enemyShipMatchCopy.length; i++) {
                    // hvis et punkt i enemyShipMatch har samme værdi som roundCount,
                    // betyder det at modstanderen altid placere skibe der.
                    // i så fald skal vi altid huske at skyde der.
                    if (enemyShipMatchCopy[i] == maxValue) {
                        //count them only as hardcoded if the ships have been placed in the same spot more than twice.
                        hardCodedIndexes.add(i);
                        enemyShipMatchCopy[i] = 0;
                    }
                }                
                maxValue = Arrays.stream(enemyShipMatchCopy).max().getAsInt();
            }
        } catch (Exception e) {
            hardCodedIndexes = new ArrayList<Integer>();
        };
        
        
        return hardCodedIndexes;
    }

    public Position getPosFromIndex(int index) {
        int Xcoordinate = index % 10;
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
    public int[] efficientHeatMap(int[] sea, ArrayList<Integer> fleet) {
        int[] outputCounterSea = new int[100];
        int[] emptysea = sea;
        Random r = new Random();
        int myRandom = 0;

        ArrayList<int[]> possiblePlaces = new ArrayList<int[]>();

        for (int i = 0; i < fleet.size(); i++) {
            int[][] nestedArrayIndexes = combinations(emptysea, fleet.get(i));
            int[][] copyToManipulate = combinations(emptysea, fleet.get(i));
            for (int j = 0; j < nestedArrayIndexes.length; j++) {
                for (int k = 0; k < nestedArrayIndexes[j].length; k++) {
                    if (sea[nestedArrayIndexes[j][k]] < 1) {
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

    public int[] efficientDistributeShips(int[] sea, int[] fleet) {
        int[] emptysea = sea;

        Random r = new Random();
        int myRandom = 0;
        ArrayList<Integer> usedPoints = new ArrayList<Integer>();
        ArrayList<int[]> possiblePlaces = new ArrayList<int[]>();
        int[] chosenPlace = null;

        for (int i = 0; i < fleet.length; i++) {
            int[][] nestedArrayIndexes = combinations(emptysea, fleet[i]);
            int[][] copyToManipulate = combinations(emptysea, fleet[i]);
            for (int j = 0; j < nestedArrayIndexes.length; j++) {
                for (int k = 0; k < nestedArrayIndexes[j].length; k++) {
                    if (usedPoints.contains(nestedArrayIndexes[j][k])
                            || emptysea[nestedArrayIndexes[j][k]] < 1) {
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
    
    public int[] ramdomArrayOneToHundred() {
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

    private int[] sumOfValues(int[][] nestedArray) {
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

    private int[][] getValuesFromSeaToNestedArray(int[] sea, int shiplength) {
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
     * 1. opgave: lav en array over alle mulige skibsplaceringer. (med plusOne,
     * + for positiv, - for negativ) 2. opgave: lave en søster array hvor hver
     * værdi ikke er et index, men dette index's værdi!!! 3 opgave: lav en
     * array: hver værdi er summen af værdierne i opgave 2.
     *
     * @param enemyMoveMap
     * @param shiplength
     */
    public int[][] combinations(int[] sea, int shiplength) {  //int[] enemyMoveMap,

        // opg1 is now a list of the possible indexes ( getEmptySea(), 3 );
        // opg1: 160 -- [1, 2, 3, 4, 5, 6, 7, 8, 11, 12, 13, 14, 15, 16, 17, 18, 21, 22, 23, 24, 25, 26, 27, 28, 31, 32, 33, 34, 35, 36, 37, 38, 41, 42, 43, 44, 45, 46, 47, 48, 51, 52, 53, 54, 55, 56, 57, 58, 61, 62, 63, 64, 65, 66, 67, 68, 71, 72, 73, 74, 75, 76, 77, 78, 81, 82, 83, 84, 85, 86, 87, 88, 91, 92, 93, 94, 95, 96, 97, 98, -1, -11, -21, -31, -41, -51, -61, -71, -2, -12, -22, -32, -42, -52, -62, -72, -3, -13, -23, -33, -43, -53, -63, -73, -4, -14, -24, -34, -44, -54, -64, -74, -5, -15, -25, -35, -45, -55, -65, -75, -6, -16, -26, -36, -46, -56, -66, -76, -7, -17, -27, -37, -47, -57, -67, -77, -8, -18, -28, -38, -48, -58, -68, -78, -9, -19, -29, -39, -49, -59, -69, -79, -10, -20, -30, -40, -50, -60, -70, -80]
        ArrayList<Integer> opg1 = this.spaceForShipIndexPlusOneER(this.getEmptySea(), shiplength);
        int[] opg2 = new int[opg1.size()];
        int[][] opg3 = new int[opg1.size()][];
        int[] temp = new int[shiplength];
        for (int i = 0; i < opg1.size(); i++) {
            temp = CorrListFromCorrAndShiplength(opg1.get(i), shiplength);
            opg3[i] = temp;
        }

        return opg3;
    }

    // tag et koordinat og en skibslængde, 
    // og find de tilsvarende indexpunkter
    private int[] CorrListFromCorrAndShiplength(int CorrPlusOne, int shiplength) {
        int[] output = new int[shiplength];
        int firstCoordinate = 0;

        if (CorrPlusOne > 0) {
            firstCoordinate = CorrPlusOne - 1;
            for (int i = 0; i < shiplength; i++) {
                output[i] = firstCoordinate + i;
            }
        } else if (CorrPlusOne < 0) {
            firstCoordinate = Math.abs(CorrPlusOne) - 1;
            for (int i = 0; i < shiplength; i++) {
                output[i] = firstCoordinate + (i * 10);
            }
        } else {
            System.out.println("EnemyReact: Fejl i CorrListFromCorrAndShiplength: CorrPlusOne == 0 !!!! (skal være > 0 eller < 0).");
        }

        return output;
    }

    private ArrayList<Integer> spaceForShipIndexPlusOneER(int[] sea, int shiplength) {
        ArrayList<Integer> numOfTimesThereIsSpace = new ArrayList<Integer>();
        int horizontal = 1;
        for (int i = 1; i < sea.length; i++) {
            if (sea[i] == 1 && sea[i - 1] == 1) {
                horizontal++;
            } else if (sea[i] != 1) {
                horizontal = 1;
            }

            if (horizontal >= shiplength) {
                numOfTimesThereIsSpace.add(i - (shiplength - 1) + 1); 
            }
            if (i % 10 == 9) {
                i++;
                horizontal = 1;
            }
        }

        int vertical = 1;
        for (int i = 10; i < sea.length; i += 10) {
            if (sea[i] == 1 && sea[i - 10] == 1) {
                vertical++;
            } else if (sea[i] != 1) {
                vertical = 1;
            }

            if (vertical >= shiplength) {
                numOfTimesThereIsSpace.add(-(i - (10 * (shiplength - 1))) - 1); 
            }
            if (i >= 90 && i != 99) {
                int temp = i - 90;
                i = 1 + temp;
            }
            //extra test
            if (i / 10 == 0) {
                vertical = 1;
            }
        }

        return numOfTimesThereIsSpace;
    }

    public Position getPosFromCoor(int Coordinate) {
        int candidateMove = Coordinate;
        int Xcoordinate = 0;
        int Ycoordinate = 0;
        if (Coordinate < 0) {
            candidateMove = (-1 * candidateMove) - 1;
            Xcoordinate = candidateMove % 10;
            Ycoordinate = 9 - (candidateMove / 10);
        } else {
            candidateMove = candidateMove - 1;
            Xcoordinate = candidateMove % 10;
            Ycoordinate = 9 - (candidateMove / 10);
        }

        Position pos = new Position(Xcoordinate, Ycoordinate);

        return pos;
    }

    public int[] getEmptySea() {
        int[] output = new int[100];
        for (int i = 0; i < output.length; i++) {
            output[i] = 1;
        }
        return output;
    }

    public int[] getSeaFromShotsFired(ArrayList<Position> shotsFired) {
        int[] output = this.getEmptySea();
        for (int i = 0; i < shotsFired.size(); i++) {
            int intFromPos = this.getIndexFromPos(shotsFired.get(i));
            output[intFromPos] = -1;
        }
        return output;
    }

    public void runPlaceShipsChr0525() {
        ArrayList<Position> emptypos = new ArrayList<Position>();
        //test shoot at static target
        ArrayList<Integer> likelyIndexes = indexesFromEnemyShipMatch(
                this.reactTestSea5(),
                10000,
                emptypos
        );
        this.printHeatmap(1, reactTestSea5());
        System.out.println("ArrayList: ");
        System.out.println(likelyIndexes);
    }

    public ArrayList<int[]> placeShipsChr0525HeighestValue(int[] enemyShotMatch, ArrayList<Integer> fleet) {
        ArrayList<int[]> shipIndexesToPlaceOnSea = new ArrayList<int[]>();
        boolean[] allowedSpaces = new boolean[100];
        for (int i = 0; i < allowedSpaces.length; i++) {
            allowedSpaces[i] = true;
        }

        //2017-05-15-kl.15.39 forsøg på at lave placeship funktionen igen;
        int[][] nestedArray = null; //til "[[0, 1, 2, 3, 4], [1, 2, 3, 4, 5],...
        int[][] values = null; //til "[[43, 2, 35, 521, 87], ...
        int[][] sumOfValues = null;//til "[[688],......
        int[][] heighestValueArr = null;
        int heighestValueInt = 0;
        int[] targetIndexArray = null;

        for (int i = 0; i < fleet.size(); i++) {
            int ship = fleet.get(i);
            //2017-05-29 - Lene + Chr - placer 2'er skib langs kanten
            if (ship == 2) {
                for (int j = 0; j < allowedSpaces.length; j++) {
                     if(j%10 > 0 && j%10 < 9 && j/10 > 0 && j/10 < 9){
                    allowedSpaces[j] = false;
                    }
                    
                }
            }
            
            
            
            nestedArray = combinations(getEmptySea(), ship);
            values = combinations(getEmptySea(), ship);
            
            //loop over values og erstat indexerne med deres enemyShotMatch værdier.
            for (int j = 0; j < nestedArray.length; j++) {
                for (int k = 0; k < nestedArray[j].length; k++) {
                    //values = "[[43, 2, 35, 521, 87], ......
                    values[j][k] = enemyShotMatch[nestedArray[j][k]];
                }
            }
            int[] eachShipSumArray = new int[1];
            sumOfValues = new int[values.length][];
            heighestValueArr = new int[values.length][];
            for (int j = 0; j < values.length; j++) {
                int eachShipSum = 0;
                heighestValueInt = values[j][0];
                for (int k = 0; k < values[j].length; k++) {
                    //sumOfValues 5'er skib: "[[688], ..."
                    eachShipSum += values[j][k];
                    if (values[j][k] > heighestValueInt) {
                        heighestValueInt = values[j][k];
                    }
                }
                eachShipSumArray[0] = eachShipSum;
                sumOfValues[j] = eachShipSumArray;

                eachShipSumArray[0] = heighestValueInt;
                heighestValueArr[j] = eachShipSumArray;
                eachShipSumArray = new int[1];

            }
            targetIndexArray = null;
            int targetIndex = -1;
            int targetSum = Integer.MAX_VALUE;//sumOfValues[0][0];
            int currentSum = 0;

            //******************************************************
            //her findes det afgørende indexnummer i nestedArray
            //******************************************************
            for (int j = 0; j < nestedArray.length; j++) {
                boolean indexIsOk = true;
                for (int k = 0; k < nestedArray[j].length; k++) {
                    if (allowedSpaces[nestedArray[j][k]] == false) {
                        indexIsOk = false;
                    }
                }
                if (indexIsOk == true) {

                    //***********************************************************************
                    //Hvis SumOfValues ønskes: brug currentSum = sumOfValues[j][0];
                    //Hvis heighest value ønskes: brug currentSum = heighestValueArr[j][0];
                    //**********************************************************************
                    //currentSum = sumOfValues[j][0];
                    currentSum = heighestValueArr[j][0];

                    if (currentSum < targetSum) {
                        targetSum = currentSum;
                        targetIndexArray = nestedArray[j];
                        targetIndex = j;
                    }
                }
            }

            //allowspaces skal testes:

            //targetIndexArray er korrekt beregnet. Nu skal allowedSpaces opdateres.
            for (int j = 0; j < targetIndexArray.length; j++) {
                allowedSpaces[targetIndexArray[j]] = false;

            }
            shipIndexesToPlaceOnSea.add(targetIndexArray);
            
            if (ship == 2) {
                for (int j = 0; j < allowedSpaces.length; j++) {
                     if(j%10 > 0 && j%10 < 9 && j/10 > 0 && j/10 < 9){ 
                    allowedSpaces[j] = true;
                    }
                }
            }
            
        }

        return shipIndexesToPlaceOnSea;
    }

    public ArrayList<int[]> placeShipsChr0525AverageValue(int[] enemyShotMatch, ArrayList<Integer> fleet) {
        ArrayList<int[]> shipIndexesToPlaceOnSea = new ArrayList<int[]>();
        boolean[] allowedSpaces = new boolean[100];
        for (int i = 0; i < allowedSpaces.length; i++) {
            allowedSpaces[i] = true;
        }

        //2017-05-15-kl.15.39 forsøg på at lave placeship funktionen igen;
        int[][] nestedArray = null; //til "[[0, 1, 2, 3, 4], [1, 2, 3, 4, 5],...
        int[][] values = null; //til "[[43, 2, 35, 521, 87], ...
        int[][] sumOfValues = null;//til "[[688],......
        int[] targetIndexArray = null;

        for (int i = 0; i < fleet.size(); i++) {
            //System.out.println("");

            int ship = fleet.get(i);
            nestedArray = combinations(getEmptySea(), ship);
            values = combinations(getEmptySea(), ship);
            //nested array: 5'er skib: "[[0, 1, 2, 3, 4], [1, 2, 3, 4, 5],...[95, 96, 97, 98, 99], [0, 10, 20, 30, 40], [10, 20, 30, 40, 50],..." 

            //loop over values og erstat indexerne med deres enemyShotMatch værdier.
            for (int j = 0; j < nestedArray.length; j++) {
                for (int k = 0; k < nestedArray[j].length; k++) {
                    //values = "[[43, 2, 35, 521, 87], ......
                    values[j][k] = enemyShotMatch[nestedArray[j][k]];
                }
            }
            int[] eachShipSumArray = new int[1];
            sumOfValues = new int[values.length][];
            for (int j = 0; j < values.length; j++) {
                int eachShipSum = 0;
                for (int k = 0; k < values[j].length; k++) {
                    //sumOfValues 5'er skib: "[[688], null..."
                    eachShipSum += values[j][k];
                }
                eachShipSumArray[0] = eachShipSum;
                sumOfValues[j] = eachShipSumArray;
                eachShipSumArray = new int[1];

            }
            targetIndexArray = null;
            int targetIndex = -1;
            int targetSum = Integer.MAX_VALUE;//sumOfValues[0][0];
            int currentSum = 0;

            //******************************************************
            //her findes det afgørende indexnummer i nestedArray
            //******************************************************
            for (int j = 0; j < nestedArray.length; j++) {
                boolean indexIsOk = true;
                for (int k = 0; k < nestedArray[j].length; k++) {
                    if (allowedSpaces[nestedArray[j][k]] == false) {
                        indexIsOk = false;
                    }
                }
                if (indexIsOk == true) {
                    currentSum = sumOfValues[j][0];
                    if (currentSum < targetSum) {
                        targetSum = currentSum;
                        targetIndexArray = nestedArray[j];
                        targetIndex = j;
                    }
                }
            }

           

            //targetIndexArray er korrekt beregnet. Nu skal allowedSpaces opdateres.
            for (int j = 0; j < targetIndexArray.length; j++) {
                
                allowedSpaces[targetIndexArray[j]] = false;

            }
            
            shipIndexesToPlaceOnSea.add(targetIndexArray);
        }

        return shipIndexesToPlaceOnSea;
    }

    public void printHeatmap(int divisor, int[] sea) {
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        int largestNumber = 0;
        int largestNumberLength = 0;
        int currentNumberLength = 0;

        for (int i = 0; i < sea.length; i++) {
            if (sea[i] > largestNumber) {
                largestNumber = sea[i];
            }
        }
        largestNumberLength = Integer.toString(largestNumber).length();

        for (int i = 0; i < sea.length; i++) {
            currentNumberLength = Integer.toString(sea[i]).length();
            if (sea[i] < 0) {
                currentNumberLength++;
            }
            for (int j = 0; j < largestNumberLength - currentNumberLength; j++) {
                System.out.print(" ");
            }
            if (sea[i] == largestNumber) {
                System.out.print("[" + (sea[i] / divisor) + "]");
            } else {
                System.out.print(" " + (sea[i] / divisor) + " ");
            }
            if (i % Math.sqrt(sea.length) == Math.sqrt(sea.length) - 1) {
                System.out.println();
            }
        }
    }

    public void printSea(int[] sea) {
        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        int largestNumber = 0;
        int largestNumberLength = 0;
        int currentNumberLength = 0;

        for (int i = 0; i < sea.length; i++) {
            if (sea[i] > largestNumber) {
                largestNumber = sea[i];
            }
        }
        largestNumberLength = Integer.toString(largestNumber).length();
        largestNumberLength++;

        for (int i = 0; i < sea.length; i++) {
            currentNumberLength = Integer.toString(sea[i]).length();
            if (sea[i] < 0) {
                currentNumberLength++;
            }

            for (int j = 0; j < largestNumberLength - currentNumberLength; j++) {
                System.out.print(" ");
            }

            if (sea[i] == 1) {
                System.out.print(" " + ".");
            } else if (sea[i] == -1) {
                System.out.print("  " + "+");
            } else {
                System.out.print(" " + sea[i]);
            }

            if (i % Math.sqrt(sea.length) == Math.sqrt(sea.length) - 1) {
                System.out.println();
            }
        }
    }

    private int[] reactTestSea1() {
        int[] fixedSea
                = {-1, 1, 1, 1, 1, 1, 1, 1, 1, -1,
                    1, -1, 1, 1, -1, -1, 1, 1, -1, 1,
                    1, 1, -1, -1, 1, 1, -1, -1, 1, 1,
                    1, 1, -1, -1, 1, 1, -1, -1, 1, 1,
                    1, -1, 1, 1, -1, -1, 1, 1, -1, 1,
                    1, -1, 1, 1, -1, -1, 1, 1, -1, 1,
                    1, 1, -1, -1, 1, 1, -1, -1, 1, 1,
                    1, 1, -1, -1, 1, 1, -1, -1, 1, 1,
                    1, -1, 1, 1, -1, -1, 1, 1, -1, 1,
                    -1, 1, 1, 1, 1, 1, 1, 1, 1, -1};

        return fixedSea;
    }

    private int[] reactTestSea2() {
        int[] fixedSea
                = {100, 200, 300, 400, 500, 600, 700, 800, 900, 1000,
                    1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900, 2000,
                    2100, 2200, 2300, 2400, 2500, 2600, 2700, 2800, 2900, 3000,
                    3100, 3200, 3300, 3400, 3500, 3600, 3700, 3800, 3900, 4000,
                    4100, 4200, 4300, 4400, 4500, 4600, 4700, 4800, 4900, 5000,
                    5100, 5200, 5300, 5400, 5500, 5600, 5700, 5800, 5900, 6000,
                    6100, 6200, 6300, 6400, 6500, 6600, 6700, 6800, 6900, 7000,
                    7100, 7200, 7300, 7400, 7500, 7600, 7700, 7800, 7900, 8000,
                    8100, 8200, 8300, 8400, 8500, 8600, 8700, 8800, 8900, 9000,
                    9100, 9200, 9300, 9400, 9500, 9600, 9700, 9800, 9900, 10000};

        return fixedSea;
    }

    private int[] reactTestSea3() {
        int[] fixedSea = {
            10, 74, 267, 442, 243, 498, 309, 475, 142, 66,
            143, 560, 185, 230, 679, 413, 475, 297, 503, 118,
            409, 153, 418, 249, 409, 522, 229, 446, 280, 274,
            237, 334, 484, 413, 326, 452, 477, 289, 279, 146,
            319, 433, 304, 369, 365, 787, 361, 204, 392, 405,
            508, 285, 197, 261, 603, 548, 346, 429, 452, 317,
            169, 447, 518, 191, 235, 325, 438, 419, 243, 236,
            213, 409, 333, 392, 540, 321, 169, 474, 155, 379,
            134, 374, 244, 463, 371, 510, 324, 80, 418, 139,
            10, 224, 281, 144, 257, 378, 441, 186, 139, 67
        };
        return fixedSea;
    }

    private int[] reactTestSea4() {
        int[] fixedSea = {
            2, 2, 69, 53, 2, 81, 2, 71, 2, 2,
            2, 73, 2, 52, 90, 2, 2, 2, 74, 2,
            2, 2, 93, 54, 2, 63, 94, 2, 2, 75,
            83, 2, 2, 98, 64, 2, 2, 95, 2, 76,
            85, 91, 66, 2, 2, 100, 65, 2, 2, 79,
            86, 55, 2, 58, 101, 2, 2, 61, 88, 78,
            84, 57, 96, 2, 2, 67, 99, 60, 2, 77,
            82, 56, 2, 97, 62, 2, 2, 92, 2, 2,
            2, 80, 2, 2, 2, 89, 2, 2, 72, 2,
            2, 2, 68, 2, 87, 2, 70, 2, 2, 59,};
        return fixedSea;
    }

    private int[] reactTestSea5() {
        // "R3 vs. X4" - X4's placeShip efter 10.000 spil.
        int[] fixedSea
                = {3914, 3670, 3671, 3670, 1, 2081, 2173, 2177, 2568, 4180,
                    3667, 1, 1612, 1373, 2197, 3, 2954, 2964, 628, 2675,
                    3668, 2097, 817, 578, 1404, 907, 2, 11, 2969, 2441,
                    3663, 1521, 427, 163, 1340, 843, 591, 14, 2970, 2424,
                    2, 2477, 1418, 1216, 1, 790, 783, 905, 32, 2254,
                    2354, 3, 949, 748, 796, 1, 1236, 1359, 2471, 7,
                    2358, 2869, 39, 652, 819, 1386, 121, 428, 1585, 3616,
                    2356, 2878, 51, 17, 898, 1465, 563, 818, 2141, 3615,
                    2870, 945, 2779, 2751, 55, 2177, 1312, 1569, 25, 3622,
                    4178, 2689, 2261, 2258, 2039, 33, 3672, 3672, 3679, 4008};

        return fixedSea;
    }

}
