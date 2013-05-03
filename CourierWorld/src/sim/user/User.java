/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.user;

import java.util.List;
import sim.courier.Courier;
import sim.courierworld.NodePackage;


/**
 * Purpose of a user is to randomly generate NodePackage objects for its node.
 * So each node contains a User.
 *
 * @author drew
 */
public class User {

    private NodePackage nodePackage;
    private int max_packages;
    private long userID;

    public User(int max_packages, long userID) {
        nodePackage = new NodePackage();
        this.max_packages = max_packages;
        this.userID = userID;
    }

    /**
     * Each timestep a user generates a node package
     */
    public void generatePackages() {
    }

    public Courier chooseCourier(List<Courier> courier) {
        // call the step function on 

        return null;
    }

    /*public boolean acceptPackage(Packages packsDelivered) {

        if (packsDelivered.userID == this.userID) {
            return true;
        }
        return false;
    }*/
}
