/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sim.auction.DutchBidder;
import sim.auction.EnglishBidder;
import sim.auction.Item;
import sim.courierworld.CourierWorld;
import sim.courierworld.Node;
import sim.courierworld.NodeKey;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.network.Edge;

/**
 *
 * @author drew
 */
public class Courier implements DutchBidder, EnglishBidder, Steppable {

    private int profit;
    private boolean isGlobal;
    // map the edges to costs
    private HashMap<NodeKey, Double> myNetwork;
    private List<Node> sourceNode;

    public Courier(boolean isGlobal) {
        myNetwork = new HashMap<>();
        sourceNode = new ArrayList<>();
        this.isGlobal = isGlobal;
    }

    public void insertKeyValPair(NodeKey k, double weight) {
        myNetwork.put(k, weight);
    }

    public void randInit(List<Courier> courierList, List<Node> nodeChoice, CourierWorld state, Node hubNode) {
        // Set up the couriers for the local clique
        for (Courier curCour : courierList) {

            // generate a random fully connected graph within the local nodes
            for (Node n : nodeChoice) {
                for (Node m : nodeChoice) {
                    if (!n.equals(m)) {
                        double randWeight = state.minWeight + state.random.nextDouble() * (state.maxWeight - state.minWeight);
                        curCour.insertKeyValPair(new NodeKey(n, m), randWeight);
                    }
                }
            }


            if (!isGlobal) {
                // must also connect to the global hub!
                for (Node n : nodeChoice) {
                    double randWeight = state.minWeight + state.random.nextDouble() * (state.maxWeight - state.minWeight);
                    curCour.insertKeyValPair(new NodeKey(n, hubNode), randWeight);
                }

                // loop over the nodes that the courier occupies
                for (Node n : curCour.getSourceNodes()) {
                    Node nextNode = n, prevNode = n;
                    while (nextNode != hubNode) {
                        nextNode = nodeChoice.get(state.random.nextInt(nodeChoice.size()));
                        double randWeight = state.minViableWeight + state.random.nextDouble() * (state.maxWeight - state.minViableWeight);
                        curCour.getMap().put(new NodeKey(prevNode, nextNode), randWeight);
                    }
                }
            }
            else
            {
                
            }




        }
    }

    @Override
    public boolean getDutchBid(int val, Item item) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doTransaction(int charge, Item item) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getEnglishBid(Item item, int val, EnglishBidder winner) {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void step(SimState state) {

        // use 1 since after packagegenerator
        state.schedule.scheduleOnce(this, 1);
    }

    public HashMap<NodeKey, Double> getMap() {
        return myNetwork;
    }

    public void addSource(Node userNode) {
        sourceNode.add(userNode);
    }

    public List<Node> getSourceNodes() {
        return sourceNode;
    }
}
