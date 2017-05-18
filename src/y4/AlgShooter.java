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
    private boolean lastShotHit;
    private boolean isSunk;
    private Position lastShot;
    private int[] heatMap;
    private int[] stat;
    private ArrayList<Position> stack;
    private ArrayList<Position> avblShots;
    private ArrayList<Position> shotsFired;
    private ArrayList<Integer> fleetBeforeShot;
    private ArrayList<Integer> fleetAfterShot;
    private HeatMapBasic heatMapper;

    public AlgShooter() {
    }

    @Override
    public void startMatch(int rounds, Fleet ships, int sizeX, int sizeY) {
        AlgShooterAverage = 0;
        EnemyAverage = 0;
        this.rounds = (double) rounds;
        stat = new int[100];
        heatMapper = new HeatMapBasic();
    }

    @Override
    public void startRound(int round) {
        hunt = true;
        target = false;
        isSunk = false;
        hitCount = 0;
        stack = new ArrayList<Position>();
        shotsFired = new ArrayList<Position>();
        lastShot = new Position(0, 0);
        lastShotHit = false;
        avblShots = pf.fillPositionArray();
        fleetBeforeShot = new ArrayList<Integer>();
        fleetAfterShot = new ArrayList<Integer>();
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
            board.placeShip(pos, s, vertical);
        }

    }

    @Override
    public void incoming(Position pos) {

    }

    @Override
    public Position getFireCoordinates(Fleet enemyShips) {
        fleetBeforeShot = fleetConverter(enemyShips);

        int index;
        if (hunt) {
            
            lastShot = heatMapper.getPosFromShotArrList(shotsFired, fleetAfterShot);
            heatMap = heatMapper.getHeatmap();
//            index = rnd.nextInt(avblShots.size());
//            lastShot = getFromGrid(index);
            
            avblShots.remove(lastShot);
            shotsFired.add(lastShot);
        } else if (target && lastShotHit) {
            addToStack(lastShot);
            lastShot = shootTopOfStack();
            avblShots.remove(lastShot);
            shotsFired.add(lastShot);
        } else {
            lastShot = shootTopOfStack();
            avblShots.remove(lastShot);
            shotsFired.add(lastShot);
        }

        return lastShot;
    }

    @Override
    public void hitFeedBack(boolean hit, Fleet enemyShips) {

        fleetAfterShot = fleetConverter(enemyShips);
        isSunk = !(fleetAfterShot.size() == fleetBeforeShot.size());
        lastShotHit = hit;

        if (hit) {
            hitCount++;
            if (!isSunk) {
                target = true;
                hunt = false;
            } else {
                sunkenShipSize = findSunkenShipSize(fleetBeforeShot, fleetAfterShot);
                hitCount = hitCount-sunkenShipSize;
                if (hitCount > 0) {
                    target = true;
                    hunt = false;
                }
                else {
                    target = false;
                    hunt = true;
                    stack.clear();
                }
            }

        } else if (!hit && !stack.isEmpty()) {
            target = true;
            hunt = false;
        } else {
            target = false;
            hunt = true;
        }

    }

    @Override
    public void endRound(int round, int points, int enemyPoints) {
        AlgShooterAverage += 100.0-points;
        EnemyAverage += 100.0-enemyPoints;
        stat[100-points]++;
        
    }

    @Override
    public void endMatch(int won, int lost, int draw) {
        
        AlgShooterAverage = AlgShooterAverage/rounds;
        EnemyAverage = EnemyAverage/rounds;
        
        System.out.println("");
        System.out.println("AlgShooter : " + AlgShooterAverage);
        System.out.println("Enemy : " + EnemyAverage);
        System.out.println("Win% : "+(100.0*won/rounds)+"%");
        System.out.println("");
        for (int i = 0; i < 100; i++) { System.out.println(i+" : "+stat[i]); }
        
        
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
        
//        if (n.x >= 0 && n.y >= 0 && n.x < 10 && n.y < 10 && !stack.contains(n) && avblShots.contains(n)) {
//            stack.add(n);
//        }
//        if (s.x >= 0 && s.y >= 0 && s.x < 10 && s.y < 10 && !stack.contains(s) && avblShots.contains(s)) {
//            stack.add(s);
//        }
//        if (e.x >= 0 && e.y >= 0 && e.x < 10 && e.y < 10 && !stack.contains(e) && avblShots.contains(e)) {
//            stack.add(e);
//        }
//        if (w.x >= 0 && w.y >= 0 && w.x < 10 && w.y < 10 && !stack.contains(w) && avblShots.contains(w)) {
//            stack.add(w);
//        }
    }
    
    public void addPositionIfValid(Position pos) {
        if (pos.x >= 0 && pos.y >= 0 && pos.x < 10 && pos.y < 10 && !stack.contains(pos) && avblShots.contains(pos)) {
            stack.add(pos);
        }
    }

    public Position shootTopOfStack() {
        Position pos = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
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

}
