/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.broker;

import java.util.List;
import sim.auction.Appraiser;
import sim.courier.Courier;
import sim.courierworld.NodePackage;

/**
 *
 * @author indranil
 */
public abstract class Broker implements Appraiser<NodePackage>{

    private double defaultRate = 0.0;
    private double profit = 0.0;
    private NodePackage myPackages = new NodePackage();
    
    public abstract double getQuote(NodePackage myPackages);

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
    public void addPackage(NodePackage myPackages, double fee)
    {
        this.myPackages.addAll(myPackages);
        profit += fee;
    }

    public abstract void performAuctions(List<Courier> courierList);

    public void decayPackages() {
        myPackages.decay();
        
    }
    
}
