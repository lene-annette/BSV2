/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y4;

import battleship.interfaces.Position;
import java.util.ArrayList;

/**
 *
 * @author lene_
 */
public class PositionFiller {
    
    public ArrayList<Position> fillPositionArray(){
        ArrayList<Position> pos = new ArrayList<Position>();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                Position p = new Position(i,j);
                pos.add(p);
            }
        }
        return pos;
    }
    
    public ArrayList<Position> makeGrid(){
        ArrayList<Position> grid = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                 if(i%2 == 0 && j%2 == 0){
                     Position p = new Position(i,j);
                     grid.add(p);
                 }
                 if(i%2 == 1 && j%2 == 1){
                     Position p = new Position(i,j);
                     grid.add(p);
                 }
            }
        }
        return grid;
    }
    
}
