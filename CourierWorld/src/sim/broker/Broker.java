/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.broker;

import sim.courierworld.NodePackage;

/**
 *
 * @author indranil
 */
public interface Broker {

    public double getQuote(NodePackage myPackages);

    public double getDefaultRate();

    public int getSuccessRate();

    public void addPackage(NodePackage myPackages, double d);
    
}
