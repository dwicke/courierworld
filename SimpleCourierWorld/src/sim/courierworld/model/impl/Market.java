/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld.model.impl;

import sim.courierworld.SimpleCourierWorld;
import sim.courierworld.model.IBroker;
import sim.courierworld.model.IMarket;
import sim.engine.SimState;

/**
 * Simple static market 
 * @author drew
 */
public class Market implements IMarket {
    
    private int minMarketVal; // this is hidden from the brokers.  those that learn this will have a better
    // delivery rate and will dominate the market.  Once the market perceives that the
    // agents have setteled on that value it should increase because the market will expect the broker
    // to pay more.  So, I want to see how well the agents adapt to the changing market minimum

    public int getMinMarketVal() {
        return minMarketVal;
    }

    public void setMinMarketVal(int minMarketVal) {
        this.minMarketVal = minMarketVal;
    }

    @Override
    public void setMarketValue(SimpleCourierWorld world) {
        setMinMarketVal(10);
        IBroker unitBroker = world.getUnitBroker();
        System.out.println("The market min is 10 and the broker " + unitBroker.toString() + " is willing to pay " + unitBroker.getDeliveryRate());
        if (unitBroker.getDeliveryRate() >= getMinMarketVal()) {
            // make emotions that the market can display to the broker to help in its learning the market min
            // could make an enum satisfied, laughable, etc...
            unitBroker.unitSold();
        }
        else {
            unitBroker.notSold();
        }
    }

    @Override
    public void step(SimState state) {
        setMarketValue((SimpleCourierWorld) state);
    }
    
}
