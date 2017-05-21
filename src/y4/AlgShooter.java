/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y4;

import battleship.interfaces.BattleshipsPlayer;
import battleship.interfaces.Board;
import battleship.interfaces.Fleet;
import battleship.interfaces.Position;
import battleship.interfaces.Ship;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author lene_
 */
public class AlgShooter implements BattleshipsPlayer {

    int globalEnemyShotCounter = 0;
    int[] ourShipPlacementRound = null;//bliver strengt taget ikke brugt til noget, men
    //måske kan vi få brug for den.
    int[] enemyShipMatch = null;
    int[] enemyShipRound = null;
    int[] enemyShotMatch = null;
    int[] enemyShotRound = null;

    private final static Random rnd = new Random();
    private final static PositionFiller pf = new PositionFiller();
    private int sizeX;
    private int sizeY;
    private int sunkenShipSize;
    private int hitCount;
    private double AlgShooterAverage;
    private double EnemyAverage;
    private double rounds;
    private Board myBoard;
    private boolean hunt;
    private boolean target;
    private boolean shotHit;
    private boolean isSunk;
    private Position shot;
    private int[] heatMap;
    private int[] stat;
    private ArrayList<Position> stack;
    private ArrayList<Position> hitList;
    private ArrayList<Position> avblShots;
    private ArrayList<Position> shotsFired;
    private ArrayList<Integer> fleetBeforeShot;
    private ArrayList<Integer> fleetAfterShot;
    private HeatMapBasic heatMapper;

    //attributes used for target methods
    private ArrayList<Position> endFields;
    private Position neighbor;
    private boolean vertHit;
    private boolean neighborMatch;
//    private int backTrack;    // blev brugt af tidligere find nabo algoritme
    private ArrayList<Position> hitListTemp;

    public AlgShooter() {
    }

    @Override
    public void startMatch(int rounds, Fleet ships, int sizeX, int sizeY) {
        AlgShooterAverage = 0;
        EnemyAverage = 0;
        this.rounds = (double) rounds;
        stat = new int[101];
        heatMapper = new HeatMapBasic();

        enemyShipMatch = heatMapper.getEmptySea();
        enemyShotMatch = heatMapper.getEmptySea();
    }

    @Override
    public void startRound(int round) {
        hunt = true;
        target = false;
        isSunk = false;
        hitCount = 0;
        stack = new ArrayList<Position>();
        endFields = new ArrayList<Position>();
        hitList = new ArrayList<Position>();
        shotsFired = new ArrayList<Position>();
        shot = new Position(0, 0);
        neighbor = new Position(0, 0);
        shotHit = false;
        avblShots = pf.fillPositionArray();
        fleetBeforeShot = new ArrayList<Integer>();
        fleetAfterShot = new ArrayList<Integer>();

        globalEnemyShotCounter = 0;
        ourShipPlacementRound = heatMapper.getEmptySea();
        enemyShipRound = heatMapper.getEmptySea();
        enemyShotRound = heatMapper.getEmptySea();

//        backTrack = 0;    // blev brugt af tidligere find nabo algoritme
        hitListTemp = new ArrayList<Position>();

    }

