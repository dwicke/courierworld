/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.auction;

import ec.util.MersenneTwisterFast;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author drew
 */
public class DutchAuction {
    private List<Item> items;
    private List<DutchBidder> bidders;
    
    public DutchAuction()
    {
    }
    
    public void setup(List<Item> goods, List<DutchBidder> theBidders)
    {
        items = goods;
        bidders = theBidders;
    }
    
    public List<Item> getRemainingItems()
    {
        return items;
    }
    
    /**
     * performs the auction.  Returns the profits from the auction.
     * @return 
     */
    public int doAuction(Appraiser apr, MersenneTwisterFast rand)
    {
        int decrementVal = 1;
        int profit = 0;
        ListIterator<Item> it = items.listIterator();
        while(it.hasNext())
        {
            Item item = it.next();
            int val = apr.getEstValue(item);
            boolean hasBid = true;
            List<DutchBidder> poswinners = new ArrayList<>();
            
            // loop over the bidders and collect the bidders
            do {
            
                for (DutchBidder bidder : bidders)
                {
                    if (bidder.getDutchBid(val, item))
                    {
                        poswinners.add(bidder);
                    }
                }
                
                // decrement the val by the decrementVal
                val -= decrementVal;
            } while(poswinners.isEmpty());
            // if there are break the loop
            
            
            // pick from the list of bidders randomly to say who won
            DutchBidder winner = poswinners.get(rand.nextInt(poswinners.size()));
            winner.doTransaction(val + decrementVal, item);
            
        }
        
        
        return profit;
    }
}
