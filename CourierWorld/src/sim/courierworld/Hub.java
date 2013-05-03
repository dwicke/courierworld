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

    public Hub() {
        this.globalCouriers = globalCouriers;
        this.localCouriers = localCouriers;
        this.brokers = brokers;
    }

    Hub(CourierWorld state, int useIndex) {
        boolean isAdded = false;
        int randx = 0, randy = 0;
        
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
        Node hubNode = new Node(this);
        
        state.grid.setObjectLocation(hubNode, randx, randy);
        state.world.addNode(hubNode);

        
        double probGenNode = 0.8;
        for (int i = 0; i < state.numLocalNode; i++)
        {
            if (state.random.nextDouble() < probGenNode)
            {
                User user = new User(state.numMaxPkgs, useIndex);
                Node userNode = new Node(user);
                
                int nodex = -1, nodey = -1;
                while(!(nodex >= 0 && nodex < state.grid.getWidth() && nodey >= 0 && nodey < state.grid.getHeight()))
                {
                    nodex = randx  +  (int)((1.0 - 2.0*state.random.nextDouble()) * state.localCliqueSize);
                    nodey = randy  +  (int)((1.0 - 2.0*state.random.nextDouble()) * state.localCliqueSize);
                }
                
                useIndex++;
            }
        }
        
        
    }
    
    
}
