
package sim.courierworld.model.impl;

import sim.courierworld.SimpleCourierWorld;

/**
 *
 * @author drew
 */
public class RandomBroker extends Broker{
    
    @Override
    public void setQuote(SimpleCourierWorld state) {
        setQuote(state.random.nextInt(21));
        super.setQuote(state);
    }

    @Override
    public void setDeliveryRate(SimpleCourierWorld state) {
        setDeliveryRate(state.random.nextInt(getQuote() + 1));
        super.setQuote(state);
    }
}
