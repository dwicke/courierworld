/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

import java.util.HashMap;

/**
 * Package generator generates
 *
 * @author drew
 */
public class NodePackage
{

    PriorityGroups priorityGroups[];

    public NodePackage()
    {
        priorityGroups = new PriorityGroups[3];
    }

    public void add(Packages packages)
    {
        priorityGroups[packages.priority.ordinal()].addPackage(packages);
    }

    public class PriorityGroups
    {

        HashMap<Node, Packages> packages = new HashMap<>();

        public void addPackage(Packages pack)
        {
            packages.put(pack.destNode, pack.stack(packages.get(pack.destNode)));
        }
    }
}
