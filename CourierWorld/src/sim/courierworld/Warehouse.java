/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author drew
 */
public class Warehouse {
    public enum Priority
    {

        IMPORTANT(0.7),
        EXPRESS(0.6),
        NORMAL(0.5);
        double decayRate;

        Priority(double decayRate)
        {
            this.decayRate = decayRate;
        }
    }
    
    public class Key {
        public Node dest;
        public Priority priority;

        public Key(Node dest, Priority priority) {
            this.dest = dest;
            this.priority = priority;
        } 

        @Override
        public boolean equals(Object obj) {
            
            if (!(obj instanceof Key))
            {
                return false;
            }
            
            Key other = (Key) obj;
            return dest == other.dest && priority.equals(other.priority);
        }

        @Override
        public int hashCode() {
            //To change body of generated methods, choose Tools | Templates.
            return super.hashCode();
        }
        
    }
    
    private HashMap<Key, Integer> mapping;

    public Warehouse() {
        this.mapping = new HashMap<>();
    }
    
    /**
     * Inserts key value pairs into this if new
     * otherwise it will add to the k the numPacks.
     * @param k
     * @param numPacks 
     */
    public void updateStack(Key k, int numPacks)
    {
        if(mapping.containsKey(k))
        {
            mapping.put(k, mapping.get(k) + numPacks);
        }
        else
        {
            mapping.put(k, numPacks);
        }
    }
    
    /**
     * Decays all of the packages in the this.
     */
    public void decayStacks()
    {
        for(Entry<Key, Integer> ent : mapping.entrySet())
        {
            ent.setValue((int) (ent.getValue() * ent.getKey().priority.decayRate));
        }
    }
    
    /**
     * Gets the number of packages that correspond to the key
     * @param key
     * @return 
     */
    public int getNumPacks(Key key)
    {
        return mapping.get(key);
    }
}
