/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.user;

import java.util.List;
import sim.courier.Courier;
import sim.courierworld.CourierWorld;
import sim.courierworld.Hub;
import sim.courierworld.Node;
import sim.courierworld.NodePackage;
import sim.courierworld.Packages;

/**
 * Purpose of a user is to randomly generate NodePackage objects for its node.
 * So each node contains a User.
 *
 * @author drew
 */
public class User
{

    private Packages packages;
    private int max_packages;
    private long userID;
    private Node hub;
    public double policy;

    public User(int max_packages, long userID, Node hub, double policy)
    {
        this.max_packages = max_packages;
        this.userID = userID;
        this.hub = hub;
        this.policy = policy;
    }

    /**
     * Each timestep a user generates a node package
     */
    public void generatePackages(CourierWorld world)
    {

        // loop throught the grid and choose a random node as the dest node.
        boolean isGood = false;

        while (isGood == false)
        {
            Node randNode = (Node) world.grid.allObjects.objs[world.random.nextInt(world.grid.allObjects.numObjs)];
            if (!randNode.isHub())
            {
                isGood = true;
                int numPacks = world.random.nextInt(max_packages);
                packages = new Packages(Packages.Priority.values()[world.random.nextInt(Packages.Priority.values().length)], randNode, numPacks, numPacks);
                packages.destNode = randNode;

            }
        }

    }

    //user gets quotes from different courier and gives packages to the courier with best quote
    public void givePackage(List<Courier> couriers, CourierWorld world)
    {
        generatePackages(world);
        if (packages.numberPacks > 0)
        {
            Courier bestCourier = null;
            double bestQuote = -1;

            for (Courier cour : couriers)
            {
                double quote = cour.getQuote(packages);
                double succRate = cour.getSuccessRate();

                if (bestQuote == -1)
                {
                    bestQuote = policy * quote * (1 - succRate) + (1 - policy) * quote;
                    bestCourier = cour;
                } else if (bestQuote > policy * quote * (1 - succRate) + (1 - policy) * quote)
                {
                    bestQuote = policy * quote * (1 - succRate) + (1 - policy) * quote;
                    bestCourier = cour;
                }
            }

            if (bestCourier != null)
            {
                bestCourier.recievePackage(packages, bestQuote / (1.0 - policy + policy * (1 - bestCourier.getSuccessRate())));
            }
        }
    }
}
