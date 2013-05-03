/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.util.List;
import sim.courier.Courier;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.user.User;

/**
 * keep track of delivery
 * of packages to this node.
 * @author drew
 */
public class Node implements Steppable{
    
    private User user;
    private List<Courier> couriers;

    public Node(User user, List<Courier> couriers) {
        this.user = user;
        this.couriers = couriers;
    }    

    @Override
    public void step(SimState state) {
        user.generatePackages();
        Courier chosen = user.chooseCourier(couriers);
        
        
    }
    
    
    
}
