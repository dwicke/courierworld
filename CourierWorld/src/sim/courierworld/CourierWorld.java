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

    public Network world = new Network(false);
    public SparseGrid2D grid = new SparseGrid2D(500, 500);

    public enum WorldProperties {

        numberNodes,
        numSmallCouriers,
        numGlobalCouriers,
        numHubs,
        distFromHubs,
        decayRate
    };
    public int numberNodes = 1000;      // number of communities other than hubs
    public int numSmallCouriers = 100; // number of couriers that can transport packages cost effectively between nonhubs
    public int numGlobalCouriers = 10; // number of couriers that can transport packages cost effectively between hubs and 
    public int numHubs = 10;            // number of hubs in the network
    public int distFromHubs = 10; // minimum distance between hubs
    public List<Node> hubs;
    public PackageGenerator pGen;
    public double decayRate;

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
                    case decayRate:
                        decayRate = Double.valueOf(prop.getProperty(propName));
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
                    default:
                        System.out.println("property: " + propName + " not an understood property");
                        break;
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        // clear the world!
        /*
         world.clear();
         grid.clear();
         hubs = new ArrayList<>();

         pGen = new PackageGenerator(numHubs, numGlobalCouriers, numSmallCouriers);

         // build the network
         // while building the agents cost overlay
         for (int i = 0; i < numHubs; i++) {
         // build a "hub" in the network and insert the node into the grid
         Node curHub = new Node();
         world.addNode(curHub);
         hubs.add(curHub);

         boolean added = false;
         while (added == false) {
         int randx = random.nextInt(grid.getWidth());
         int randy = random.nextInt(grid.getHeight());
         Bag res = new Bag();
         res = grid.getMooreNeighbors(randx, randy, distFromHubs, Grid2D.BOUNDED, res, null, null);

         if (res.isEmpty()) {
         // add the node to randx, randy
         grid.setObjectLocation(curHub, randx, randy);
         added = true;
         }
         }
         }

         // link the hubs 
         for (Node n : hubs) {
         int numLinks = random.nextInt(numHubs - 2) + 2;

         }




         // build the two brokers

         // build the couriers


         // schedule the package generator
         schedule.scheduleOnce(new PackageGenerator(numHubs, numGlobalCouriers, numSmallCouriers), 0);
         // schedule the couriers
         */
    }

    public static void main(String[] args) {
        doLoop(CourierWorld.class, args);
        System.exit(0);
    }
}