    @Override
    public void placeShips(Fleet fleet, Board board) {
        myBoard = board;
        sizeX = board.sizeX();
        sizeY = board.sizeY();

        ArrayList<Integer> potentialSpace = new ArrayList<Integer>();
        ArrayList<Integer> usedSpaces = new ArrayList<Integer>();

        for (int i = 0; i < fleet.getNumberOfShips(); ++i) {
            Ship s = fleet.getShip(i);
            boolean vertical = rnd.nextBoolean();
            boolean goodSpace = false;
            Position pos = new Position(0, 0);

            while (goodSpace == false) {
                if (vertical) {

                    int x = rnd.nextInt(sizeX);
                    int y = rnd.nextInt(sizeY - (s.size() - 1));
                    pos = new Position(x, y);

                    for (int j = 0; j < s.size(); j++) {
                        int indexLtoRBtoT = x + (y * 10) + (j * 10);
                        potentialSpace.add(indexLtoRBtoT);
                    }
                    boolean fieldIsOk = true;
                    for (int j = 0; j < potentialSpace.size(); j++) {
                        if (usedSpaces.contains(potentialSpace.get(j))) {
                            fieldIsOk = false;
                        }
                    }
                    if (fieldIsOk == true && potentialSpace.size() == s.size()) {
                        usedSpaces.addAll(potentialSpace);
                        goodSpace = true;
                    }
                    potentialSpace.clear();

                } else {
                    int x = rnd.nextInt(sizeX - (s.size() - 1));
                    int y = rnd.nextInt(sizeY);
                    pos = new Position(x, y);

                    for (int j = 0; j < s.size(); j++) {
                        int indexLtoRBtoT = x + (y * 10) + j;
                        potentialSpace.add(indexLtoRBtoT);
                    }
                    boolean fieldIsOk = true;
                    for (int j = 0; j < potentialSpace.size(); j++) {
                        if (usedSpaces.contains(potentialSpace.get(j))) {
                            fieldIsOk = false;
                        }
                    }
                    if (fieldIsOk == true && potentialSpace.size() == s.size()) {
                        usedSpaces.addAll(potentialSpace);
                        goodSpace = true;
                    }
                    potentialSpace.clear();
                }
            }

            //2017-05-18 -- chr -- kodeforslag:
            ArrayList<Integer> ourShipCoor = shipCoorFromPos(pos, s.size(), vertical);
            for (int j = 0; j < ourShipCoor.size(); j++) {
                this.ourShipPlacementRound[ourShipCoor.get(j)] = 2;
            }
            board.placeShip(pos, s, vertical);
        }

    }

    public ArrayList<Integer> shipCoorFromPos(Position pos, int shiplength, boolean vertical) {
        ArrayList<Integer> shipCoor = new ArrayList<Integer>();
        int startCorr = heatMapper.getCorrFromPos(pos);
        int nextCoor = 0;
        shipCoor.add(startCorr);
        for (int i = 1; i < shiplength; i++) {
            if (vertical) {
                nextCoor = startCorr - (i * 10);
                shipCoor.add(nextCoor);
            } else {
                nextCoor = startCorr + i;
                shipCoor.add(nextCoor);
            }
        }

        return shipCoor;
    }

    @Override
    public void incoming(Position pos) {
        globalEnemyShotCounter++;
        int enemyShot = heatMapper.getCorrFromPos(pos);
        for (int i = 0; i < this.enemyShotRound.length; i++) {
            enemyShotRound[enemyShot] = 101 - globalEnemyShotCounter;//dette er for at se skudrækkefølgen
        }

    }

    @Override
    public Position getFireCoordinates(Fleet enemyShips) {
        //System.out.println("getFireCoordinates: activated");
        fleetBeforeShot = fleetConverter(enemyShips);
        long startTime = System.currentTimeMillis();
        
        if (hunt) {
            //This is hunting mode.
            //Shots are chosen from making a heatmap and
            //choosing the most probable place for a ship to be located.
            shot = heatMapper.getPosFromShotArrList(shotsFired, fleetAfterShot);

            //The heatmap is stored for later use in target mode.
            //Since no new heat map is generated when in target mode.
            heatMap = heatMapper.getHeatmap();

        } else if (target && shotHit) {
            //This is target mode when last shot was a hit.

            //All positions around the hit is added to the stack
            addToStack(shot);

            if (hitCount == 1) {
                //If this is the first hit shot will be made from stack
                shot = shootFromStack();
            } else {
                //With more than one hit occuring it is determined 
                //if last hit has any neighbor hits
                checkForMatch();
                if (endFields.isEmpty()) {
                    //With no end fields present shot will be made from stack
                    shot = shootFromStack();
                } else {
                    //With end fields present those will be shot first
                    shot = targetShooter();
                }
            }

        } else {
            //Still in target mode - but last shot was not a hit
            if (endFields.isEmpty()) {
                //If no end points present shot will be made from stack
                shot = shootFromStack();
            } else {
                //The first end point was an empty sea spot
                //and the other end point will be shot
                shot = targetShooter();
            }

        }

        //The shot is transferred from available shots array
        //to the shots fired array.
        avblShots.remove(shot);
        shotsFired.add(shot);

        shooterDebugOutput(startTime);

        return shot;
    }

