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
 *
 * @author drew
 */
public class Market implements IMarket {
    
    private int minMarketVal;

    public int getMinMarketVal() {
        return minMarketVal;
    }

    public void setMinMarketVal(int minMarketVal) {
        this.minMarketVal = minMarketVal;
    }

    @Override
    public void setMarketValue(SimpleCourierWorld world) {
        minMarketVal = 10;
        IBroker unitBroker = world.getUnitBroker();
        System.out.println("The market min is 10 and the broker " + unitBroker.toString() + " is willing to pay " + unitBroker.getDeliveryRate());
    }

    @Override
    public void step(SimState state) {
        setMarketValue((SimpleCourierWorld) state);
    }
    
}
