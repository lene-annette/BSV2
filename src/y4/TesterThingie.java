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
public class TesterThingie { 
    public static void main(String[] args) {
        
        PositionFiller pf = new PositionFiller();
        
        ArrayList<Position> grid = pf.makeGrid();
        int size = grid.size();
        
        Position p = grid.get(21);
        
        for(int i = 0; i < grid.size(); i++){
            System.out.println("index: " + i + " Position: " + grid.get(i));
        }
        
//        ArrayList<Position> fullmap = pf.fillPositionArray();
//        Position p = fullmap.get(1);
//        
//        System.out.println(p);
//        
//        EnemyReact er = new EnemyReact();
//        er.run();
        
        //HeatMapBasic hm = new HeatMapBasic();
        //hm.run();
        
        //HeatMapInverse hmi = new HeatMapInverse();
        //hmi.run();
        
    }
    
    
            
    
}


        /*
            // Lenes kode: 2017-05-17 kl. 12.57 - Chr
//        PositionFiller pf = new PositionFiller();
//        
//        ArrayList<Position> p = pf.fillPositionArray();
//        
//        System.out.println(p.get(87));

          AlgShooter as = new AlgShooter();
          
          ArrayList<Integer> before = new ArrayList<Integer>();
          ArrayList<Integer> after = new ArrayList<Integer>();
        
          before.add(2);
          before.add(3);
          before.add(3);
          before.add(4);
          before.add(5);
          
          after.add(2);
          after.add(3);
          after.add(3);
          after.add(5);
          
          int test = as.findSunkenShipSize(before, after);
          System.out.println("Sunken Ship Size : " + test);
*/