/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld.model;

import sim.courierworld.SimpleCourierWorld;

/**
 * This class represents the market
 * that the broker must deal 
 * with to get the unit delivered
 * It sets the minimum price that brokers
 * must pay to get the unit delivered.
 * @author drew
 */
public interface IMarket {
    
    /**
     * This method sets the value of the unit stored in the world.
     * It represents the minimum that the market would take for that unit.
     * @param world the world with the unit
     */
    public void setMarketValue(SimpleCourierWorld world);
}
