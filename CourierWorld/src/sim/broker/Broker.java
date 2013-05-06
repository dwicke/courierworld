/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.broker;

import java.util.List;
import sim.auction.Appraiser;
import sim.courier.Courier;
import sim.courierworld.Warehouse;

/**
 *
 * @author indranil
 */
public abstract class Broker implements Appraiser<Warehouse>{

    private double defaultRate = 0.0;
    private double profit = 0.0;
    private Warehouse myPackages = new Warehouse();
    
    public abstract double getQuote(Warehouse myPackages);

    /**
     * Returns the percentage of packages the broker
     * has lost.
     * @return 
     */
    public double getDefaultRate()
    {
        return defaultRate;
    }

    /**
     * Combines the packages in myPackages with the Broker's packages
     * @param myPackages
     * @param fee
     */
    public void addPackage(Warehouse myPackages, double fee)
    {
        this.myPackages.addAll(myPackages);
        profit += fee;
    }

    public abstract void performAuctions(List<Courier> courierList);

    public void decayPackages() {
        myPackages.decayStacks();
        
    }

    public void performAuction() {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
