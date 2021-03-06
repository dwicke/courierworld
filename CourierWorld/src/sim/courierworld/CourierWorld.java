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
import sim.broker.Broker;
import sim.courier.Courier;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.Grid2D;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Edge;
import sim.field.network.Network;
import sim.util.Bag;

/**
 *
 * @author drew
 */
public class CourierWorld extends SimState implements Steppable {

    public int gridSize = 500;
    public double maxPolicyVal = 0.5;
    public int numberNodes = 100;      // number of communities other than hubs
    public int numSmallCouriers = 200; // number of couriers that can transport packages cost effectively between nonhubs
    public int numGlobalCouriers = 10; // number of couriers that can transport packages cost effectively between hubs and 
    public int numHubs = 3;            // number of hubs in the network
    public int distFromHubs = 100; // minimum distance between hubs
    public int numLocalNode = 8;
    public int numMaxGlobCourierPerHub = 4;
    public int numMinGlobCourierPerHub = 2;
    public int numMaxPkgs = 100;
    public int localCliqueSize = 50; //radius of the cliques
    public int hubNhbrSize = 20;
    public int maxNumCouriersPerHub = 10;//maxNumCouriersPerHub < minNumCouriersPerNode*numLocalNode
    public int maxNumCouriersPerNode = 4;
    public double maxWeight = 1;
    public double minWeight = 0;
    public double minViableWeight = 0.2;
    public int maxRandTravel = 5;
    public List<Node> hubNodes;
    //public PackageGenerator pGen;
    //public double decayRate;
    public SparseGrid2D grid = new SparseGrid2D(gridSize, gridSize);
    public int minNumCouriersPerNode = 2;
    public List<Courier> globalCourierList;
    double randGivePack = 0.8;// chance that courier will decide to give a courier a package while a courier is delivering packages
    public Network logisticNetwork = new Network(false);// undirecterd graph
    public Edge[][] adgList;
    
    

    @Override
    public void step(SimState state) {


       // System.err.println("Timestep: " + state.schedule.getSteps());
        //user to local courier
        for (int i = 0; i < grid.allObjects.numObjs; i++) {
            Node node = (Node) grid.allObjects.objs[i];

            if (!node.isHub()) {
                // generate a package                
                node.userToCourier(this);
            }
        }

        // local courier to broker
        for (Node hubnode : hubNodes) {
            for (Broker brok : hubnode.getHub().brokers) {
                //brok.updateServiceRate();
            }
            for (Courier cour : hubnode.getHub().localCouriers) {
                cour.sendPackageToBroker(hubnode.getHub().brokers, this);
            }
        }

        // broker to couriers through auction in each hub
        for (Node hubnode : hubNodes) {

            // perform an auction with each broker
            for (Broker broker : hubnode.getHub().brokers) {
                List<Courier> allCours = new ArrayList<>();
                allCours.addAll(globalCourierList);
                allCours.addAll(hubnode.getHub().localCouriers);

                broker.performAuctions(allCours, this, hubnode);
                broker.decayPackages();

            }
            // must immediatly move packs globally since this is the only time
            // I know the true source.
            // global courier to broker
            for (Courier gCour : globalCourierList) {
                // moves packages to most profitable broker
                // global couriers are globally connected so
                // they have the option of transporting to the
                // correct hub where the local destination stems
                // from or to a different hub where it the stacks
                // will be auctioned off next timestep
                gCour.movePacksGlobally(hubnode, this);
            }
        }

        // local courier to user
        for (Node hubnode : hubNodes) {
            for (Courier cour : hubnode.getHub().localCouriers) {
                // so the local couriers deliver the stacks to the
                // destination nodes.  Since the local couriers
                // are fully connected to all other nodes in the
                // local substructure they can pass through
                // x number of local nodes and ask for user for
                // stacks before delivering the stacks to the dest.
                cour.deliverStacks(hubnode, this);
            }
            
            double sum = 0.0;
            for (int i = 0; i < hubnode.getHub().brokers.size(); i++)
            {
                sum += hubnode.getHub().brokers.get(i).profit;
            }
            for (int i = 0; i < hubnode.getHub().brokers.size(); i++){
                hubnode.getHub().profitShare.set(i, hubnode.getHub().brokers.get(i).profit/sum);
            }
        }
        
        System.err.println("------------------------------");

    }

    public enum WorldProperties {

        numberNodes,
        numSmallCouriers,
        numGlobalCouriers,
        numHubs,
        distFromHubs,
        numLocalNode;
    };

    public CourierWorld(long seed) {
        super(seed);
    }

