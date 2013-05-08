/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.broker;

import sim.auction.Item;
import sim.courierworld.NodePackage;

/**
 * This broker ensures that the packages are delivered by using a Dutch auction
 * @author drew
 */
public class BrokerWithAuction extends Broker{

    @Override
    public double getQuote(NodePackage myPackages) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getEstValue(Item<NodePackage> i) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
