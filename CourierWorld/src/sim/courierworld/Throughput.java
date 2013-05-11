/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

/**
 *
 * @author drew
 */
public class Throughput {

    
    public int numStacks = 0;
    public int throughput = 0;
    public int time = 1;
    public String type;
    
    
    public Throughput(String type) {
        this.type = type;
    }
    
    

    public int getNumStacks() {
        return numStacks;
    }

    public int getThroughput() {
        return throughput;
    }
    

    @Override
    public String toString() {
        //To change body of generated methods, choose Tools | Templates.
        return type;
    }

    public void addStacks(int totalNumPacks, long steps) {
        numStacks += totalNumPacks;
        throughput = (int) (numStacks / (steps + 1));
    }
    
    
    
    
}
