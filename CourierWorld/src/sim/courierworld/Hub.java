/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import sim.broker.Broker;
import sim.broker.BrokerWithAuction;
import sim.broker.BrokerWithoutAuction;
import sim.broker.BrokerWithoutAuctionU;
import sim.courier.Courier;
import sim.field.grid.Grid2D;
import sim.field.grid.SparseGrid2D;
import sim.field.network.Network;
import sim.portrayal.Inspector;
import sim.portrayal.SimpleInspector;
import sim.user.User;
import sim.util.Bag;

/**
 *
 * @author indranil
 */
public class Hub {

    public CourierWorld state;
    public List<Courier> localCouriers;// the couriers in the local substrate of this hub
    public List<Broker> brokers; // the local brokers for this hub
    public List<Node> localNodes; // local nodes that branch out from this hub
    public Node myNode;
    public ArrayList<Double> profitShare = new ArrayList<>();

    public List<Broker> getBrokers() {
        return brokers;
    }

    public List<Courier> getLocalCouriers() {
        return localCouriers;
    }

    public ArrayList<Double> getProfitShare() {
        return profitShare;
    }

    
    Hub(CourierWorld state) {
        this.state = state;
    }

    public void setup(int useIndex) {
        boolean isAdded = false;
        int randx = 0, randy = 0;

        brokers = new ArrayList<>();
        brokers.add(new BrokerWithAuction(this));
        brokers.add(new BrokerWithAuction(this));
        brokers.add(new BrokerWithoutAuction(this));
        brokers.add(new BrokerWithoutAuctionU(this));

        localCouriers = new ArrayList<>();
        for (int i = 0; i < state.maxNumCouriersPerHub; i++) {
            localCouriers.add(new Courier(false));
        }
        for(int i = 0; i < brokers.size(); i++){
            profitShare.add(1.0/brokers.size());
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
        myNode = hubNode;
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

        int courierCount = 0;
        localNodes = new ArrayList<>();
        for (int i = 0; i < state.numLocalNode; i++) {
            if (state.random.nextDouble() < probGenNode) {
                User user = new User(state.numMaxPkgs, useIndex, hubNode, state.maxPolicyVal * state.random.nextDouble(), state.randGivePack);
                Node userNode = new Node(user);

                int nodex = -1, nodey = -1;

                while (!(nodex >= 0 && nodex < state.grid.getWidth() && nodey >= 0 && nodey < state.grid.getHeight())) {
                    nodex = randx + (int) ((1.0 - 2.0 * state.random.nextDouble()) * state.localCliqueSize);
                    nodey = randy + (int) ((1.0 - 2.0 * state.random.nextDouble()) * state.localCliqueSize);

                    if (!(state.grid.getObjectsAtLocation(nodex, nodey) == null)) {
                        nodex = -1;//to look for another location
                    }
                }
                // added local node to grid
                state.grid.setObjectLocation(userNode, nodex, nodey);

                // now add the couriers that service that user
                if (courierCount >= state.maxNumCouriersPerHub) {
                    // add random number of couriers to the userNode
                    int numCouriers = state.random.nextInt(state.maxNumCouriersPerNode - state.minNumCouriersPerNode) + state.minNumCouriersPerNode;
                    // shuffle the couriers indices
                   

                    for (int j = cours.length - 1; j >= 0; j--) {
                        int index = state.random.nextInt(j+1);
                        // swap
                        int a = cours[index];
                        cours[index] = cours[j];
                        cours[j] = a;
                    }
                    

                    for (int j = 0; j < numCouriers; j++) {

                        userNode.addCourier(localCouriers.get(cours[j]));
                        localCouriers.get(cours[j]).addSource(userNode);
                    }

                } else {
                    // add the couriers to the node
                    //System.err.print(state.maxNumCouriersPerNode - state.minNumCouriersPerNode);
                    int numCouriers = state.random.nextInt(state.maxNumCouriersPerNode - state.minNumCouriersPerNode) + state.minNumCouriersPerNode;
                    int j;
                    for (j = 0; j < numCouriers; j++) {
                        userNode.addCourier(localCouriers.get(cours[j]));
                        localCouriers.get(courierCount++).addSource(userNode);
                        if (courierCount >= state.maxNumCouriersPerHub) {
                            break;
                        }
                    }

                    numCouriers -= j;


                    for (int k = 0; k < numCouriers; k++) {
                        userNode.addCourier(localCouriers.get(k));
                        localCouriers.get(k).addSource(userNode);
                    }
                }

                localNodes.add(userNode);
                useIndex++;
            }
        }

        // Set up the couriers for the local clique
        for (Courier curCour : localCouriers) {
            curCour.randInit(localNodes, state, hubNode);
        }
    }
}