    @Override
    public void hitFeedBack(boolean hit, Fleet enemyShips) {

        //2017-05-18 -kl. 17.02- -chr- kodeforslag:
        //hvis det er et hit, skal feltet tilføjes enemyShipRound:
        // det er forudsat (i endRound) at det der tilføjes til enemyShipRound er en
        // negativ integer (eg -2).
        //this.enemyShipRound[heatMapper.getCorrFromPos(pos)] = -this.enemyShipRound[heatMapper.getCorrFromPos(pos)];
        fleetAfterShot = fleetConverter(enemyShips);
        isSunk = !(fleetAfterShot.size() == fleetBeforeShot.size());
        shotHit = hit;

        if (hit) {
            hitCount++;
//            backTrack++;   // blev brugt af tidligere find nabo algoritme
            hitList.add(shot);
            hitListTemp.add(shot);

            if (!isSunk) {
                target = true;
                hunt = false;
            } else {
                sunkenShipSize = findSunkenShipSize(fleetBeforeShot, fleetAfterShot);
                hitCount = hitCount - sunkenShipSize;
                if (hitCount > 0) {
                    target = true;
                    hunt = false;
                } else {
                    target = false;
                    hunt = true;
                    stack.clear();
                    endFields.clear();
                    hitListTemp.clear();
//                    backTrack = 0;
                }
            }

        } else if (!stack.isEmpty() || !endFields.isEmpty()) {
            target = true;
            hunt = false;
        } else {
            target = false;
            hunt = true;
        }

    }

    //Uses the heatmap to determine which of the end fields should be
    //the one shot at firat and the removes the end field for the array
    //of possible endfields.
    public Position targetShooter() {
        Position pos = heatMapper.getPosFromStack(heatMap, endFields);
        int index = endFields.indexOf(pos);
        endFields.remove(index);
        return pos;
    }

    // This method is use when 2 or more hits has been found.
    // It checks if a new hit is neighbor to a previous hit.
    // If so it also determines if it as a vertical or horizontal neighbor.
    public boolean checkForMatch() {

        neighborMatch = false;
        vertHit = false;

        int tempSize = hitListTemp.size();

        for (int i = 0; i < tempSize - 1; i++) {

            if (hitListTemp.get(i).x == shot.x) {
                neighborMatch = true;
                vertHit = true;
                neighbor = hitListTemp.get(i);
                findEndFields();
            }
            if (hitListTemp.get(i).y == shot.y) {
                neighborMatch = true;
                vertHit = false;
                neighbor = hitListTemp.get(i);
                findEndFields();
            }

        }

//                      // Tidligere find nabo algoritme      
//
//        int ticker = 2;
//        int hits = hitList.size();
//
//        //Backtracks all previous hits in hitList and
//        //checks if the latest hit is neighbor to one of them.
//        while (!neighborMatch && ticker <= backTrack) {
//            if (hitList.get(hits - ticker).x == shot.x) {
//                neighborMatch = true;
//                vertHit = true;
//            } else if (hitList.get(hits - ticker).y == shot.y) {
//                neighborMatch = true;
//            } else {
//                ticker++;
//            }
//        }
//        //if a neighbor is found the end points will be determined
//        if (neighborMatch) {
//            neighbor = hitList.get(hits - ticker);
//            findEndFields();
//        }
//        
        return neighborMatch;
    }

    // This methods calculates the positions next to neighbor hits.
    public void findEndFields() {
        Position right, left, up, down;

        if (vertHit) {
            if (shot.y < neighbor.y) {
                down = new Position(shot.x, shot.y - 1);
                up = new Position(neighbor.x, neighbor.y + 1);
            } else {
                down = new Position(neighbor.x, neighbor.y - 1);
                up = new Position(shot.x, shot.y + 1);
            }
            addEndFieldsIfValid(down);
            addEndFieldsIfValid(up);
        } else {
            if (shot.x < neighbor.x) {
                right = new Position(shot.x - 1, shot.y);
                left = new Position(neighbor.x + 1, neighbor.y);
            } else {
                right = new Position(neighbor.x - 1, neighbor.y);
                left = new Position(shot.x + 1, shot.y);
            }
            addEndFieldsIfValid(right);
            addEndFieldsIfValid(left);

        }

    }

    // Add end positions next to neighbor hits if they are valid.
    // If valid they must already exits in stack
    // Valid end positions will be removed from stack array
    // and transferred to endFields array
    public void addEndFieldsIfValid(Position pos) {
        if (stack.contains(pos)) {
            endFields.add(pos);
            stack.remove(pos);
        }
    }

