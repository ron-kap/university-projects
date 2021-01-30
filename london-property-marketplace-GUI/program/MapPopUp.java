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
 * Opens a new window to display properties in selected neighbourhood.
 * The properties can be sorted based on Number of Reviews, Price, and Host Name.
 * This can be done both smallest to highest and highest to smallest.
 *
 * @version 1
 */
public class MapPopUp
{
    //Sort Options
    final static String NUMBERREVIEWS = "Number of Reviews";
    final static String PRICE = "Price";
    final static String HOST = "Host Name";
    final static String HIGHEST = "Highest / A to Z";
    final static String LOWEST = "Lowest / Z to A";
    
    private Area selectedArea;
    private ArrayList<AirbnbListing> areaListings;

    private JFrame frame;
    private Container contentPane;
    private Container sortPane;
    private Container listPane;

    /**
     * Constructor for objects of class PopupWindow
     * @param area The chosen neighbourhood
     * @param data The available dataset
     */
    public MapPopUp(Area area, ArrayList<AirbnbListing> data)
    {
        selectedArea = area;
        areaListings = data;
        frame = new JFrame(selectedArea.getName());
        contentPane = frame.getContentPane();
        makeList();
        makePopup();
    }
    
    /**
     * Sorts the data for listings that are in the selected area
     */
    private void makeList()
    {
        ArrayList<AirbnbListing> newList = new ArrayList<>();
        for(AirbnbListing listing: areaListings){
            if(listing.getNeighbourhood().equals(selectedArea.getName())) {
                newList.add(listing);   //Filters data for properties in chosen neighbourhood
            }
        }
        areaListings = newList;
        sortByReviews(NUMBERREVIEWS);   //Default is filter by reviews 
    }
    
    /**
     * Initial frame creator
     */
    public void makePopup()
    {
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());

        sortPane = makeSortPane();
        contentPane.add(sortPane, BorderLayout.PAGE_START);
        
        listPane = populateGrid();
        contentPane.add(listPane, BorderLayout.CENTER);

        contentPane.setBackground(Color.WHITE);
        frame.setSize(1200, 600);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    /**
     * Creates sorting boxes to be placed into frame
     */
    private Container makeSortPane() 
    {
        Container newSortPane = new Container();
        newSortPane.setLayout(new FlowLayout());

        //Sorting Criteria ComboBox
        JComboBox sortBox = new JComboBox();
        sortBox.addItem(NUMBERREVIEWS);
        sortBox.addItem(PRICE);
        sortBox.addItem(HOST);
        
        //Order ComboBox
        JComboBox orderBox = new JComboBox();
        orderBox.addItem(HIGHEST);
        orderBox.addItem(LOWEST);
        
        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> changeList(sortBox, orderBox));
        
        newSortPane.add(sortBox);
        newSortPane.add(orderBox);
        newSortPane.add(sortButton);
        return newSortPane;
    }
    
    /**
     * Creates table of values with each property in selected area
     */
    private Container populateGrid()
    {
        Container listPane = new Container();
        listPane.setLayout(new GridLayout(0, 6));
        JScrollPane scrollPane = new JScrollPane(listPane);
        scrollPane.setViewportView(listPane);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20); //Increase scroll speed
        
        //Create and add headers
        JLabel hostIdTop = new JLabel("Host ID", JLabel.CENTER);
        JLabel hostNameTop = new JLabel("Host Name", JLabel.CENTER);
        JLabel priceTop = new JLabel("Price", JLabel.CENTER);
        JLabel noOfReviewsTop = new JLabel("Number of Reviews", JLabel.CENTER);
        JLabel minNightsTop = new JLabel("Minimum Nights", JLabel.CENTER);
        JLabel moreInfoTop = new JLabel("");
        
        listPane.add(hostIdTop);
        listPane.add(hostNameTop);
        listPane.add(priceTop);
        listPane.add(noOfReviewsTop);
        listPane.add(minNightsTop);
        listPane.add(moreInfoTop);
        
        for(AirbnbListing listing: areaListings){
            //Create and add listings
            JLabel hostId = new JLabel(listing.getHost_id(), JLabel.CENTER);
            JLabel hostName = new JLabel(listing.getHost_name(), JLabel.CENTER);
            JLabel price = new JLabel("£" + listing.getPrice(), JLabel.CENTER);
            JLabel noOfReviews = new JLabel("" + listing.getNumberOfReviews(), JLabel.CENTER);
            JLabel minNights = new JLabel("" + listing.getMinimumNights(), JLabel.CENTER);
            JButton moreInfo = new JButton("More Info");
            moreInfo.addActionListener(e -> new DescriptionBox(listing));

            listPane.add(hostId);
            listPane.add(hostName);
            listPane.add(price);
            listPane.add(noOfReviews);
            listPane.add(minNights);
            listPane.add(moreInfo);
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

    /**
     * Referenced by the button and passes along to sorting methods based on the
     * current value of the first ComboBox
     * @param combo The sort combo box 
     * @param displayBox The order combo box
     */
    public void changeList(JComboBox combo, JComboBox displayBox) 
    {
        String displayType = displayBox.getSelectedItem().toString();
        if(combo.getSelectedItem().equals(NUMBERREVIEWS)) {
            sortByReviews(displayType);
        }
        else if(combo.getSelectedItem().equals(PRICE)) {
            sortByPrice(displayType);
        }
        else if(combo.getSelectedItem().equals(HOST)) {
            sortByName(displayType);
        }
        updatePopup();
    }
    
    /**
     * Sorts each property in areaListings by Number of Reviews
     * @param displayType The order of sorting
     */
    private void sortByReviews(String displayType)
    {
        Collections.sort(areaListings, new Comparator<AirbnbListing>()
            {
                public int compare(AirbnbListing listing1, AirbnbListing listing2) {
                    int noReviews1 = listing1.getNumberOfReviews();
                    int noReviews2 = listing2.getNumberOfReviews();
                    if(displayType.equals(LOWEST)) {
                        return noReviews1 - noReviews2; //If "Lowest / A to Z" is chosen
                    }
                    return noReviews2 - noReviews1;
                }
            }
        );
    }

    /**
     * Sorts each property in areaListings by Price
     * @param displayType The order of sorting
     */
    private void sortByPrice(String displayType)
    {
        Collections.sort(areaListings, new Comparator<AirbnbListing>()
            {
                public int compare(AirbnbListing listing1, AirbnbListing listing2) {
                    int price1 = listing1.getPrice();
                    int price2 = listing2.getPrice();
                    if(displayType.equals(HIGHEST)) {
                        return price2 - price1; //If "Highest / Z to A" is chosen
                    }
                    return price1 - price2;
                }
            }
        );
    }
    
    /**
     * Sorts each property in areaListings by Host Name
     * @param displayType The order of sorting
     */
    private void sortByName(String displayType)
    {
        Collections.sort(areaListings, new Comparator<AirbnbListing>()
            {
                public int compare(AirbnbListing listing1, AirbnbListing listing2) {
                    String name1 = listing1.getHost_name();
                    String name2 = listing2.getHost_name();
                    if(displayType.equals(HIGHEST)) {
                        return name1.compareTo(name2); //If "Highest / Z to A" is chosen
                    }
                    return name2.compareTo(name1);
                }
            }
        );
    }
}
