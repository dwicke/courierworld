/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import sim.courierworld.model.IArbiter;
import sim.courierworld.model.IBroker;
import sim.courierworld.model.IMarket;
import sim.courierworld.model.impl.Arbiter;
import sim.courierworld.model.impl.Broker;
import sim.courierworld.model.impl.Market;
import sim.courierworld.model.impl.MyRandomSequence;
import sim.courierworld.model.impl.MySequence;
import sim.courierworld.model.impl.RandomBroker;
import sim.courierworld.model.impl.Unit;
import sim.engine.ParallelSequence;
import sim.engine.RandomSequence;
import sim.engine.Sequence;
import sim.engine.SimState;
import static sim.engine.SimState.doLoop;
import sim.engine.Steppable;
import sim.util.Bag;

/**
 *
 * @author drew
 */
public class SimpleCourierWorld extends SimState implements Steppable {

    /**
     * the number of brokers that will ever exist.
     */
    private int numbrokers;
    
    /**
     * The brokers that set the price that they will accept the unit at
     * and pay to market to see that it is delivered.
     */
    private Bag brokers = new Bag();
    /**
     * The arbiter that decides which broker to pay to deliver the package
     */
    private IArbiter arbiter = new Arbiter();
    /**
     * the market that decides the min price to deliver a particular unit
     */
    private IMarket market = new Market();
    /**
     * the package to deliver
     */
    private Unit unit;
    /**
     * the broker that the arbiter has chosen
     */
    private IBroker unitBroker;

    public int getNumbrokers() {
        return numbrokers;
    }

    public void setNumbrokers(int numbrokers) {
        this.numbrokers = numbrokers;
    }

    public Bag getBrokers() {
        return brokers;
    }

    public void setBrokers(Bag brokers) {
        this.brokers = brokers;
    }

    public IArbiter getArbiter() {
        return arbiter;
    }

    public void setArbiter(IArbiter arbiter) {
        this.arbiter = arbiter;
    }

    public IMarket getMarket() {
        return market;
    }

    public void setMarket(IMarket market) {
        this.market = market;
    }

    public IBroker getUnitBroker() {
        return unitBroker;
    }

    public void setUnitBroker(IBroker unitBroker) {
        this.unitBroker = unitBroker;
    }
    
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    public SimpleCourierWorld(long seed) {
        super(seed);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        doLoop(SimpleCourierWorld.class, args);
        System.exit(0);
    }

    @Override
    public void step(SimState state) {
        System.err.println("Creating a Unit");
        // create a unit
        if (getUnit() == null)
            setUnit(new Unit());
        
        
    }
    
    @Override
    public void start() {
        super.start();
        numbrokers = 3;
        System.out.println("Starting...\n" + numbrokers + " brokers");
        // create the brokers
        for (int i = 0; i < numbrokers; i++) {
            IBroker b = new RandomBroker();
            b.setID(i);
            brokers.add(b);
        }
        
        
        // set up the sequence of steppable
        // first step this to create the unit
        List steps = new ArrayList();
        // create the unit
        steps.add(this);
        
        List parallelSteps = new ArrayList();
        // step the brokers to set their quotes for this unit
        for (int i = 0; i < numbrokers; i++)
        {
            final IBroker broker = (IBroker) brokers.objs[i];
            // schedule the brokers to set their quotes for taking the packages
            parallelSteps.add(new Steppable() {

                @Override
                public void step(SimState state) {
                    broker.setQuote((SimpleCourierWorld)state);
                }
            });
            
        }
        steps.add(new MyRandomSequence(parallelSteps));
        
        // call the arbiter that sets the unit broker
        steps.add(new Arbiter());
        // call the setDeliveryRate on the unit broker
        steps.add(new Steppable() {

            @Override
            public void step(SimState state) {
               ((SimpleCourierWorld)state).unitBroker.setDeliveryRate((SimpleCourierWorld)state);
            }
        });
        
        // call the market
        steps.add(new Market());
        // set the schedule
        schedule.scheduleOnce(new MySequence(steps));
    }
}
