/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y4;

/**
 *
 * @author Christian
 */
public class HeatMapInverse {
    HeatMapBasic hm = new HeatMapBasic();
    
    void run() {
        System.out.println("HeatMapInverse -- run");
        
        int[] heat = hm.generateVirginHeatmap();
        hm.printHeatmap(1000, heat);
        System.out.println("");
        
        int[] sea = hm.generateVirginPlacement();
        hm.printSea(sea);
        
    }
    
    
    
}
