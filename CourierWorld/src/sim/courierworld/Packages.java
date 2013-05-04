/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.courierworld;

/**
 *
 * @author indranil
 */
public class Packages
{

    public Priority priority; // the priority of the packages.
    public Node destNode;// where I need to go
    public int numberPacks;// current number of packages
    public int initNumberPacks;// when combining packages make sure to add

    public Packages(Priority priority, Node destNode, int numberPacks, int initNumberPacks)
    {
        this.priority = priority;
        this.destNode = destNode;
        this.numberPacks = numberPacks;
        this.initNumberPacks = initNumberPacks;
    }

    Packages stack(Packages otherPack)
    {
        if (otherPack != null)
        {
            this.numberPacks += otherPack.numberPacks;
            this.initNumberPacks += otherPack.initNumberPacks;
        }
        return this;
    }

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
}
