package sim.courierworld;

import sim.courierworld.Node;

public class NodeKey {
        // unordered collection of two nodes that have an undirected edge between
        public Node nodeA, nodeB;

        public NodeKey(Node n, Node m) {
            this.nodeA = n;
            this.nodeB = m;
        }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NodeKey))
            return false;
        NodeKey other = (NodeKey) obj;
        // if this stores the same instances as the other NodeKey object then equivalent
        if ((other.nodeA == nodeA && other.nodeB == nodeB) || (other.nodeA == nodeB && other.nodeB == nodeA))
        {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // same idea as equals
        return nodeA.hashCode() + nodeB.hashCode();
    }
        
        
        
    }