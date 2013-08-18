/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld.model.impl;

import sim.courierworld.SimpleCourierWorld;
import sim.courierworld.model.IBroker;

/**
 *
 * @author drew
 */
public class Broker implements IBroker{

    private int id;
    private int deliveryRate, quote, profit;
    
    @Override
    public void setQuote(SimpleCourierWorld state) {
        System.out.println("Setting the quote for " + toString() + " to be: " + getQuote());
    }

    @Override
    public void setDeliveryRate(SimpleCourierWorld state) {
        System.out.println("Setting the Delivery Rate for " + toString() + " to be: " + getDeliveryRate());
    }
    
    @Override
    public int getDeliveryRate() {
        return deliveryRate;
    }
    @Override
    public int getQuote() {
        return quote;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String toString() {
        return "Broker_" + id;
    }

    /**
     * @param deliveryRate the deliveryRate to set
     */
    @Override
    public void setDeliveryRate(int deliveryRate) {
        this.deliveryRate = deliveryRate;
    }

    /**
     * @param quote the quote to set
     */
    @Override
    public void setQuote(int quote) {
        this.quote = quote;
    }
    
    
    
}
