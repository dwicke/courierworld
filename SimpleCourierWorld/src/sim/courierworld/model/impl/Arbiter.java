/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld.model.impl;

import sim.courierworld.SimpleCourierWorld;
import sim.courierworld.model.IArbiter;
import sim.courierworld.model.IBroker;
import sim.engine.SimState;

/**
 * Simple arbiter that picks the Broker with the smallest quote
 * @author drew
 */
public class Arbiter implements IArbiter{

    @Override
    public void step(SimState state) {
        SimpleCourierWorld scw = (SimpleCourierWorld) state;
        IBroker minBroker = null;
        for (Object cur : scw.getBrokers()) {
            IBroker curBroker = (IBroker) cur;
            if(minBroker == null || (minBroker != null && curBroker.getQuote() < minBroker.getQuote())) {
                minBroker = curBroker;
            }
        }
        if (minBroker != null) {
            scw.setUnitBroker(minBroker);
            System.out.println("In Arbiter and picked broker: " + minBroker.toString());
        }
    }
    
}
