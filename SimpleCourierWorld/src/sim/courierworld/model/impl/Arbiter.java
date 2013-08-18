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
 *
 * @author drew
 */
public class Arbiter implements IArbiter{

    @Override
    public void step(SimState state) {
        SimpleCourierWorld scw = (SimpleCourierWorld) state;
        scw.setUnitBroker((IBroker)scw.getBrokers().objs[0]);
        System.out.println("In Arbiter and picked broker: " + scw.getBrokers().objs[0].toString());
    }
    
}
