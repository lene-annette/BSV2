/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y4;

import battleship.interfaces.Position;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Christian
 */
public class HeatMapBasic {

    private final static Random rnd = new Random();
    private int[] heatmap;

    //private HeatMapInverse hmi = new HeatMapInverse();
    /*
    
        int[] sea = new int[100];//this.fixedSeaWithSips();
        for (int i = 0; i < sea.length; i++) {
            sea[i] = 1;
        }
        
        this.printSea(sea);
        System.out.println();
        
        ArrayList<Integer> fleet = new ArrayList<Integer>();
        fleet.add(5);
        fleet.add(4);
        fleet.add(3);
        fleet.add(3);
        fleet.add(2);
        
        //sea[33] = -2;
        //sea[34] = -2;
        //sea[35] = -2;
        
        int[] heat = simpleHeatMap(sea, fleet);     
        this.printHeatmap(10, heat);
        System.out.println(this.getIntFromHeatMap(heat, fleet));
     */

 /*
        //2017-05-17 kl. 14.30 - chr - Succe test af: getPosFromStack.
        ArrayList<Position> stack = this.getPosArrListTEST();
        int[] heat = this.generateVirginHeatmap();
        Position pos = this.getPosFromStack(heat, stack);
        this.printHeatmap(10, heat);
        System.out.println(this.getArrListCorrFromArrListPos(stack));
        System.out.println(this.getCoordinateFromPos(pos));
     */
    public void run() {
        System.out.println("HeatMapBasic -- run");

        ArrayList<Integer> arrList = new ArrayList<Integer>();
        arrList = this.spaceForShipIndexPlusOne(this.antiHeatTemplatePS(), 4);
        System.out.println(arrList);
        this.printSea(this.antiHeatTemplatePS());

    }
    //public Position getPosFromStack(int[] heatmap, ArrayList<Position> stack){

    public Position getPosFromStack(int[] heatmap, ArrayList<Position> stack) {

        ArrayList<Integer> CoordinateStack = new ArrayList<Integer>();

        CoordinateStack = this.getArrListCorrFromArrListPos(stack);

        int largestCorr = 0;
        for (int i = 0; i < CoordinateStack.size(); i++) {
            if (heatmap[CoordinateStack.get(i)] > heatmap[CoordinateStack.get(largestCorr)]) {
                largestCorr = i;
            }
        }

        Position pos = this.getPosFromIndex(CoordinateStack.get(largestCorr));
        return pos;
        /*
        ArrayList<Integer> CoordinateStack = new ArrayList<Integer>();
        int coordinate = 0;
        int largestHeatIndex = 0;
        for (int i = 0; i < stack.size(); i++) {
            coordinate = this.getCoordinateFromPos(stack.get(i));
            CoordinateStack.add(coordinate);
        }
        
        for (int i = 0; i < CoordinateStack.size(); i++) {
            if (heatmap[CoordinateStack.get(i)] > heatmap[CoordinateStack.get(largestHeatIndex)]) {
                largestHeatIndex = i;
            }
        }
        
        return this.getPosFromIndex(largestHeatIndex);
         */
    }

    public Position getPosFromShotArrList(ArrayList<Position> previousShots, ArrayList<Integer> fleet) {

        //int size = previousShots.size();
        int[] sea = generateSeaFromPositions(previousShots);
        heatmap = simpleHeatMap(sea, fleet);
        Position pos = getPositionFromHeatMap(heatmap);
        
        return pos;
    }


    public int getIntFromHeatMap(int[] heatmap) {

        int maxHeatIndex = 0;
        for (int i = 0; i < heatmap.length; i++) {
            if (heatmap[i] > heatmap[maxHeatIndex]) {
                maxHeatIndex = i;
            }
        }
        return maxHeatIndex;
    }


    private Position getPositionFromHeatMap(int[] heatmap) {
        int maxHeatIndex = 0;
        for (int i = 0; i < heatmap.length; i++) {
            if (heatmap[i] > heatmap[maxHeatIndex]) {
                maxHeatIndex = i;
            }
        }

        Position pos = getPosFromIndex(maxHeatIndex);
        return pos;
    }

    private Position getPosFromIndex(int index) {
        //int Xcoordinate = 0;
        int Xcoordinate = index % 10;
        //int Ycoordinate = 0;
        int Ycoordinate = 9 - (index / 10);

        Position pos = new Position(Xcoordinate, Ycoordinate);

        return pos;
    }

    private int[] generateSeaFromPositions(ArrayList<Position> previousShots) {
        int[] sea = new int[100];
        for (int i = 0; i < sea.length; i++) {
            sea[i] = 1;
        }

        int Xcoordinate = 0;
        int Ycoordinate = 0;
        int arrayCoordinate = 0;

        for (int i = 0; i < previousShots.size(); i++) {
            Xcoordinate = previousShots.get(i).x;
            Ycoordinate = previousShots.get(i).y;
            arrayCoordinate = (9 - Ycoordinate) * 10 + Xcoordinate;
            sea[arrayCoordinate] = -1;
        }
        return sea;
    }

