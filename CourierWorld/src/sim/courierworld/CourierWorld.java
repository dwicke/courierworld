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
import sim.field.grid.Grid2D;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.util.Bag;

/**
 *
 * @author drew
 */
public class CourierWorld extends SimState {

    public int gridSize = 500;
    public SparseGrid2D grid = new SparseGrid2D(gridSize, gridSize);
    public int minNumCouriersPerNode = 2;
    public List<Courier> globalCourierList;

    public enum WorldProperties {

        numberNodes,
        numSmallCouriers,
        numGlobalCouriers,
        numHubs,
        distFromHubs,
        numLocalNode;
    };
    public int numberNodes = 1000;      // number of communities other than hubs
    public int numSmallCouriers = 100; // number of couriers that can transport packages cost effectively between nonhubs
    public int numGlobalCouriers = 10; // number of couriers that can transport packages cost effectively between hubs and 
    public int numHubs = 10;            // number of hubs in the network
    public int distFromHubs = 10; // minimum distance between hubs
    public int numLocalNode = 100;
    public int numGlobCourierPerHub = 4;
    public int numMaxPkgs = 100;
    public int localCliqueSize = 50;
    public int hubNhbrSize = 20; 
    public int maxNumCouriersPerHub = 10;
    public int maxNumCouriersPerNode = 5;
    public double maxWeight = 1;
    public double minWeight = 0;
    public double minViableWeight = 0.2;
    //public List<Node> hubs;
    //public PackageGenerator pGen;
    //public double decayRate;

    public CourierWorld(long seed) {
        super(seed);
    }

    public void start() {
        super.start();


        // Need to load params from file
        Properties prop = new Properties();
        try {
            // the configuration file name
            String fileName = "world.config";
            InputStream is = new FileInputStream(fileName);

            // load the properties file
            prop.load(is);

            for (String propName : prop.stringPropertyNames()) {
                switch (WorldProperties.valueOf(propName)) {
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


        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }


        // clear the world!

        grid.clear();
        
        int userIndex = 0;
        for(int i = 0; i < numGlobalCouriers; i++)
        {
            Courier gc = new Courier(true);
        }
        for (int i = 0; i < numHubs; i++)
        {            
            Hub h = new Hub(this, userIndex);            
        }
        
 



        // build the two brokers

        // build the couriers


        // schedule the package generator
        schedule.scheduleOnce(new PackageGenerator(numHubs, numGlobalCouriers, numSmallCouriers), 0);
        // schedule the couriers

    }

    public static void main(String[] args) {
        doLoop(CourierWorld.class, args);
        System.exit(0);
    }
}
