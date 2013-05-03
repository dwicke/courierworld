/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courier;

import java.util.HashMap;
import sim.auction.DutchBidder;
import sim.auction.EnglishBidder;
import sim.auction.Item;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.network.Edge;

/**
 *
 * @author drew
 */
public class Courier implements DutchBidder, EnglishBidder, Steppable{

    
    private int profit;
    // map the edges to costs
    private HashMap<Edge, Integer> myNetwork;
    
    public Courier()
    {
        myNetwork = new HashMap<>();
    }
    
    
    
    
    @Override
    public boolean getDutchBid(int val, Item item) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doTransaction(int charge, Item item) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getEnglishBid(Item item, int val, EnglishBidder winner) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void step(SimState state) {
        
        // use 1 since after packagegenerator
        state.schedule.scheduleOnce(this, 1);
    }
    
}
