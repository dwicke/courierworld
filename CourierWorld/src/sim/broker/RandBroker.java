/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.broker;

import java.util.List;
import sim.auction.Item;
import sim.courier.Courier;
import sim.courierworld.Warehouse;

/**
 *
 * @author drew
 */
public class RandBroker extends Broker {

    
    /**
     * How much i charge to take the packages and ensure they get delivered
     * my estimate of their worth and plus a fee.
     * @param myPackages
     * @return 
     */
    @Override
    public double getQuote(Warehouse myPackages) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param courierList 
     */
    @Override
    public void performAuctions(List<Courier> courierList) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * used so know how to start the auction.
     * @param i
     * @return 
     */
    @Override
    public double getEstValue(Item<Warehouse> i) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
