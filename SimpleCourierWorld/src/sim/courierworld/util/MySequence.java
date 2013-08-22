/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld.util;

import java.util.Collection;
import java.util.List;
import sim.engine.SimState;
import sim.engine.Steppable;

/**
 *
 * @author drew
 */
public class MySequence implements Steppable{

    protected List mySteps;
    
    public MySequence(List steps) {
        mySteps = steps;
    }
    
    @Override
    public void step(SimState state) {
       for (Object obj : mySteps) {
           ((Steppable)obj).step(state);
       }
    }
    
}
