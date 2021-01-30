import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Opens a new window to display more information on a selected property
 * 
 * @version 1
 */
public class DescriptionBox
{
    private AirbnbListing listing;

    /**
     * Constructor for objects of class DescriptionBox
     * @param listing The chosen property listing
     */
    public DescriptionBox(AirbnbListing listing)
    {
        this.listing = listing;
        makeDescriptionBox();
    }
    
    /**
     * Creates the content pane with the property data
     */
    private void makeDescriptionBox()
    {
        JFrame frame = new JFrame(listing.getHost_name() + "'s Property");
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridLayout(0, 2));
        
        JLabel description = new JLabel("" + listing.getName());
        JLabel roomType = new JLabel("" + listing.getRoom_type());
        JLabel lastReview = new JLabel("" + listing.getLastReview());        
        JLabel hostListings = new JLabel("" + listing.getCalculatedHostListingsCount());        
        JLabel availibility = new JLabel("" + listing.getAvailability365());
        
        contentPane.add(new JLabel("Description"));
        contentPane.add(description);
        contentPane.add(new JLabel("Room Type"));
        contentPane.add(roomType);
        contentPane.add(new JLabel("Date of Last Review"));
        contentPane.add(lastReview);
        contentPane.add(new JLabel("Total Host Listings"));
        contentPane.add(hostListings);
        contentPane.add(new JLabel("Days Available in Year"));
        contentPane.add(availibility);
        
        frame.pack();
        frame.setVisible(true);
    }
}
