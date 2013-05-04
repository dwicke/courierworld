/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.text.html.parser.Entity;
import sim.auction.DutchBidder;
import sim.auction.EnglishBidder;
import sim.auction.Item;
import sim.broker.Broker;
import sim.courierworld.CourierWorld;
import sim.courierworld.Hub;
import sim.courierworld.Node;
import sim.courierworld.NodeKey;
import sim.courierworld.NodePackage;
import sim.courierworld.Packages;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.network.Edge;

/**
 *
 * @author drew
 */
public class Courier implements DutchBidder, EnglishBidder, Steppable
{

    private double profit;
    public NodePackage myPackages;
    private boolean isGlobal;
    // map the edges to costs
    private HashMap<NodeKey, Double> myNetwork;
    private List<Node> sourceNode;
    private double policy;

    public Courier(boolean isGlobal)
    {
        myNetwork = new HashMap<>();
        sourceNode = new ArrayList<>();
        myPackages = new NodePackage();
        this.isGlobal = isGlobal;
    }

    public void insertKeyValPair(NodeKey k, double weight)
    {
        myNetwork.put(k, weight);
    }

    public void randInit(List<Node> nodeChoice, CourierWorld state, Node hubNode)
    {
        // Set up the couriers
        // generate a random fully connected graph
        for (Node n : nodeChoice)
        {
            for (Node m : nodeChoice)
            {
                if (!n.equals(m))
                {
                    double randWeight = state.minWeight + state.random.nextDouble() * (state.maxWeight - state.minWeight);
                    insertKeyValPair(new NodeKey(n, m), randWeight);
                }
            }
        }


        if (!isGlobal)
        {
            // must also connect to the global hub
            for (Node n : nodeChoice)
            {
                double randWeight = state.minWeight + state.random.nextDouble() * (state.maxWeight - state.minWeight);
                insertKeyValPair(new NodeKey(n, hubNode), randWeight);
            }

            // loop over the nodes that the courier occupies
            for (Node n : getSourceNodes())
            {
                Node nextNode = n, prevNode = n;
                while (nextNode != hubNode)
                {
                    nextNode = nodeChoice.get(state.random.nextInt(nodeChoice.size()));
                    double randWeight = state.minViableWeight + state.random.nextDouble() * (state.maxWeight - state.minViableWeight);
                    getMap().put(new NodeKey(prevNode, nextNode), randWeight);
                }
            }
        } else
        {
            double randWeight = state.minViableWeight + state.random.nextDouble() * (state.maxWeight - state.minViableWeight);

            insertKeyValPair(new NodeKey(nodeChoice.get(state.random.nextInt(nodeChoice.size())), nodeChoice.get(state.random.nextInt(nodeChoice.size()))), randWeight);
        }
    }

    @Override
    public boolean getDutchBid(double val, Item item)
    {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void doTransaction(double charge, Item item)
    {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getEnglishBid(Item item, int val, EnglishBidder winner)
    {
        //To change body of generated methods, choose Tools | Templates.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void step(SimState state)
    {

        // use 1 since after packagegenerator
        state.schedule.scheduleOnce(this, 1);
    }

    public HashMap<NodeKey, Double> getMap()
    {
        return myNetwork;
    }

    public void addSource(Node userNode)
    {
        sourceNode.add(userNode);
    }

    public List<Node> getSourceNodes()
    {
        return sourceNode;
    }

    public double getSuccessRate()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void recievePackage(Packages packages, double fee)
    {
        myPackages.add(packages);
        profit += fee; //TODO
    }

    public double getQuote(Packages nodePackage)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendPackageToBroker(List<Broker> brokers)
    {
        if (!myPackages.isEmpty())
        {
            Broker bestBroker = null;
            double bestQuote = -1;

            // for each of the brokers get a quote
            for (Broker b : brokers)
            {
                double quote = b.getQuote(myPackages);
                double succRate = b.getDefaultRate();

                if (bestQuote == -1)
                {
                    bestQuote = policy * quote * (1 - succRate) + (1 - policy) * quote;
                    bestBroker = b;
                } else if (bestQuote > policy * quote * (1 - succRate) + (1 - policy) * quote)
                {
                    bestQuote = policy * quote * (1 - succRate) + (1 - policy) * quote;
                    bestBroker = b;
                }
            }

            // give teh broker his fee and the packages
            if (bestBroker != null)
            {
                bestBroker.addPackage(myPackages, bestQuote / (1.0 - policy + policy * (1 - bestBroker.getDefaultRate())));
                // i have to reset myPackages I have given them to the broker
                myPackages = new NodePackage();
            }
        }
    }
}
