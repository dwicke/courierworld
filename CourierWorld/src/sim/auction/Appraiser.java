/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.auction;

/**
 * The middle man 
 * @author drew
 */
public interface Appraiser<T> {
    public double getEstValue(Item<T> i); // how much the item is worth 
}
