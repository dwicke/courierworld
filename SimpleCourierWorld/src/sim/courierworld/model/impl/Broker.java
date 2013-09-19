
package sim.courierworld.model.impl;

import sim.courierworld.SimpleCourierWorld;
import sim.courierworld.model.IBroker;

/**
 *
 * @author drew
 */
public class Broker implements IBroker{

    private int id;
    private int deliveryRate, quote, profit, numUnsoldUnits, numUnitsSold;
    // another variable I would like to add is the ability to kill off the broker
    // if they have not profit.  So, I could start out with many many brokers
    // with the some amount of profit. then once a broker has no profit they
    // can't buy packages.
    
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

    @Override
    public void unitSold() {
        numUnitsSold++;
    }

    @Override
    public void notSold() {
        numUnsoldUnits++;
    }

    @Override
    public int getNumUnitsNotSold() {
        return numUnsoldUnits;
    }
    
    @Override
    public int getNumUnitsSold() {
        return numUnitsSold;
    }
    
}
