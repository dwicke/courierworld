/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.auction;

/**
 *
 * @author drew
 */
public interface Item<T> {
 
    public void setItem(T item);
    public T getItem();
}
