/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.auction;

import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author drew
 */
public class EnglishAuction {
    
    private List<Item> items;
    private List<EnglishBidder> bidders;
    
    public EnglishAuction()
    {
    }
    
    public void setup(List<Item> goods, List<EnglishBidder> theBidders)
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
    public int doAuction(Appraiser apr)
    {
        int bidIncrement = 1;
        int profits = 0;
        ListIterator<Item> it = items.listIterator();
        while(it.hasNext())
        {
            Item item = it.next();
            int val = apr.getEstValue(item);
            boolean hasBid = true;
            EnglishBidder winner = null;
            while (hasBid == true){
                hasBid = false;
                for (EnglishBidder bidder : bidders)
                {
                    // bidder needs to know if winning
                    int bid = bidder.getEnglishBid(item, val, winner);
                    if (bid >= val)
                    {
                        val = bid + bidIncrement;
                        hasBid = true;
                        winner = bidder;
                    }
                }
            }
            // if there is a winner
            if (winner != null)
            {
                // must subtract bidIncrement because val was what I was asking for if
                // going to take over the winner
                winner.doTransaction(val - bidIncrement, item); // tell winner how much they paid and what they got
                profits += val - bidIncrement;// increment bid
                it.remove();// remove from list of items to be auctioned
            }
            
        }
        return profits;
    }
    
    
}
