/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld.model.impl;

import sim.courierworld.SimpleCourierWorld;

/**
 *
 * @author drew
 */
public class LearningBroker extends Broker {
    
    
    @Override
    public void setQuote(SimpleCourierWorld state) {
        super.setQuote(state);
        
        
    }

    @Override
    public void setDeliveryRate(SimpleCourierWorld state) {
        super.setDeliveryRate(state);
        
    }
    
    @Override
    public void unitSold() {
        super.unitSold();
    }

    @Override
    public void notSold() {
        super.notSold();
    }
}
