/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.broker;

import sim.auction.Appraiser;
import sim.auction.Item;
import sim.courierworld.Node;

/**
 * This broker doesn't care about its client and getting the package delivered
 * it wants to make as much from the auction as possible. So, it uses an English
 * auction.
 * @author drew
 */
public class MaxProfitBroker implements Appraiser<Package> {

    
    
    
    @Override
    public int getEstValue(Item i) {
        //To change body of generated methods, choose Tools | Templates.
        // need to factor in the average life of the packs
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
