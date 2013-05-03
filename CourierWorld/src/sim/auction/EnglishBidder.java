/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.auction;

/**
 *
 * @author drew
 */
public interface EnglishBidder extends Bidder{
    public int getEnglishBid(Item item, int val, EnglishBidder winner);
}
