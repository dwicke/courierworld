/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

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

    public class PriorityGroups {
        // always starting in a hub since that is where the package was generated
        Packages packages[];

        public class Packages {

            int destNode;// where I need to go
            int numberPacks;// number of packages
            int initNumberPacks;
            double decayrate;
        }
    }

    public enum Priority {

        IMPORTANT,
        EXPRESS,
        NORMAL
    }
}

