/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.broker;

import sim.auction.Appraiser;
import sim.auction.Item;
import sim.courierworld.Node;
import sim.courierworld.NodePackage;

/**
 * This broker doesn't care about its client and getting the package delivered
 * it wants to make as much from the auction as possible. So, it uses an English
 * auction.
 * @author drew
 */
public class MaxProfitBroker extends Broker {

    @Override
    public double getEstValue(Item i) {
        //To change body of generated methods, choose Tools | Templates.
        // need to factor in the average life of the packs
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getQuote(NodePackage myPackages) {
        return 0;
        
    }

    
    
}
