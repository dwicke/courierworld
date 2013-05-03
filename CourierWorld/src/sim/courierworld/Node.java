/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.util.ArrayList;
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
    private Hub hub;
    private List<Courier> couriers;
    private boolean isHub;

    public Node(User user) {
        this.user = user;
        couriers = new ArrayList<>();
        isHub = false;
        
    }    

    Node(Hub hub) {
        isHub = true;
        this.hub = hub;
        couriers = new ArrayList<>();
    }
    
    public void addCourier(Courier c)
    {
        this.couriers.add(c);
    }
    
    public int getNumCouriers()
    {
        return couriers.size();
    }

    @Override
    public void step(SimState state) {
        user.generatePackages();
        Courier chosen = user.chooseCourier(couriers);
        
        
    }

    boolean isHub() {
        return isHub; //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
