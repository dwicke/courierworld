/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import sim.engine.SimState;
import sim.engine.Steppable;

/**
 * generates the next timestep's packages
 * while keeping the old packages.
 * it also manages the time of the packages.
 * @author drew
 */
public class PackageGenerator implements Steppable{
    
    int numHubs, numGlobal, numSmall;
    public PackageGenerator(int numHubs, int numGlobal, int numSmall)
    {
        this.numGlobal = numGlobal;
        this.numHubs = numHubs;
        this.numSmall = numSmall;
    }
    
    /**
     * generates new packages and updates timestamps on current packages.
     */
    public void nextTimeStep()
    {
        
    }

    @Override
    public void step(SimState state) {
        nextTimeStep();// update the world
        // resechedule myself
        state.schedule.scheduleOnce(this, 0);
    }
    
    
    
    
}
