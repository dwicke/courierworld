
package sim.courierworld.model;

import sim.courierworld.SimpleCourierWorld;

/**
 * Brokers are the go between for those that produce units and those that consume.
 */
public interface IBroker {

    /**
     * This method is called to say how
     * much this broker charges to take the unit
     * @param state the current state of the world that contains
     * the unit
     */
    public void setQuote(SimpleCourierWorld state);
    
    /**
     * This is the price that the broker is willing to spend
     * to deliver the package.  The broker wants to minimize
     * the price it pays but it doesn't want to set it soo low
     * that it doesn't get its package delivered.
     * @param state the world
     */
    public void setDeliveryRate(SimpleCourierWorld state);
    
    public void setID(int id);
    public int getID();
    public int getDeliveryRate();
    public int getQuote();
    public void setDeliveryRate(int deliveryRate);
    public void setQuote(int quote);
    public int getNumUnitsNotSold();
    public int getNumUnitsSold();
    
    
    public void unitSold();

    public void notSold();
    
}
