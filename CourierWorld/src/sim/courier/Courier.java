/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courier;

import ec.util.MersenneTwisterFast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import sim.auction.DutchBidder;
import sim.auction.EnglishBidder;
import sim.auction.Item;
import sim.broker.Broker;
import sim.courierworld.*;
import sim.engine.SimState;
import sim.engine.Steppable;

/**
 *
 * @author drew
 */
public class Courier {

    private double profit;
    public Warehouse myPackages;
    private boolean isGlobal;
    // map the edges to costs
    private HashMap<NodeKey, Double> myNetwork;
    private List<Node> sourceNode;
    private double policy;
    private Broker userBroker; // the broker i used to give user a quote and used to give success rate.

    
    
    public Courier(boolean isGlobal) {
        myNetwork = new HashMap<>();
        sourceNode = new ArrayList<>();
        myPackages = new Warehouse();
        this.isGlobal = isGlobal;
    }

    public void insertKeyValPair(NodeKey k, double weight) {
        myNetwork.put(k, weight);
    }

    public void randInit(List<Node> nodeChoice, CourierWorld state, Node hubNode) {
        // Set up the couriers
        // generate a random fully connected graph
        for (Node n : nodeChoice) {
            for (Node m : nodeChoice) {
                if (!n.equals(m)) {
                    double randWeight = state.minWeight + state.random.nextDouble() * (state.maxWeight - state.minWeight);
                    insertKeyValPair(new NodeKey(n, m), randWeight);
                }
            }
        }


        if (!isGlobal) {
            // must also connect to the global hub
            for (Node n : nodeChoice) {
                double randWeight = state.minWeight + state.random.nextDouble() * (state.maxWeight - state.minWeight);
                insertKeyValPair(new NodeKey(n, hubNode), randWeight);
            }

            // loop over the nodes that the courier occupies
            for (Node n : getSourceNodes()) {
                Node nextNode = n, prevNode = n;
                while (nextNode != hubNode) {
                    nextNode = nodeChoice.get(state.random.nextInt(nodeChoice.size()));
                    double randWeight = state.minViableWeight + state.random.nextDouble() * (state.maxWeight - state.minViableWeight);
                    getMap().put(new NodeKey(prevNode, nextNode), randWeight);
                }
            }
        } else {
            double randWeight = state.minViableWeight + state.random.nextDouble() * (state.maxWeight - state.minViableWeight);

            insertKeyValPair(new NodeKey(nodeChoice.get(state.random.nextInt(nodeChoice.size())), nodeChoice.get(state.random.nextInt(nodeChoice.size()))), randWeight);
        }
    }

    /**
     * The cost network for the courier.
     *
     * @return
     */
    public HashMap<NodeKey, Double> getMap() {
        return myNetwork;
    }

    /**
     * Add a node that the courier is located in.
     *
     * @param userNode
     */
    public void addSource(Node userNode) {
        sourceNode.add(userNode);
    }

    /**
     * Gets the list of nodes that the courier occupies.
     *
     * @return
     */
    public List<Node> getSourceNodes() {
        return sourceNode;
    }

    /**
     * What is the probability of success. Calculated based on the broker
     * success rate. Can't calculate the success rate directly since not
     * tracking each package individually.
     *
     * @return
     */
    public double getSuccessRate() {
        // 1 = defaulRate + succRate -> succRate = 1 - defaultRate
        return 1.0 - userBroker.getDefaultRate();
    }

    public void recievePackage(Entry<Warehouse.Key, Integer> stack, double fee) {
        myPackages.updateStack(stack.getKey().dest, stack.getKey().priority, stack.getValue());
        profit += fee; //TODO
    }

    public double getQuote(Entry<Warehouse.Key, Integer> stack, Node hubNode) {

        Warehouse h = new Warehouse();

        h.updateStack(stack.getKey(), stack.getValue());
        // loop over the brokers and get a quote.


        double bestQuote = -1;

        // for each of the brokers get a quote
        for (Broker b : hubNode.getHub().brokers) {
            double quote = b.getQuote(myPackages);
            double succRate = b.getDefaultRate();

            if (bestQuote == -1) {
                bestQuote = policy * quote * (1 - succRate) + (1 - policy) * quote;
                userBroker = b;
            } else if (bestQuote > policy * quote * (1 - succRate) + (1 - policy) * quote) {
                bestQuote = policy * quote * (1 - succRate) + (1 - policy) * quote;
                userBroker = b;
            }
        }
        // return the best quote.
        return bestQuote;

    }

    public void sendPackageToBroker(List<Broker> brokers) {
        if (myPackages.hasStack()) {
            Broker bestBroker = null;
            double bestQuote = -1;

            // for each of the brokers get a quote
            for (Broker b : brokers) {
                double quote = b.getQuote(myPackages);
                double succRate = b.getDefaultRate();

                if (bestQuote == -1) {
                    bestQuote = policy * quote * (1 - succRate) + (1 - policy) * quote;
                    bestBroker = b;
                } else if (bestQuote > policy * quote * (1 - succRate) + (1 - policy) * quote) {
                    bestQuote = policy * quote * (1 - succRate) + (1 - policy) * quote;
                    bestBroker = b;
                }
            }

            // give the broker his fee and the packages
            if (bestBroker != null) {
                bestBroker.addPackage(myPackages, bestQuote / (1.0 - policy + policy * (1 - bestBroker.getDefaultRate())));
                // i have to reset myPackages I have given them to the broker
                myPackages.clear();
            }
        }
    }

    public void movePacksGlobally(Node globalNode, CourierWorld world) {
        // moves packages to most profitable broker
        // global couriers are globally connected so
        // they have the option of transporting to the
        // correct hub where the local destination stems
        // from or to a different hub where it the stacks
        // will be auctioned off next timestep
       
        
        // probability of delivering to global node that has the local node
        // stemming from it given priority
        // loop over the packages
        Iterator<Entry<Warehouse.Key, Integer>> iter = myPackages.getIterator();
        

        while (iter.hasNext()) {
            
            
            
            
            
        }
        
        
    }

    /**
     * Delivers the packages and tries to get customers along the way.
     */
    public void deliverStacks(Node globalNode, CourierWorld world) {

        // loop over the packages
        Iterator<Entry<Warehouse.Key, Integer>> iter = myPackages.getIterator();
        int maxRandTravel = world.maxRandTravel;

        while (iter.hasNext()) {
            Entry<Warehouse.Key, Integer> stacks = iter.next();
            Node curNode = globalNode;// where I am right now
            int randTrav = world.random.nextInt(maxRandTravel + 1);

            // travel to randTrav number of nodes.
            for (int i = 0; i < randTrav; i++) {
                double best = world.maxWeight;
                Node next = null;
                // find the best next node.
                for (Node localNode : globalNode.getHub().localNodes) {
                    if (curNode != localNode) {
                        if (myNetwork.get(new NodeKey(curNode, localNode)) < best) {
                            best = myNetwork.get(new NodeKey(curNode, localNode));
                            next = localNode;
                        }
                    }
                }

                if (next != null) {
                    // not sure if i should do the division...
                    profit -= (best * (stacks.getValue() / randTrav));
                    next.getUser().randGivePackage(this, world);
                    curNode = next;
                }

            }
            
            // deliver to the destination node
            profit -= (myNetwork.get(new NodeKey(curNode, stacks.getKey().dest)) * (stacks.getValue()));
            
        }


    }
}
