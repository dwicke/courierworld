/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.user;

import java.util.List;
import sim.courier.Courier;
import sim.courierworld.Hub;
import sim.courierworld.Node;
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
    private Node hub;

    public User(int max_packages, long userID, Node hub) {
        nodePackage = new NodePackage();
        this.max_packages = max_packages;
        this.userID = userID;
        this.hub = hub;
    }

    public User(int numMaxPkgs, int useIndex, Node hubNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