    @Override
    public void endRound(int round, int points, int enemyPoints) {

        //2017-05-18 -kl.17.02 -chr- kodeforslag:
        for (int i = 0; i < this.enemyShipRound.length; i++) {
            if (enemyShipRound[i] < 1) {
                this.enemyShipMatch[i]++;
            }
        }
        for (int i = 0; i < this.enemyShotRound.length; i++) {
            this.enemyShotMatch[i] += enemyShotRound[i];
        }

        AlgShooterAverage += 100.0 - points;
        EnemyAverage += 100.0 - enemyPoints;
        stat[100 - points]++;

    }

    @Override
    public void endMatch(int won, int lost, int draw) {

        AlgShooterAverage = AlgShooterAverage / rounds;
        EnemyAverage = EnemyAverage / rounds;

        System.out.println("");
        System.out.println("AlgShooter : " + AlgShooterAverage);
        System.out.println("Enemy : " + EnemyAverage);
        System.out.println("Win% : " + (100.0 * won / rounds) + "%");
        System.out.println("");
//        for (int i = 1; i < 101; i++) {
//            System.out.println(i + " : " + stat[i]);
//        }

    }

    public void addToStack(Position pos) {
        Position n, s, e, w;
        n = new Position(pos.x, (pos.y + 1));
        s = new Position(pos.x, (pos.y - 1));
        e = new Position((pos.x + 1), pos.y);
        w = new Position((pos.x - 1), pos.y);

        addPositionIfValid(n);
        addPositionIfValid(s);
        addPositionIfValid(e);
        addPositionIfValid(w);

    }

    public void addPositionIfValid(Position pos) {
        if (pos.x >= 0 && pos.y >= 0 && pos.x < 10 && pos.y < 10 && !stack.contains(pos) && avblShots.contains(pos)) {
            stack.add(pos);
        }
    }

    public Position shootFromStack() {
        Position pos = heatMapper.getPosFromStack(heatMap, stack);
        int index = stack.indexOf(pos);
        stack.remove(index);
        return pos;
    }

//    public Position getFromGrid(int index) {
//        Position result;
//        Position p = avblShots.get(index);
//
//        if (p.x % 2 == 0 && p.y % 2 != 0) {
//            result = p;
//        } else if (p.x % 2 != 0 && p.y % 2 == 0) {
//            result = p;
//        } else if (index != avblShots.size() - 1) {
//            result = avblShots.get(index + 1);
//        } else {
//            result = avblShots.get(index - 1);
//        }
//
//        return result;
//    }
    public ArrayList<Integer> fleetConverter(Fleet fleet) {
        int shipCount = fleet.getNumberOfShips();
        ArrayList<Integer> fleetArray = new ArrayList<Integer>();
        for (int i = 0; i < shipCount; i++) {
            fleetArray.add(fleet.getShip((shipCount - 1) - i).size());
        }
        return fleetArray;
    }

    public int findSunkenShipSize(ArrayList<Integer> beforeShot, ArrayList<Integer> afterShot) {
        for (Integer sizeShip : afterShot) {
            beforeShot.remove(sizeShip);
        }
        return (int) (beforeShot.get(0));

    }

    public void shooterDebugOutput(long startTime) {
        
        long finishTime = System.currentTimeMillis();
        System.out.println("Skudberegningen tog: "+(finishTime-startTime)+ " ms");
        System.out.println("Shot fired at : " + shot.toString());
        System.out.print("Hunting : " + hunt + " - Target : " + target);
        System.out.println(" - Hitcounter : " + hitCount + " - HitListTemp (size) : " + hitListTemp.size());
        System.out.println("Last shot a hit : " + shotHit + " - Ship sunk last round : " + isSunk);

        if (!stack.isEmpty()) {
            System.out.print("Stack remaining: ");
            for (int i = 0; i < stack.size(); i++) {
                System.out.print(stack.get(i).toString() + " ");
            }
            System.out.println("");
        }

        if (!endFields.isEmpty()) {
            System.out.print("End points remaining : ");
            for (int i = 0; i < endFields.size(); i++) {
                System.out.print(endFields.get(i).toString() + " ");
            }
            System.out.println("");
        }

        int numberOfShotsAvbl = avblShots.size();
        int numberOfShotsFired = shotsFired.size();
        System.out.println("avblShots (size) : " + numberOfShotsAvbl + " - shotsFired (size) : " + numberOfShotsFired);
        System.out.println("Is " + shot.toString() + " in avblShots : " + avblShots.contains(shot));
        System.out.println("Last position in shotsFired : " + shotsFired.get(numberOfShotsFired - 1));
        System.out.println();

    }

}
