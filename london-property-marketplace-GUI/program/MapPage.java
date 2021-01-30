import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import javax.imageio.ImageIO;
import java.util.ArrayList;

import java.util.Random;
/**
 * MapPage class, responsible for the creation and calculation of the map and listings. - Panel 2.
 *
 * @version 1
 */
public class MapPage
{
    private ArrayList<Area> areas;
    private int minPrice;
    private int maxPrice;
    private ArrayList<AirbnbListing> data;
    private ArrayList<AirbnbListing> availableProps;
    
    int areaListings = 0;
    /**
     * Constructor for objects of class MapPage
     * 
     * @param areas An array of the neighbourhoods in London
     */
    public MapPage(ArrayList<Area> areas)
    {
        this.areas = areas;
    }

    /**
     * Builds the container that holds the data for the Map Screen (Panel 2)
     *
     * @param minPriceValue The minimum price selected in the minimum price combo box
     * @param maxPriceValue The maximum price selected in the maximum price combo box
     * @param data The arraylist of data
     * @return a JPanel containing the map
     */
    public JPanel makeMapPage(int minPriceValue, int maxPriceValue, ArrayList<AirbnbListing> data)
    {
        minPrice = minPriceValue;
        maxPrice = maxPriceValue;
        this.data = data;

        //map container
        JPanel mapContainer = new JPanel();
        mapContainer.setLayout(new BorderLayout());
        mapContainer.setBackground(Color.WHITE);
        //colour

        //title container
        JPanel titleContainer = new JPanel();
        mapContainer.add(titleContainer, BorderLayout.NORTH);
        titleContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        //contents
        JLabel mapLabel = new JLabel("Showing properties between £" + minPrice + " - £" + maxPrice, SwingConstants.CENTER);
        mapLabel.setFont(new Font("Century Gothic", Font.BOLD, 1));
        mapLabel.setFont(mapLabel.getFont().deriveFont(28f)); 
        titleContainer.add(mapLabel);

        setButtons(mapContainer);

        return mapContainer;
    }

    /**
     * creates a marker for the map.
     * creates a button, sets its image and removes its default background
     * 
     * @param width The width of the button to be created
     */
    public JButton createButton(int width)
    {
        JButton button = new JButton();
        try {
            button.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/Space boi.png")).getScaledInstance(width, width*3, Image.SCALE_SMOOTH)));
            button.setSize(width, width*3);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return button;
    }

    /**
     * creates a popup window, to display more information about the
     * listings in a certain neighbourhood.
     * 
     * @param area The area to dsiplay information for
     * @param dataset The data that the popup will use
     */
    public void createMapPopup(Area area, ArrayList<AirbnbListing> dataset)
    {
        MapPopUp a = new MapPopUp(area, availableProps);
    }    

    /**
     * Creates the buttons, creates the map, scales the buttons
     * and places the buttons in each neighbourhood.
     * 
     * @param container The container to add the map to
     */
    public void setButtons(Container container)
    {
        Container c = new Container();
        c.setLayout(null);
        availableProps = new ArrayList<>();
        for(AirbnbListing listing : data) {
            if(listing.getPrice() <= maxPrice && listing.getPrice() >= minPrice) {
                availableProps.add(listing);
            }
        }

        for (Area area : areas) {
            JButton button = createButton((int)scaleButton(area, availableProps));
            button.setLocation((int)(area.getX() - (button.getWidth()/2)), (int)(area.getY() - (button.getHeight())));
         
            button.addActionListener(e -> createMapPopup(area, data));
            area.setButton(button);
            if (areaListings > 0) {
                c.add(button);
            }
        }

        ImageIcon mapIcon = new ImageIcon("images/map.jpg");
        Image image = mapIcon.getImage();
        Image newimg = image.getScaledInstance(1040, 821, java.awt.Image.SCALE_SMOOTH);
        mapIcon = new ImageIcon(newimg);
        JButton map = new JButton(mapIcon);
        map.setSize(1040,821);
        map.setLocation(0,0);
        map.addActionListener(e -> toggleVisible());
        map.setRolloverEnabled(false);
        c.add(map);

        container.add(c);
    }

    /**
     * reverses the visibility of the markers on the map
     */
    public void toggleVisible()
    {
        for (Area area : areas) {
            area.getButton().setVisible(!(area.getButton().isVisible()));
        }
    }

    /**
     * scales the marker for an area, using the area and the data.
     * 
     * @param area The area of the marker
     * @param data The data to use for scaling
     */
    public double scaleButton(Area area, ArrayList<AirbnbListing> data) {
        double min = 15;
        double max = 50;

        areaListings = 0;

        for (AirbnbListing listing : data) {
            if (listing.getNeighbourhood().equals(area.getName())) {
                areaListings++;
            }
        }

        double scaleFactor = (double)((areaListings*500*1.5)/data.size());

        if (scaleFactor < min) {
            return min;
        } else if (scaleFactor > max) {
            return max;
        } else {
            return scaleFactor;
        }
    }
}