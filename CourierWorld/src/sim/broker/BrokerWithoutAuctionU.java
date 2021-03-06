/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.broker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sim.courier.Courier;
import sim.courierworld.CourierWorld;
import sim.courierworld.Hub;
import sim.courierworld.Node;
import sim.courierworld.Warehouse;

/**
 * This broker doesn't care about its client and getting the package delivered
 * it wants to make as much from the auction as possible. So, it uses an English
 * auction.
 *
 * @author drew
 */
public class BrokerWithoutAuctionU extends Broker {
    public double baseRate = 1.0;//selling without profit
    public double  minBaseRate = 0.5;
    public double  maxBaseRate = 10.0;
    public double  profitFactor = 1.5;

    public BrokerWithoutAuctionU(Hub myHub) {
        super(myHub);
    }
    
    @Override
    public double getQuote(Warehouse myPackages)
    {
        quote = serviceRate*myPackages.getTotalNumPacks();
        //System.err.println("-q" + quote);
        return quote;
    }

    @Override
    public String toString() {
        return "WithoutAuction"; //To change body of generated methods, choose Tools | Templates.
    }

    

    @Override
    public void performAuctions(List<Courier> courierList, CourierWorld world, Node hubNode) {
        //get bid rate for current step
        upDateBidRate();
        //do this for each package <dest, priority>
        Iterator<Map.Entry<Warehouse.Key, Integer>> iter = getMyPackages().getIterator();
        HashMap<Courier, Warehouse> bids = new HashMap<>();

        while (iter.hasNext()) {
            Map.Entry<Warehouse.Key, Integer> stacks = iter.next();
            //all couriers have bid
            Collections.shuffle(courierList); // first come first serve
            for (Courier c : courierList) {
                bids.put(c, new Warehouse());
                // bid is the number of packs that the courier is willing to take
                int bid = c.getSomeStacks(stacks, bidRate, hubNode, world); 
                if (bid > 0) {
                    bids.get(c).updateStack(stacks.getKey(), bid);
                    stacks.setValue(stacks.getValue() - bid);
                }
                
                if(stacks.getValue() == 0)
                {
                    break;
                }
            }

        }
        //decide on how to allocate 
        for (Map.Entry<Courier, Warehouse> en : bids.entrySet())
        {
            en.getKey().myPackages.addAll(en.getValue());
            succPakcages.addAll(en.getValue());

            profit -= (double) (bidRate * en.getValue().getTotalNumPacks());
            en.getKey().profit += (double) (bidRate * en.getValue().getTotalNumPacks());
            
        }
        //System.err.println("-p " + profit);
        //System.err.println("-d" +  defaultRate);

    }

    private void upDateBidRate()
    {
       double compBidRate = 0;
       double compServRate = 0;
       for(Broker b : myHub.brokers){
           if(!b.equals(this)){
               compBidRate += b.bidRate;
               compServRate += b.serviceRate;
           }
       }
       
       
       bidRate =  compBidRate / (myHub.brokers.size()-1) + 0.1 * Math.random() ;
       serviceRate = compServRate / (myHub.brokers.size()-1) - 0.1* Math.random();
     }

 

}
