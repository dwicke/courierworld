/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sim.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JFrame;
import sim.app.networktest.NetworkTest;
import sim.courierworld.CourierWorld;
import sim.courierworld.Node;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
import sim.portrayal.network.SimpleEdgePortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;
import sim.portrayal.simple.OvalPortrayal2D;

/**
 *
 * @author indranil
 */
public class CourierWorldWithUI extends GUIState {

    public Display2D display;
    public JFrame displayFrame;
    NetworkPortrayal2D edgePortrayal = new NetworkPortrayal2D();
    SparseGridPortrayal2D courierWorldPortrayal = new SparseGridPortrayal2D();

    public static void main(String[] args) {
        new CourierWorldWithUI().createController();
    }

    public CourierWorldWithUI() {
        super(new CourierWorld(System.currentTimeMillis()));

    }

    public Object getSimulationInspectedObject() {
        return state;
    }

    public CourierWorldWithUI(SimState state) {
        super(state);
    }

    public static String getName() {
        return "Courier World";
    }

    public void start() {
        super.start();
        setupPortrayals();
    }

    public void load() {
        super.start();
        setupPortrayals();
    }

    public void setupPortrayals() {
        final CourierWorld courWorld = (CourierWorld) state;

        // tell the portrayals what to portray and how to portray them
        courierWorldPortrayal.setField(courWorld.grid);
        final ArrayList<Color> colorList = new ArrayList<>();
        colorList.add(Color.red);
        colorList.add(Color.BLUE);
        colorList.add(Color.green);
        colorList.add(Color.magenta);
        colorList.add(Color.yellow);
        colorList.add(Color.cyan);
        colorList.add(Color.ORANGE);
        colorList.add(Color.pink);
        courierWorldPortrayal.setPortrayalForAll(new OvalPortrayal2D(4.0) {
            public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
                Node node = (Node) object;

                if (node.isHub()) {
                    paint = new Color(0, 0, 0);
                } else {
                    paint = colorList.get(node.getUser().getHub().hashCode() % 8);
                }
                super.draw(object, graphics, info);

            }
        });
        // tell the portrayals what to portray and how to portray them
        edgePortrayal.setField(new SpatialNetwork2D(courWorld.grid, courWorld.logisticNetwork));
        SimpleEdgePortrayal2D p = new SimpleEdgePortrayal2D(Color.lightGray, Color.lightGray, null);
        p.setShape(SimpleEdgePortrayal2D.SHAPE_THIN_LINE);
        p.setBaseWidth(3);
        
        edgePortrayal.setPortrayalForAll(p);
        // reschedule the displayer
        display.reset();
        
        // redraw the display
        display.repaint();
    }

    public void init(Controller c) {
        super.init(c);
        display = new Display2D(1000, 1000, this);
        display.setClipping(false);
        displayFrame = display.createFrame();
        displayFrame.setTitle("Can You Deliver?");
        c.registerFrame(displayFrame); // so the frame appears in the "Display" list
        displayFrame.setVisible(true);
        display.attach(edgePortrayal, "kop");
        display.attach(courierWorldPortrayal, "CourierWorld");

    }
}