    //2017-05-17 kl. 12.35 - Chr.
    // nedestående funktion burde sættes ind i generateSeaFromPositions;
    private int getCoordinateFromPos(Position pos) {
        int output = 0;
        int Xcoordinate = pos.x;
        int Ycoordinate = pos.y;
        output = (9 - Ycoordinate) * 10 + Xcoordinate;
        return output;
    }

    public int[] simpleHeatMap(int[] sea, ArrayList<Integer> fleet) {

        int[] arrFleet = new int[fleet.size()];
        for (int i = 0; i < fleet.size(); i++) {
            arrFleet[i] = fleet.get(i);
        }

        int iterations = 10000;
        int[] newsea = new int[10 * 10];

        int[] tempsea = null;
        
        for (int i = 0; i < iterations; i++) {
            tempsea = distributeShips(sea, arrFleet);
            for (int j = 0; j < tempsea.length; j++) {
                if (tempsea[j] > 1) {
                    newsea[j] += 1;
                }
            }
        }

        return newsea;

    }

    private int[] distributeShips(int[] sea, int[] fleet) {

        int[] newsea = new int[10 * 10];
        System.arraycopy(sea, 0, newsea, 0, sea.length); //newsea er nu en kopi af sea.

        ArrayList<Integer> potentialSpace = new ArrayList<Integer>();
        ArrayList<Integer> usedSpaces = new ArrayList<Integer>();

        for (int i = 0; i < fleet.length; ++i) {
            int s = fleet[i];

            int count = countShipSpace(sea, s);
            if (count <= 15) {
                ArrayList<Integer> possibleLocations = spaceForShipIndexPlusOne(newsea, s);// 2017-05-19 kl. 21.52 --Chr.
                                                                                // her bruges space for ship
                int index = rnd.nextInt(possibleLocations.size());
                
                int position = possibleLocations.get(index);// 2017-05-19 kl. 21.52 --Chr.
                                                             //th position i 1 higher than it should be 
                                                             // (to sepparate "positive" 0 and "negative" 0,
                                                             //  they are written as +1 and -1 and so on..)
                if (position < 0) {
                    position = position * (-1); 
                    for (int j = 0; j < s; j++) {
                        int indexLtoRBtoT = position + (j * 10) -1;// 2017-05-19-chr... therefore 1 is subtracted...
                        usedSpaces.add(indexLtoRBtoT);
                    }
                } else {
                    for (int j = 0; j < s; j++) {
                        int indexLtoRBtoT = position + j -1;  // 2017-05-19-chr... therefore 1 is subtracted...
                        usedSpaces.add(indexLtoRBtoT);
                    }
                }
            } else {
                boolean vertical;
                boolean goodSpace = false;

                while (!goodSpace) {
                    vertical = rnd.nextBoolean();

                    if (vertical) {

                        int x = rnd.nextInt(10);
                        int y = rnd.nextInt(10 - (s - 1));//rnd.nextInt(sizeY-(s-1));

                        for (int j = 0; j < s; j++) {
                            int indexLtoRBtoT = x + (y * 10) + (j * 10);
                            potentialSpace.add(indexLtoRBtoT);
                        }
                        boolean fieldIsOk = true;
                        for (int j = 0; j < potentialSpace.size(); j++) {
                            if (usedSpaces.contains(potentialSpace.get(j)) || newsea[potentialSpace.get(j)] < 1) {
                                fieldIsOk = false;
                            }
                        }
                        if (fieldIsOk) { // && potentialSpace.size() == s
                            usedSpaces.addAll(potentialSpace);
                            goodSpace = true;
                        }
                        potentialSpace.clear();

                    } else {
                        int x = rnd.nextInt(10 - (s - 1));//rnd.nextInt(sizeX-(s-1));
                        int y = rnd.nextInt(10);

                        for (int j = 0; j < s; j++) {
                            int indexLtoRBtoT = x + (y * 10) + j;
                            potentialSpace.add(indexLtoRBtoT);
                        }
                        boolean fieldIsOk = true;
                        for (int j = 0; j < potentialSpace.size(); j++) {
                            if (usedSpaces.contains(potentialSpace.get(j)) || newsea[potentialSpace.get(j)] < 1) {
                                fieldIsOk = false;

                            }
                        }
                        if (fieldIsOk) {  // && potentialSpace.size() == s
                            usedSpaces.addAll(potentialSpace);
                            goodSpace = true;
                        }
                        potentialSpace.clear();
                    }
                }
                //board.placeShip(pos, s, vertical);
            }
        }

        for (int i = 0; i < usedSpaces.size(); i++) {
            newsea[usedSpaces.get(i)] = 2;
        }
        return newsea;
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

    private ArrayList<Integer> getArrListCorrFromArrListPos(ArrayList<Position> stack) {
        ArrayList<Integer> CoordinateStack = new ArrayList<Integer>();
        for (Position p : stack) {
            CoordinateStack.add(this.getCoordinateFromPos(p));
        }
        return CoordinateStack;
    }

    private ArrayList<Position> getPosArrListTEST() {
        ArrayList<Position> stack = new ArrayList<Position>();
        Position p1 = new Position(4, 3);
        Position p2 = new Position(4, 4);
        Position p3 = new Position(4, 5);
        Position p4 = new Position(4, 6);
        stack.add(p1);
        stack.add(p2);
        stack.add(p3);
        stack.add(p4);
        return stack;
    }

    public int[] generateVirginHeatmap() {
        int[] sea = new int[100];//this.fixedSeaWithSips();
        for (int i = 0; i < sea.length; i++) {
            sea[i] = 1;
        }

        ArrayList<Integer> fleet = new ArrayList<Integer>();
        fleet.add(5);
        fleet.add(4);
        fleet.add(3);
        fleet.add(3);
        fleet.add(2);
        int[] heat = simpleHeatMap(sea, fleet);
        return heat;
    }

    public int[] generateVirginPlacement() {
        int[] sea = new int[100];//this.fixedSeaWithSips();
        for (int i = 0; i < sea.length; i++) {
            sea[i] = 1;
        }
        int[] fleet = {5, 4, 3, 3, 2};
        int[] newsea = this.distributeShips(sea, fleet);
        return newsea;
    }

    public int[] getHeatmap() {
        return heatmap;
    }

    public ArrayList<Integer> getVirginFleet() {
        ArrayList<Integer> fleet = new ArrayList<Integer>();
        fleet.add(5);
        fleet.add(4);
        fleet.add(3);
        fleet.add(3);
        fleet.add(2);

        return fleet;
    }

    public int[] getEmptySea() {
        int[] output = new int[100];
        for (int i = 0; i < output.length; i++) {
            output[i] = 1;
        }
        return output;
    }

    public ArrayList<Integer> spaceForShipIndexPlusOne(int[] sea, int shiplength) {
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
                numOfTimesThereIsSpace.add(i - (shiplength - 1));
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
                numOfTimesThereIsSpace.add(-(i - (10 * (shiplength - 1))));
                ver = true;
            }
            if (i >= 90 && i != 99) {
                //System.out.println("90's: "+i);
                int temp = i - 90;
                i = 1 + temp;
            }
            //extra test
            if (i / 10 == 0) {
                vertical = 1;
            }
            //System.out.println(i);
        }

