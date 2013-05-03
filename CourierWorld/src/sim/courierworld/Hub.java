/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import sim.broker.Broker;
import sim.broker.MaxDeliveredBroker;
import sim.broker.MaxProfitBroker;
import sim.courier.Courier;
import sim.field.grid.Grid2D;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.user.User;
import sim.util.Bag;

/**
 *
 * @author indranil
 */
public class Hub {

    public List<Courier> globalCouriers;// the global couriers that occupy this hub
    public List<Courier> localCouriers;// the couriers in the local substrate of this hub
    public List<Broker> brokers; // the local brokers for this hub
    public List<Node> localNodes; // local nodes that branch out from this hub

    Hub(CourierWorld state, int useIndex) {
        boolean isAdded = false;
        int randx = 0, randy = 0;

        brokers = new ArrayList<>();
        brokers.add(new MaxDeliveredBroker());
        brokers.add(new MaxProfitBroker());

        localCouriers = new ArrayList<>();
        for (int i = 0; i < state.maxNumCouriersPerHub; i++) {
            localCouriers.add(new Courier(false));
        }






        // first we generate an (x,y) coordinate for the hub
        // ensuring that it is the correct distance away from other hubs
        while (!isAdded) {
            isAdded = true;
            randx = state.random.nextInt(state.grid.getWidth());
            randy = state.random.nextInt(state.grid.getHeight());

            Bag neighbr = new Bag();
            neighbr = state.grid.getMooreNeighbors(randx, randy, state.distFromHubs, Grid2D.BOUNDED, neighbr, null, null);

            if (!neighbr.isEmpty()) {
                for (int i = 0; i < neighbr.numObjs; i++) {
                    if (((Node) neighbr.get(i)).isHub()) {
                        isAdded = false;
                        break;
                    }
                }
            }
        }
        // we create a node to hold this (the network is filled only user nodes
        // and hub nodes)
        Node hubNode = new Node(this);
        // add the hub to the grid and the network
        state.grid.setObjectLocation(hubNode, randx, randy);


        // Now we are going to create the local cliques
        // specific to the hub.  These nodes are user nodes
        // they will eventually have couriers.
        double probGenNode = 0.8;
        int cours[] = new int[state.maxNumCouriersPerHub];

        // indices into localCouriers
        for (int i = 0; i < cours.length; i++) {
            cours[i] = i;
        }

        localNodes = new ArrayList<>();
        for (int i = 0; i < state.numLocalNode; i++) {
            if (state.random.nextDouble() < probGenNode) {
                User user = new User(state.numMaxPkgs, useIndex, hubNode);
                Node userNode = new Node(user);

                int nodex = -1, nodey = -1;

                while (!(nodex >= 0 && nodex < state.grid.getWidth() && nodey >= 0 && nodey < state.grid.getHeight())) {
                    nodex = randx + (int) ((1.0 - 2.0 * state.random.nextDouble()) * state.localCliqueSize);
                    nodey = randy + (int) ((1.0 - 2.0 * state.random.nextDouble()) * state.localCliqueSize);

                    if (!(state.grid.getObjectsAtLocation(nodex, nodey).isEmpty())) {
                        nodex = -1;//to look for another location
                    }
                }
                // added local node to grid
                state.grid.setObjectLocation(userNode, nodex, nodey);

                // add random number of couriers to the userNode
                int numCouriers = state.random.nextInt(state.maxNumCouriersPerNode - state.minNumCouriersPerNode) + state.minNumCouriersPerNode;
                // shuffle the couriers indices
                for (int j = 0; j < state.maxNumCouriersPerHub; j++) {
                    int shuffle = state.random.nextInt(state.maxNumCouriersPerHub - j) + j;
                    cours[j] = cours[shuffle];
                }
                // add the couriers to the node
                for (int j = 0; j < numCouriers; j++) {
                    userNode.addCourier(localCouriers.get(cours[j]));
                    localCouriers.get(cours[j]).addSource(userNode);
                }
                localNodes.add(userNode);
                useIndex++;
            }


        }




        // Set up the couriers for the local clique
        for (Courier curCour : localCouriers) {
            
            // generate a random fully connected graph within the local nodes
            for (Node n : localNodes) {
                for (Node m : localNodes) {
                    if (!n.equals(m)) {
                        double randWeight = state.minWeight + state.random.nextDouble()*(state.maxWeight - state.minWeight);
                        curCour.insertKeyValPair(new NodeKey(n,m), randWeight);
                    }
                }
            }
            
            
            
            // must also connect to the global hub!
            for (Node n : localNodes)
            {
                double randWeight = state.minWeight + state.random.nextDouble()*(state.maxWeight - state.minWeight);
                curCour.insertKeyValPair(new NodeKey(n,hubNode), randWeight);
            }
            
            // loop over the nodes that the courier occupies
            for (Node n : curCour.getSourceNodes())
            {
                Node nextNode = n, prevNode = n;
                while (nextNode != hubNode)
                {
                    nextNode = localNodes.get(state.random.nextInt(localNodes.size()));
                    double randWeight = state.minViableWeight + state.random.nextDouble()*(state.maxWeight - state.minViableWeight);
                    curCour.getMap().put(new NodeKey(prevNode, nextNode), randWeight);
                }
            }
            
            //loop for adding global courier to the Hub
            
        }







    }
}
