/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld.model.impl;

import sim.courierworld.SimpleCourierWorld;

/**
 *
 * @author drew
 */
public class DynamicMarket extends Market{

    @Override
    public void setMarketValue(SimpleCourierWorld world) {
        super.setMarketValue(world);
        // the market value is hidden from the brokers.  those that learn this will have a better
        // delivery rate and will dominate the market.  Once the market perceives that the
        // agents have setteled on that value it should increase because the market will expect the broker
        // to pay more.  So, I want to see how well the agents adapt to the changing market minimum
        
    }
    
    
    
}