        return numOfTimesThereIsSpace;
    }

    public int countShipSpace(int[] sea, int shiplength) {
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

            if (horizontal >= shiplength) {
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
                //System.out.println(i);
            } else if (sea[i] != 1) {
                vertical = 1;
            }

            if (vertical >= shiplength) {
                numOfTimesThereIsSpace++;
                ver = true;
            }
            if (i >= 90 && i != 99) {
                //System.out.println("90's: "+i);
                int temp = i - 90;
                i = 1 + temp;
            }
            //extra test
            if (i / 10 == 0) {
                vertical = 1;
            }
            //System.out.println(i);
        }

        return numOfTimesThereIsSpace;
    }

    public int getCorrFromPos(Position pos) {
        int output = 0;
        output = ((9 - pos.y) * 10) + pos.x;
        return output;
    }

    public int[] fixedSeaWithSips() {
        int[] fixedSea
                = {1, 1, 1, 1, 3, 3, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 5, 1, 1, 1,
                    7, 7, 7, 1, 1, 1, 5, 1, 1, 1,
                    1, 1, 1, 1, 1, 1, 5, 1, 1, 11,
                    1, 1, 9, 1, 1, 1, 1, 1, 1, 11,
                    1, 1, 9, 1, 1, 1, 1, 1, 1, 11,
                    1, 1, 9, 1, 1, 1, 1, 1, 1, 11,
                    1, 1, 9, 1, 1, 1, 1, 1, 1, 11};

        return fixedSea;
    }

    private int[] antiHeatTemplatePS() {
        int[] fixedSea
                = {-1, 1, 1, 1, -1, -1, 1, 1, 1, -1,
                    1, -1, 1, 1, -1, -1, 1, 1, -1, 1,
                    1, 1, -1, -1, 1, 1, -1, -1, 1, 1,
                    1, 1, -1, -1, 1, 1, -1, -1, 1, 1,
                    -1, -1, 1, 1, -1, -1, 1, 1, -1, -1,
                    -1, -1, 1, 1, -1, -1, 1, 1, -1, -1,
                    1, 1, -1, -1, 1, 1, -1, -1, 1, 1,
                    1, 1, -1, -1, 1, 1, -1, -1, 1, 1,
                    1, -1, 1, 1, -1, -1, 1, 1, -1, 1,
                    -1, 1, 1, 1, -1, -1, 1, 1, 1, -1};

        return fixedSea;
    }

}
