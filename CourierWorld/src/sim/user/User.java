/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.user;

import java.util.List;
import sim.courier.Courier;
import sim.courierworld.NodePackage;

/**
 * Purpose of a user is to randomly generate NodePackage objects
 * for its node.  So each node contains a User.
 * @author drew
 */
public class User {
    private NodePackage nodePackage;

    public User() {
        nodePackage = new NodePackage();
    }
    
    /**
     * Each timestep a user 
     */
    public void generateNodePackage()
    {
        
    }

    
    public Courier chooseCourier(List<Courier> courier)
    {
        // call the step function on 
        
        return null;
    }
}
