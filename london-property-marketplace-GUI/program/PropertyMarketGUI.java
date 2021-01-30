import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

/**
 * Master class for PropertyMarketGUI.
 *
 * @version 1
 */
public class PropertyMarketGUI

{
    private AirbnbDataLoader loader = new AirbnbDataLoader();
    private ArrayList<AirbnbListing> data = loader.load();
    private WelcomePage welcome;
    private MapPage map;
    private StatsPage stats;
    
    private JFrame frame;
    private JFrame statsFrame;
    
    private boolean displayPopUp = false;
    
    private JLabel minText;
    private JComboBox minPrice;
    private JLabel maxText;
    private JComboBox maxPrice;
    private boolean invalidPrice;
    
    private JButton leftButton;
    private JButton rightButton;
    
    private Container mainPane;

    private JPanel currentPage;
    private int currentPageInt;
    
    private ArrayList<Area> areas;
    
    
    /**
     * Constructor for objects of class PropertyMarketGUI
     */
    public PropertyMarketGUI()
    {
        createLocations();
        makeFrame();
    }
     /**
     * Creates the Frame for the PropertyMarketGUI 
     * and adds various AWT and Swing Components to it. 
     * Sets the current page to the welcome page. 
     */
    private void makeFrame()
    {
        //frame
        frame = new JFrame("Property Market");
        frame.setResizable(false);
        mainPane = frame.getContentPane();
        mainPane.setBackground(Color.decode("#d9d9d9"));
        mainPane.setLayout(new BorderLayout());
        
        //price comboboxes
        Container menuPane = new Container();
        menuPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        minText = new JLabel("Price range   ::   Minimum Price");
        menuPane.add(minText);
        minPrice = returnMinPriceComboBox();
        minPrice.addActionListener(e -> minValue());
        menuPane.add(minPrice);
        
        menuPane.add(minPrice);
        maxText = new JLabel("Maximum Price");
        menuPane.add(maxText);
        maxPrice = returnMaxPriceComboBox();
        maxPrice.addActionListener(e -> maxValue());
        menuPane.add(maxPrice);
        
        mainPane.add(menuPane, BorderLayout.NORTH);
        
        //buttons
        Container bottomPane = new Container();
        bottomPane.setLayout(new BorderLayout());
        
        leftButton = new JButton("<<  Previous");
        bottomPane.add(leftButton, BorderLayout.WEST);
        leftButton.addActionListener(e -> setMiddleFrame(false));
    
        rightButton = new JButton("Next   >>");
        rightButton.addActionListener(e -> setMiddleFrame(true));
        bottomPane.add(rightButton, BorderLayout.EAST);
                
        mainPane.add(bottomPane, BorderLayout.SOUTH);
        
        //middle pane
        currentPage = new JPanel();
        currentPage.setLayout(new FlowLayout(FlowLayout.RIGHT));
        
        //(initial) welcome page creation
        makeWelcome();
        rightButton.setEnabled(false);
        
        frame.setSize(1050, 975);
        frame.setVisible(true);
    }
      /**
     * Creates the Welcome page and sets it as our current centre panel.
     */
    public void makeWelcome()
    {
        mainPane.remove(currentPage);
        JPanel container = new JPanel();
        welcome = new WelcomePage();
        container = welcome.makeWelcome((int)minPrice.getSelectedItem(), (int)maxPrice.getSelectedItem());
        currentPage = container;
        currentPageInt = 1;
        leftButton.setVisible(false);
        currentPage.setBorder(new LineBorder(Color.lightGray,2));
        mainPane.add(currentPage, BorderLayout.CENTER);
    }
      /**
     * Creates the Map page and sets it as our current centre panel.
     */
    public void makeMap()
    {
        mainPane.remove(currentPage);
        JPanel container = new JPanel();
        map = new MapPage(areas);
        container = map.makeMapPage((int)minPrice.getSelectedItem(), (int)maxPrice.getSelectedItem(), data);
        displayPopUp = true;
        currentPage = container;
        leftButton.setVisible(true);
        rightButton.setEnabled(true);
        currentPageInt = 2;
        currentPage.setBorder(new LineBorder(Color.lightGray,2));
        mainPane.add(currentPage, BorderLayout.CENTER);
    }
     /**
     * Creates the Crime HeatMap page and sets it as our current centre panel.
     */
    public void makeExtension()
    {
        mainPane.remove(currentPage);
        JPanel container = new JPanel();
        ExtensionPage CENSORED = new ExtensionPage();
        container = CENSORED.makeExtension();
        currentPage = container;
        rightButton.setVisible(false);
        currentPageInt = 4;
        currentPage.setBorder(new LineBorder(Color.lightGray,2));
        mainPane.add(currentPage, BorderLayout.CENTER);
    }
     /**
     * Creates the statistics page and sets it as our current centre panel.
     */
    public void makeStats()
    {
        mainPane.remove(currentPage);
        JPanel container = new JPanel();
        stats = new StatsPage((int)minPrice.getSelectedItem(), (int)maxPrice.getSelectedItem(), data);
        container = stats.makeStatsPage((int)minPrice.getSelectedItem(), (int)maxPrice.getSelectedItem(), data, areas);
        currentPage = container;
        rightButton.setVisible(true);
        leftButton.setVisible(true);
        currentPageInt = 3;
        currentPage.setBorder(new LineBorder(Color.lightGray,2));
        mainPane.add(currentPage, BorderLayout.CENTER);
    }
     /**
     * Creates and sets welcome page.
     * Called everytime the minimum or maximum price is changed.
     */
    public void resetToWelcome()
    {
        makeWelcome();
        rightButton.setEnabled(false);
        mainPane.validate();
        frame.repaint();
    }
     /**
     * Called to change center page in centre panel.
     * Contains the logic to call the next relevant page. 
     * 
     * @param nextButton, true if the next button was pressed false otherwhise (previous button was pressed)
     */
    public void setMiddleFrame(boolean nextButton)
    {   
        if(nextButton == true) {
            switch (currentPageInt) {
                case 1: makeMap();
                        break;
                case 2: makeStats();
                        break;
                case 3: makeExtension();
                        break;
            }
        } else {
            switch (currentPageInt) {
                case 2: makeWelcome();
                        break;
                case 3: makeMap();
                        break;
                case 4: makeStats();
                        break;
            }
        }
        
        mainPane.validate();
        frame.repaint();       
    }
     /**
     * Uses an array list an collections to order all prices in ascending order.
     * 
     * @return JcomboBox containing all prices stored as Integers in ascending order 
     */
    public JComboBox returnMinPriceComboBox()
    {
       HashSet<Integer> allPricesHash = gatherPrices();
       ArrayList<Integer> sortedAllPrices = new ArrayList<>(allPricesHash);
       Collections.sort(sortedAllPrices);
       JComboBox<Integer> minComboBox = new JComboBox<Integer>();
       for(Integer price:sortedAllPrices)
       {
           minComboBox.addItem(price);
       }
       
       return minComboBox;
    }
      /**
     * Uses an array list an collections to order all prices in descending order.
     * 
     * @return JcomboBox containing all prices stored as Integers in descending order 
     */
    public JComboBox returnMaxPriceComboBox()
    {
       HashSet<Integer> allPricesHash = gatherPrices();
       ArrayList<Integer> sortedAllPrices = new ArrayList<>(allPricesHash);
       Collections.sort(sortedAllPrices,Collections.reverseOrder());
       JComboBox<Integer> maxComboBox = new JComboBox<Integer>();
       for(Integer price:sortedAllPrices)
       {
           maxComboBox.addItem(price);
       }
       
       return maxComboBox;
    }
     /**
     * Creates a warning pop-up box if selected value is invalid (min>max).
     */
    public void minValue()
    {
        Integer valueInteger =  (Integer)minPrice.getSelectedItem();
        Integer currentMaxInteger = (Integer)maxPrice.getSelectedItem();
        int valueInt = valueInteger.intValue();
        int valueMax = currentMaxInteger.intValue();
        resetToWelcome();
        
        if(valueInt>valueMax)
        {
            //alert needed as button visability is too subtle
            welcome.setPriceLabel(" ");
            JOptionPane.showMessageDialog(frame, 
                    "Please select a vaild price.",
                    "Invaild Price Range", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        rightButton.setEnabled(true);
    }
    /**
    * Creates a warning pop-up box if selected value is invalid (max<min).
    */
    public void maxValue()
    {
        Integer valueInteger =  (Integer)maxPrice.getSelectedItem();
        Integer currentMinInteger = (Integer)minPrice.getSelectedItem();
        int valueInt = valueInteger.intValue();
        int valueMin = currentMinInteger.intValue();
        resetToWelcome();
        
        if(valueInt<valueMin)
        {
            welcome.setPriceLabel(" ");
            JOptionPane.showMessageDialog(frame, 
                    "Please select a vaild price range.",
                    "Invaild Price Range", 
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        rightButton.setEnabled(true);
    }
     /**
     * @return allPrices, a Hashset containing all the possible prices from the data.
     */
     public HashSet<Integer> gatherPrices()
    {   
        HashSet<Integer> allPrices = new HashSet<>();
        for(AirbnbListing listing: data)
        {   
            Integer i = new Integer(listing.getPrice());
            if(i != null)
            {
                allPrices.add(i);
            }
        }
       
        return allPrices;
    }
    
    public void createLocations()
    {
        areas = new ArrayList();
        Area hillingdon = new Area("Hillingdon", 80, 340, 115.7);
        areas.add(hillingdon);
        Area harrow = new Area("Harrow", 180, 205, 50.47);
        areas.add(harrow);
        Area ealing = new Area("Ealing", 200, 375, 55.53);
        areas.add(ealing);
        Area hounslow = new Area("Hounslow", 150, 470, 55.98);
        areas.add(hounslow);
        Area richmond = new Area("Richmond upon Thames", 250, 510, 57.41);
        areas.add(richmond);
        Area brent = new Area("Brent", 275 ,285, 43.24);
        areas.add(brent);
        Area barnet = new Area("Barnet", 360, 170, 86.74);
        areas.add(barnet);
        Area enfield = new Area("Enfield", 500, 100, 82.21);
        areas.add(enfield);
        
        Area haringey = new Area("Haringey", 500, 225, 29.59);
        areas.add(haringey);
        Area camden = new Area("Camden", 420, 290, 21.76);
        areas.add(camden);
        Area westminster = new Area("Westminster", 445, 380, 21.48);
        areas.add(westminster);
        Area kensingtonAndChelsea = new Area("Kensington and Chelsea", 410, 430, 12.13);
        areas.add(kensingtonAndChelsea);
        Area hammersmithAndFulham = new Area("Hammersmith and Fulham", 340, 390, 16.4);
        areas.add(hammersmithAndFulham);
        
        Area walthamForest = new Area("Waltham Forest", 605, 190, 38.82);
        areas.add(walthamForest);
        Area hackney = new Area("Hackney", 555, 290, 19.06);
        areas.add(hackney);
        Area islington = new Area("Islington", 480, 300, 14.86);
        areas.add(islington);
        Area city = new Area("City of London", 512, 375, 2.90);
        areas.add(city);
        
        Area redbridge = new Area("Redbridge", 730, 225, 56.41);
        areas.add(redbridge);
        Area newham = new Area("Newham", 690, 350, 36.22);
        areas.add(newham);
        Area towerHamlets = new Area("Tower Hamlets", 600, 375, 19.77);
        areas.add(towerHamlets);
        Area southwark = new Area("Southwark", 535, 470, 28.85);
        areas.add(southwark);
        Area lambeth = new Area("Lambeth", 480, 525, 26.82);
        areas.add(lambeth);
        Area wandsworth = new Area("Wandsworth", 390, 510, 34.26);
        areas.add(wandsworth);
        Area merton = new Area("Merton", 370, 600, 37.61);
        areas.add(merton);
        Area kingston = new Area("Kingston upon Thames", 280, 645, 37.24);
        areas.add(kingston);
        
        Area barkingAndDagenham = new Area("Barking and Dagenham", 800, 310, 36.09);
        areas.add(barkingAndDagenham);
        Area greenwich = new Area("Greenwich", 700, 470, 47.35);
        areas.add(greenwich);
        Area lewisham = new Area("Lewisham", 620, 525, 35.15);
        areas.add(lewisham);
        
        Area havering = new Area("Havering", 920, 250, 112.3);
        areas.add(havering);
        Area bexley = new Area("Bexley", 820, 480, 60.56);
        areas.add(bexley);
        Area bromley = new Area("Bromley", 730, 700, 150.2);
        areas.add(bromley);
        Area croydon = new Area("Croydon", 535, 725, 86.53);
        areas.add(croydon);
        Area sutton = new Area("Sutton", 425, 700, 43.84);
        areas.add(sutton);
    }
}