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
import sim.auction.Item;
import sim.courier.Courier;
import sim.courierworld.NodePackage;
import sim.courierworld.Warehouse;

/**
 * This broker doesn't care about its client and getting the package delivered
 * it wants to make as much from the auction as possible. So, it uses an English
 * auction.
 *
 * @author drew
 */
public class BrokerWithoutAuction extends Broker {

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

    @Override
    public void performAuctions(List<Courier> courierList) {
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
                int bid = c.getSomeStacks(stacks, bidRate); 
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
        
        for (Courier c : courierList)
        {
            c.myPackages.addAll(bids.get(c));
            
        }

    }
}
