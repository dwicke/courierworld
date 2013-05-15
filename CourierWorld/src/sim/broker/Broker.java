/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.broker;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import sim.courier.Courier;
import sim.courierworld.CourierWorld;
import sim.courierworld.Node;
import sim.courierworld.Warehouse;

/**
 *
 * @author indranil
 */
public abstract class Broker{

    public double defaultRate = 0.0;
    public double profit = 0.0;
    double bidRate = 0.1;
    public  double serviceRate = 0.5;
    public double quote = 0;
    public Warehouse myPackages = new Warehouse();
    public Warehouse lostPackages = new Warehouse();
    public Warehouse succPakcages = new Warehouse();

    public double getProfit() {
        return profit;
    }

    public double getBidRate() {
        return bidRate;
    }

    public double getServiceRate() {
        return serviceRate;
    }
    
    
    public double getQuote()
    {
        return quote;
    }
    // the quote for all of the packages in the warehouse
    public abstract double getQuote(Warehouse myPackages);

    public Warehouse getMyPackages() {
        return myPackages;
    }
    
    
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

    public abstract void performAuctions(List<Courier> courierList,  CourierWorld world, Node hubNode);

    public void decayPackages() {
        myPackages.decayStacks(lostPackages);
        
        
    }

    public abstract void updateServiceRate();
    
    

    
}
