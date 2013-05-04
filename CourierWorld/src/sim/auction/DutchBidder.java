/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.auction;

/**
 *
 * @author drew
 */
public interface DutchBidder extends Bidder{
    /**
     * return true if bidding false if not
     * @param val - how much would have to pay
     * @param item - the item being bid on
     * @return 
     */
    public boolean getDutchBid(double val, Item item);
}