    @Override
    public void start() {
        super.start();
        globalCourierList = new ArrayList<>();
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
        //setup global courier
        for (int i = 0; i < numGlobalCouriers; i++) {
            Courier gc = new Courier(true);
            globalCourierList.add(gc);
        }
        hubNodes = new ArrayList<>();
        //initialize the hubs and local couriers inside it
        for (int i = 0; i < numHubs; i++) {
            Hub h = new Hub(this);
            h.setup(userIndex);
            hubNodes.add(h.myNode);
        }
        //setup the cost network for the global courier
        for (Courier c : globalCourierList) {
            c.randInit(hubNodes, this, null);
        }

        for (int i = 0; i < grid.allObjects.numObjs; i++) {
            Node nodei = (Node) grid.allObjects.objs[i];
            for (int j = i + 1; j < grid.allObjects.numObjs; j++) {
                Node nodej = (Node) grid.allObjects.objs[j];
                if (!nodei.isHub() && !nodej.isHub()) {
                    if (nodei.getUser().getHub().hashCode() == nodej.getUser().getHub().hashCode()) {
                        logisticNetwork.addEdge(nodei, nodej, new Throughput("LC"));
                    }

                } else if (nodei.isHub() && nodej.isHub()) {
                    logisticNetwork.addEdge(nodei, nodej, new Throughput("GC"));
                } else if (nodei.isHub() && !nodej.isHub()) {
                    if (nodei.getHub().localNodes.contains(nodej)) {
                        logisticNetwork.addEdge(nodei, nodej, new Throughput("LGC"));
                    }
                }

            }

        }

        adgList = logisticNetwork.getAdjacencyMatrix();



        schedule.scheduleRepeating(this);

    }

    public static void main(String[] args) {
        doLoop(CourierWorld.class, args);
        System.exit(0);
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public double getMaxPolicyVal() {
        return maxPolicyVal;
    }

    public void setMaxPolicyVal(double maxPolicyVal) {
        this.maxPolicyVal = maxPolicyVal;
    }

    public int getNumberNodes() {
        return numberNodes;
    }

    public void setNumberNodes(int numberNodes) {
        this.numberNodes = numberNodes;
    }

    public int getNumSmallCouriers() {
        return numSmallCouriers;
    }

    public void setNumSmallCouriers(int numSmallCouriers) {
        this.numSmallCouriers = numSmallCouriers;
    }

    public int getNumGlobalCouriers() {
        return numGlobalCouriers;
    }

    public void setNumGlobalCouriers(int numGlobalCouriers) {
        this.numGlobalCouriers = numGlobalCouriers;
    }

    public int getNumHubs() {
        return numHubs;
    }

    public void setNumHubs(int numHubs) {
        this.numHubs = numHubs;
    }

    public int getDistFromHubs() {
        return distFromHubs;
    }

    public void setDistFromHubs(int distFromHubs) {
        this.distFromHubs = distFromHubs;
    }

    public int getNumLocalNode() {
        return numLocalNode;
    }

    public void setNumLocalNode(int numLocalNode) {
        this.numLocalNode = numLocalNode;
    }

    public int getNumMaxGlobCourierPerHub() {
        return numMaxGlobCourierPerHub;
    }

    public void setNumMaxGlobCourierPerHub(int numMaxGlobCourierPerHub) {
        this.numMaxGlobCourierPerHub = numMaxGlobCourierPerHub;
    }

    public int getNumMinGlobCourierPerHub() {
        return numMinGlobCourierPerHub;
    }

    public void setNumMinGlobCourierPerHub(int numMinGlobCourierPerHub) {
        this.numMinGlobCourierPerHub = numMinGlobCourierPerHub;
    }

    public int getNumMaxPkgs() {
        return numMaxPkgs;
    }

    public void setNumMaxPkgs(int numMaxPkgs) {
        this.numMaxPkgs = numMaxPkgs;
    }

    public int getLocalCliqueSize() {
        return localCliqueSize;
    }

    public void setLocalCliqueSize(int localCliqueSize) {
        this.localCliqueSize = localCliqueSize;
    }

    public int getHubNhbrSize() {
        return hubNhbrSize;
    }

    public void setHubNhbrSize(int hubNhbrSize) {
        this.hubNhbrSize = hubNhbrSize;
    }

    public int getMaxNumCouriersPerHub() {
        return maxNumCouriersPerHub;
    }

    public void setMaxNumCouriersPerHub(int maxNumCouriersPerHub) {
        this.maxNumCouriersPerHub = maxNumCouriersPerHub;
    }

    public int getMaxNumCouriersPerNode() {
        return maxNumCouriersPerNode;
    }

    public void setMaxNumCouriersPerNode(int maxNumCouriersPerNode) {
        this.maxNumCouriersPerNode = maxNumCouriersPerNode;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(double minWeight) {
        this.minWeight = minWeight;
    }

    public double getMinViableWeight() {
        return minViableWeight;
    }

    public void setMinViableWeight(double minViableWeight) {
        this.minViableWeight = minViableWeight;
    }

    public int getMaxRandTravel() {
        return maxRandTravel;
    }

    public void setMaxRandTravel(int maxRandTravel) {
        this.maxRandTravel = maxRandTravel;
    }

    public int getMinNumCouriersPerNode() {
        return minNumCouriersPerNode;
    }

    public void setMinNumCouriersPerNode(int minNumCouriersPerNode) {
        this.minNumCouriersPerNode = minNumCouriersPerNode;
    }

    public double getRandGivePack() {
        return randGivePack;
    }

    public void setRandGivePack(double randGivePack) {
        this.randGivePack = randGivePack;
    }
}
