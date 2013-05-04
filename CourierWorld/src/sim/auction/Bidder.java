/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.auction;

/**
 *
 * @author drew
 */
interface Bidder {
    /**
     * The bidder takes charge of the provided item
     * and removes the amount charge.
     * @param charge
     * @param item 
     */
    public void doTransaction(double charge, Item item);
}
