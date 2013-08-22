/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import sim.engine.SimState;
import sim.engine.Steppable;

/**
 *
 * @author drew
 */
public class MyRandomSequence extends MySequence {

    public MyRandomSequence(List steps) {
        super(steps);
    }

    @Override
    public void step(SimState state) {
        Collections.shuffle(mySteps);
        super.step(state);
    }
    
    
}
