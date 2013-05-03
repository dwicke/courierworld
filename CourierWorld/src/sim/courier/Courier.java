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
import sim.courierworld.Node;
import sim.courierworld.NodeKey;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.network.Edge;

/**
 *
 * @author drew
 */
public class Courier implements DutchBidder, EnglishBidder, Steppable{

    
    
    private int profit;
    private boolean isGlobal;
    // map the edges to costs
    private HashMap<NodeKey, Double> myNetwork;
    private List<Node> sourceNode;
    
    public Courier(boolean  isGlobal)
    {
        myNetwork = new HashMap<>();
        sourceNode = new ArrayList<>(); 
        this.isGlobal = isGlobal;
    }
    
    public void insertKeyValPair(NodeKey k, double weight)
    {
        myNetwork.put(k, weight);
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
    public List<Node> getSourceNodes()
    {
        return sourceNode;
    }
   

   
    
}
