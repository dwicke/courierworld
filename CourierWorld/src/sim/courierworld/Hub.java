/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.util.List;
import sim.broker.Broker;
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

    

    Hub(CourierWorld state, int useIndex) {
        boolean isAdded = false;
        int randx = 0, randy = 0;
        
        // first we generate an (x,y) coordinate for the hub
        // ensuring that it is the correct distance away from other hubs
        while(!isAdded)
        {
            isAdded = true;
            randx = state.random.nextInt(state.grid.getWidth());
            randy = state.random.nextInt(state.grid.getHeight());
            
            Bag neighbr = new Bag();
            neighbr = state.grid.getMooreNeighbors(randx, randy, state.distFromHubs,Grid2D.BOUNDED, neighbr ,null,null);
            
            if(!neighbr.isEmpty())
            {
                for(int i = 0; i < neighbr.numObjs; i++)
                {
                    if(((Node)neighbr.get(i)).isHub())
                    {
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
        for (int i = 0; i < state.numLocalNode; i++)
        {
            if (state.random.nextDouble() < probGenNode)
            {
                User user = new User(state.numMaxPkgs, useIndex,hubNode);
                Node userNode = new Node(user);
                
                int nodex = -1, nodey = -1;

                while(!(nodex >= 0 && nodex < state.grid.getWidth() && nodey >= 0 && nodey < state.grid.getHeight()))
                {
                    nodex = randx  +  (int)((1.0 - 2.0*state.random.nextDouble()) * state.localCliqueSize);
                    nodey = randy  +  (int)((1.0 - 2.0*state.random.nextDouble()) * state.localCliqueSize);
                    
                    if(!(state.grid.getObjectsAtLocation(nodex, nodey).isEmpty()))
                    {
                        nodex = -1;//to look for another location
                    }
                }
                
                state.grid.setObjectLocation(userNode, nodex, nodey);
                useIndex++;
            }        
            
            
        }
        
        
        
        
        
        
    }
    
    
}
