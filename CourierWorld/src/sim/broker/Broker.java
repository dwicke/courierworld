/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.broker;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
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
    private Warehouse lostPackages = new Warehouse();
    private Warehouse succPakcages = new Warehouse();
    
    
    // the quote for all of the packages in the warehouse
    public abstract double getQuote(Warehouse myPackages);

    /**
     * Returns the percentage of packages the broker
     * has lost.
     * @return 
     */
    public double getDefaultRate()
    {
        Iterator<Entry<Warehouse.Key,Integer> > iter = myPackages.getIterator();
        double sum = 0.0;
        int count = 0;
        while (iter.hasNext())
        {
            Warehouse.Key k = iter.next().getKey();
            int numSucPacks = succPakcages.getNumPacks(k);
            int numLostPacks = lostPackages.getNumPacks(k);
            // do the sum only if there are stats about them
            if(numSucPacks != -1 && numLostPacks != -1)
            {
                sum += ((double) (numLostPacks)) / ((double) (numLostPacks + numSucPacks));
                count++;
            }
        }
        // don't want / by zero
        if (count == 0)
        {
            return 0.0;
        }
        defaultRate = sum / (double) count;
        
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

    
}
