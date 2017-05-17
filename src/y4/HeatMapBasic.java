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
    public void run(){
        System.out.println("HeatMapBasic -- run");
        
    }
    
    public Position getPosFromShotArrList(ArrayList<Position> previousShots, ArrayList<Integer> fleet) {
        int[] sea = this.generateSeaFromPositions(previousShots);
        int[] heatmap = this.simpleHeatMap(sea, fleet);
        Position pos = this.getPositionFromHeatMap(heatmap);
        return pos;
    }
    
    /**
     * 
     * @param sea
     * @param fleet
     * @return single int coordinate
     */
    private int getIntFromHeatMap(int[] heatmap, ArrayList<Integer> fleet){
        
        int maxHeatIndex = 0;   
        for (int i = 0; i < heatmap.length; i++) {
            if (heatmap[i] > heatmap[maxHeatIndex]) {
                maxHeatIndex = i;
            }
        }
        return maxHeatIndex;
    }
    
    /**
     * 
     * @param sea
     * @param fleet
     * @return Position of "hottest" point in the sea.
     */
    private Position getPositionFromHeatMap(int[] heatmap){
        int maxHeatIndex = 0;   
        for (int i = 0; i < heatmap.length; i++) {
            if (heatmap[i] > heatmap[maxHeatIndex]) {
                maxHeatIndex = i;
            }
        }
        
        Position pos = this.getPosFromIndex(maxHeatIndex);
        return pos;
    }
    
    private Position getPosFromIndex(int index){
        int Xcoordinate = 0;
        Xcoordinate = index % 10; 
        int Ycoordinate = 0;
        Ycoordinate = 9-(index / 10);
            
        Position pos = new Position(Xcoordinate, Ycoordinate);
        
        return pos;
    }
    
    private int[] generateSeaFromPositions(ArrayList<Position> previousShots){
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
            arrayCoordinate = (9-Ycoordinate)*10 + Xcoordinate;
            sea[arrayCoordinate] = -1;
        }
        return sea;
    }
    
    
    private int[] simpleHeatMap(int[] sea, ArrayList<Integer> fleet) {
        
        int[] arrFleet = new int[fleet.size()];
        for (int i = 0; i < fleet.size(); i++) {
            arrFleet[i] = fleet.get(i);
        }
        
        int iterations = 100000;
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
        System.arraycopy(sea, 0, newsea, 0, sea.length);
        //newsea er nu en kopi af sea.

        ArrayList<Integer> potentialSpace = new ArrayList<Integer>();
        ArrayList<Integer> usedSpaces = new ArrayList<Integer>();

        for (int i = 0; i < fleet.length; ++i) {
            int s = fleet[i];
            boolean vertical;

            boolean goodSpace = false;

            while (goodSpace == false) {
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
                    if (fieldIsOk == true && potentialSpace.size() == s) {
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
                    if (fieldIsOk == true && potentialSpace.size() == s) {
                        usedSpaces.addAll(potentialSpace);
                        goodSpace = true;
                    }
                    potentialSpace.clear();
                }
            }

            //board.placeShip(pos, s, vertical);
        }
        for (int i = 0; i < usedSpaces.size(); i++) {
            newsea[usedSpaces.get(i)] = 2;
        }
        return newsea;
    }
    
    /*
    public int[] simpleHeatMap(int[] sea, int[] fleet) {

        int iterations = 100000;
        int[] newsea = new int[10 * 10];

        int[] tempsea = null;
        for (int i = 0; i < iterations; i++) {
            tempsea = distributeShips(sea, fleet);
            for (int j = 0; j < tempsea.length; j++) {
                if (tempsea[j] > 1) {
                    newsea[j] += 1;
                }
            }
        }

        return newsea;

    }
    */
         

    private void printHeatmap(int divisor, int[] sea) {
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

    private void printSea(int[] sea) {
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
    
    private int[] fixedSeaWithSips() {
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
    
}