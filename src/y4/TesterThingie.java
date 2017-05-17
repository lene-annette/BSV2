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
        
        ArrayList<Position> p = pf.fillPositionArray();
        
        System.out.println(p.get(87));
        
    }
            
    
}
