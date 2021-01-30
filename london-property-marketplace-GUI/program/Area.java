import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Represents a single neighbourhood and their corresponding positions. 
 *
 * @version 1
 */
public class Area
{
    private String name;
    private double xPosition;
    private double yPosition;
    private JButton button;
    private double surfaceArea;

    /**
     * Constructor for objects of class Area
     * 
     * @param name The name of the neighbourhood
     * @param xPosition The x-coordinate of the neighbourhood on the map
     * @param yPosition The y-coordinate of the neighbourhood on the map
     * @param surfaceArea The surface area of the neighbourhood
     */
    public Area(String name, double xPosition, double yPosition, double surfaceArea)
    {
        this.name = name;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.surfaceArea = surfaceArea;
    }

    /**
     * returns the name of the neighbourhood
     * 
     * @return the name of the neighbourhood
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * returns the x-coordinate of the neighbourhood
     * 
     * @return the x-coordinate of the neighbourhood
     */
    public double getX()
    {
        return xPosition;
    }
    
    /**
     * returns the y-coordinate of the neighbourhood
     * 
     * @return the y-coordinate of the neighbourhood
     */
    public double getY()
    {
        return yPosition;
    }
    
    /**
     * assigns a button to the neighbourhood
     * 
     * @param button The button to be assigned to the neighbourhood
     */
    public void setButton(JButton button)
    {
        this.button = button;
    }
    
    /**
     * returns the button associated to the neighbourhood
     * 
     * @return the button associated to the neighbourhood
     */
    public JButton getButton()
    {
        return button;
    }
    
    /**
     * returns the surface area of the neighbourhood
     * 
     * @return the surface area of the neighbourhood
     */
    public double getSurfaceArea()
    {
        return surfaceArea;
    }
}
