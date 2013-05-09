/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import sim.courier.Courier;
import sim.courierworld.CourierWorld;
import sim.courierworld.Hub;
import sim.courierworld.Node;
import sim.courierworld.Warehouse;

/**
 * Purpose of a user is to randomly generate warehouse objects to be delivered.
 * So each node contains a User.
 *
 * @author drew
 */
public class User {

    private Warehouse allPackages;
    private int max_packages;
    private long userID;
    private Node hub;
    public double policy;
    private double randGivePack;

    public Node getHub() {
        return hub;
    }

    public User(int max_packages, long userID, Node hub, double policy, double randGivePack) {
        this.max_packages = max_packages;
        this.userID = userID;
        this.hub = hub;
        this.policy = policy;
        this.randGivePack = randGivePack;
        allPackages = new Warehouse();
    }

    /**
     * Each timestep a user generates
     */
    public void generatePackages(CourierWorld world) {

        // loop throught the grid and choose a random node as the dest node.
        boolean isGood = false;
        allPackages.clear();
        while (isGood == false) {
            Node randNode = (Node) world.grid.allObjects.objs[world.random.nextInt(world.grid.allObjects.numObjs)];
            if (!randNode.isHub()) {
                isGood = true;
                int numPacks = world.random.nextInt(max_packages);
                if (numPacks > 0) {
                    allPackages.updateStack(randNode, Warehouse.Priority.values()[world.random.nextInt(Warehouse.Priority.values().length)], numPacks);
                }
            }
        }

    }

    /**
     * Returns whether the user gave a package.
     *
     * @param courier
     */
    public Map.Entry<Warehouse.Key, Integer> randGivePackage(Courier courier, CourierWorld world) {
        
        // should be based on a policy
        if (world.random.nextDouble() < randGivePack) {
            generatePackages(world);
            return (allPackages.getTotalNumPacks() == 0) ? null : allPackages.getPackage();
        }
        return null;
    }

    //user gets quotes from different courier and gives packages to the courier with best quote
    public void givePackage(List<Courier> couriers, CourierWorld world) {
        generatePackages(world);
        if (allPackages.hasStack()) {
            Courier bestCourier = null;
            double bestQuote = -1;

            for (Courier cour : couriers) {
                // must get quote first in order for the courier to say what its success rate is
                double quote = cour.getQuote(allPackages.getPackage(), hub.getHub().brokers);
                double succRate = cour.getSuccessRate();

                if (bestQuote == -1) {
                    bestQuote = policy * quote * (1 - succRate) + (1 - policy) * quote;
                    bestCourier = cour;
                } else if (bestQuote > policy * quote * (1 - succRate) + (1 - policy) * quote) {
                    bestQuote = policy * quote * (1 - succRate) + (1 - policy) * quote;
                    bestCourier = cour;
                }
            }

            if (bestCourier != null) {
                bestCourier.recievePackage(allPackages.getPackage(), bestQuote / (1.0 - policy + policy * (1 - bestCourier.getSuccessRate())));
            }
        }
    }
}
