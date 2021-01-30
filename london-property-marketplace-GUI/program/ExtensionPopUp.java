import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * Opens a new window to display crimes in selected zone.
 *
 * @version 1
 */
public class ExtensionPopUp
{
    private String selectedZone;
    
    private ArrayList<MetPoliceListing> zoneListings;

    private JFrame frame;
    private Container contentPane;
    private Container sortPane;
    private Container listPane;

    /**
     * Constructor for objects of class PopupWindow
     * @param zone The selected zone of map
     * @param data The list of MetPoliceListings
     */
    public ExtensionPopUp(String zone, ArrayList<MetPoliceListing> data)
    {
        selectedZone = zone;
        zoneListings = data;
        frame = new JFrame("Zone " + selectedZone);
        contentPane = frame.getContentPane();
        makeList();
        makePopup();
    }
    
    /**
     * Sorts the data for crimes that are in the selected zone
     */
    private void makeList()
    {
        ArrayList<MetPoliceListing> newList = new ArrayList<>();
        for(MetPoliceListing listing: zoneListings){
            if(listing.getZone().equals(selectedZone)) {
                newList.add(listing);   //Filters data for properties in chosen neighbourhood
            }
        }
        zoneListings = newList;
    }
    
    /**
     * Initial frame creator
     */
    public void makePopup()
    {
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        
        listPane = populateGrid();
        contentPane.add(listPane, BorderLayout.CENTER);

        contentPane.setBackground(Color.WHITE);
        frame.setSize(909, 600);
        frame.setVisible(true);
    }
    
    /**
     * Creates table of values with each crime in selected zone
     */
    private Container populateGrid()
    {
        Container listPane = new Container();
        listPane.setLayout(new GridLayout(0, 4));
        JScrollPane scrollPane = new JScrollPane(listPane);
        scrollPane.setViewportView(listPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20); //Increase scroll speed

        //Create and add headers
        JLabel idTop = new JLabel("Crime ID", JLabel.CENTER);
        JLabel zoneTop = new JLabel("Zone", JLabel.CENTER);
        JLabel typeTop = new JLabel("Type of Crime", JLabel.CENTER);
        JLabel dateTop = new JLabel("Date", JLabel.CENTER);
        
        listPane.add(idTop);
        listPane.add(zoneTop);
        listPane.add(typeTop);
        listPane.add(dateTop);
        
        for(MetPoliceListing listing: zoneListings){
            //Create and add listings
            JLabel id = new JLabel(listing.getId(), JLabel.CENTER);
            JLabel zone = new JLabel(listing.getZone(), JLabel.CENTER);
            JLabel type = new JLabel(listing.getTypeOfCrime(), JLabel.CENTER);
            JLabel date = new JLabel(listing.getDate(), JLabel.CENTER);

            listPane.add(id);
            listPane.add(zone);
            listPane.add(type);
            listPane.add(date);
        }
        return scrollPane;
    }
    
    /**
     * Creates a new table with a new sorted list and removes the old one
     */
    public void updatePopup()
    {
        contentPane.remove(listPane);

        listPane = populateGrid();  
        contentPane.add(listPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

}