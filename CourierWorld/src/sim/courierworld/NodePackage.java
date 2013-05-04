/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Package generator generates
 *
 * @author drew
 */
public class NodePackage {

    PriorityGroups priorityGroups[];

    public NodePackage() {
        priorityGroups = new PriorityGroups[3];
    }

    public void add(Packages packages) {
        priorityGroups[packages.priority.ordinal()].addPackage(packages);
    }

    public boolean isEmpty() {
        for (int i = 0; i < priorityGroups.length; i++) {
            if (!priorityGroups[i].packages.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public void addAll(NodePackage myPackages) {
        for (int i = 0; i < myPackages.priorityGroups.length; i++) {
            priorityGroups[i].combine(myPackages.priorityGroups[i].packages);
        }
    }

    public void decay() {
        for (int i = 0; i < priorityGroups.length; i++) {
            priorityGroups[i].decay();
        }
    }

    public class PriorityGroups {

        HashMap<Node, Packages> packages = new HashMap<>();

        public void addPackage(Packages pack) {
            packages.put(pack.destNode, pack.stack(packages.get(pack.destNode)));
        }
        
        public void combine(HashMap<Node, Packages> packs)
        {
            for (Entry<Node, Packages> en : packs.entrySet())
            {
                addPackage(en.getValue());
            }
        }
        
        public void decay()
        {
            for(Entry<Node, Packages> next : packages.entrySet())
            {
                next.getValue().decay();// decrease the number of packages.
            }
        }
        
    }
}
