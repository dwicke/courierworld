/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author drew
 */
public class Warehouse {

    public int getTotalNumPacks()
    {
        int counter = 0;
        for (Entry<Key, Integer> en : mapping.entrySet())
        {
            counter += en.getValue();
        }
        return counter;
    }
    
    public enum Priority
    {

        IMPORTANT(0.9),
        EXPRESS(0.8),
        NORMAL(0.7);
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
    
   
    public void updateStack(Node dest, Priority priority, int numPacks)
    {
        updateStack(new Key(dest, priority), numPacks);
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
     * Decays all of the packages in this.
     */
    public int decayStacks(Warehouse lostPackages)
    {
        int decayed  = 0 ;
        for(Entry<Key, Integer> ent : mapping.entrySet())
        {
            decayed = ent.getValue() - (int)(ent.getValue() * ent.getKey().priority.decayRate);
            ent.setValue((int) (ent.getValue() * ent.getKey().priority.decayRate));
            lostPackages.updateStack(ent.getKey(), decayed);
        }
        return decayed;
    }
    
    /**
     * Gets the number of packages that correspond to the key
     * if key is not in this return -1.
     * @param key
     * @return 
     */
    public int getNumPacks(Key key)
    {
        if (mapping.containsKey(key))
            return mapping.get(key);
        else
            return -1;
    }
    
    public void clear()
    {
        mapping.clear();
    }
    public boolean hasStack() {
        return !(mapping.isEmpty());
    }
    
    public Entry<Key, Integer> getPackage()
    {
        return mapping.entrySet().iterator().next();
    }
    public void addAll(Warehouse myPackages) {
        for (Entry<Key, Integer> en : myPackages.mapping.entrySet())
        {
            updateStack(en.getKey(), en.getValue());
        }
    }
    
    public Iterator<Entry<Key, Integer> > getIterator()
    {
        return mapping.entrySet().iterator();
    }
}
