/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import sim.courierworld.model.IArbiter;
import sim.courierworld.model.IMarket;
import sim.courierworld.model.Unit;
import sim.engine.SimState;
import static sim.engine.SimState.doLoop;
import sim.engine.Steppable;
import sim.util.Bag;

/**
 *
 * @author drew
 */
public class SimpleCourierWorld extends SimState implements Steppable {

    
    
    private Bag brokers;
    private IArbiter arbiter;
    private IMarket market;
    private Unit unit;
    
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    public SimpleCourierWorld(long seed) {
        super(seed);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        doLoop(SimpleCourierWorld.class, args);
        System.exit(0);
    }

    @Override
    public void step(SimState state) {
        
        // create a unit
        setUnit(new Unit());
        
        
    }
    
    @Override
    public void start() {
        super.start();
        
        // create the brokers
        
        
        // set up the schedule
        // first step this to create the unit
        
        // step the brokers to set their quotes for this unit
        // call the arbiter
        // 
        
    }
}
