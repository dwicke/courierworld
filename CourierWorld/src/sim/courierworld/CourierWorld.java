/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import sim.courier.Courier;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.Grid2D;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Bag;

/**
 *
 * @author drew
 */
public class CourierWorld extends SimState implements Steppable
{

    public int gridSize = 500;
    public SparseGrid2D grid = new SparseGrid2D(gridSize, gridSize);
    public int minNumCouriersPerNode = 2;
    public List<Courier> globalCourierList;

    @Override
    public void step(SimState state)
    {
        for(int i = 0; i < grid.allObjects.numObjs; i++)
        {
            Node node = (Node) grid.allObjects.objs[i];
            
            if (!node.isHub())
            {
                // generate a package
                
                node
            }
            
            
        }
    }

    public enum WorldProperties
    {

        numberNodes,
        numSmallCouriers,
        numGlobalCouriers,
        numHubs,
        distFromHubs,
        numLocalNode;
    };
    public int numberNodes = 1000;      // number of communities other than hubs
    public int numSmallCouriers = 200; // number of couriers that can transport packages cost effectively between nonhubs
    public int numGlobalCouriers = 10; // number of couriers that can transport packages cost effectively between hubs and 
    public int numHubs = 10;            // number of hubs in the network
    public int distFromHubs = 10; // minimum distance between hubs
    public int numLocalNode = 100;
    public int numMaxGlobCourierPerHub = 4;
    public int numMinGlobCourierPerHub = 2;
    public int numMaxPkgs = 100;
    public int localCliqueSize = 50; //radius of the cliques
    public int hubNhbrSize = 20;
    public int maxNumCouriersPerHub = 10;//maxNumCouriersPerHub < minNumCouriersPerNode*numLocalNode
    public int maxNumCouriersPerNode = 5;
    public double maxWeight = 1;
    public double minWeight = 0;
    public double minViableWeight = 0.2;
    public List<Node> hubNodes;
    //public PackageGenerator pGen;
    //public double decayRate;

    public CourierWorld(long seed)
    {
        super(seed);
    }

    public void start()
    {
        super.start();


        // Need to load params from file
        Properties prop = new Properties();
        try
        {
            // the configuration file name
            String fileName = "world.config";
            InputStream is = new FileInputStream(fileName);

            // load the properties file
            prop.load(is);

            for (String propName : prop.stringPropertyNames())
            {
                switch (WorldProperties.valueOf(propName))
                {
                    case numberNodes:
                        numberNodes = Integer.parseInt(prop.getProperty(propName));
                        break;
                    case numGlobalCouriers:
                        numGlobalCouriers = Integer.parseInt(prop.getProperty(propName));
                        break;
                    case distFromHubs:
                        distFromHubs = Integer.parseInt(prop.getProperty(propName));
                        break;
                    case numHubs:
                        numHubs = Integer.parseInt(prop.getProperty(propName));
                        break;
                    case numSmallCouriers:
                        numSmallCouriers = Integer.parseInt(prop.getProperty(propName));
                        break;
                    case numLocalNode:
                        numLocalNode = Integer.parseInt(prop.getProperty(propName));
                    default:
                        System.out.println("property: " + propName + " not an understood property");
                        break;
                }
            }


        } catch (FileNotFoundException e)
        {
        } catch (IOException e)
        {
        }


        // clear the world!

        grid.clear();

        int userIndex = 0;
        //setup global courier
        for (int i = 0; i < numGlobalCouriers; i++)
        {
            Courier gc = new Courier(true);
            globalCourierList.add(gc);
        }
        hubNodes = new ArrayList<>();
        //initialize the hubs and local couriers inside it
        for (int i = 0; i < numHubs; i++)
        {
            Hub h = new Hub(this);
            h.setup(userIndex);
            hubNodes.add(h.myNode);
        }
        //setup the cost network for the global courier
        for (Courier c : globalCourierList)
        {
            c.randInit(hubNodes, this, null);
        }

        schedule.scheduleRepeating(this);

    }

    public static void main(String[] args)
    {
        doLoop(CourierWorld.class, args);
        System.exit(0);
    }
}
