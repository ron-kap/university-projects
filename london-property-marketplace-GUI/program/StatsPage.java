import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * StatsPage class, responsible for individual statistic logic 
 * and organisation - Panel 3.
 *
 * @version 1
 */
public class StatsPage
{
    private int minPrice;
    private int maxPrice;
    private ArrayList<AirbnbListing> data;
    private ArrayList<Area> areas; 
    
    private ArrayList<AirbnbListing> availableProp;
    
    private int[] activeStatPages = {1, 2, 3, 4};
    private int index;  //field because of lamda
    
    private JPanel firstStatBox;
    private JPanel secondStatBox;
    private JPanel thirdStatBox;
    private JPanel fourthStatBox;

    private JPanel firstBoxContent = new JPanel();
    private JPanel secondBoxContent = new JPanel();
    private JPanel thirdBoxContent = new JPanel();
    private JPanel fourthBoxContent = new JPanel();
    
    private JPanel statsContainer;
    private JPanel statsGrid;
    private JPanel buffer;
    
    /**
     * Constructor for objects of class StatsPopUp
     */
    public StatsPage(int minPriceValue, int maxPriceValue, ArrayList<AirbnbListing> data)
    {
        minPrice = minPriceValue;
        maxPrice = maxPriceValue;
        this.data = data;
        
        availableProp = new ArrayList<>();
        for(AirbnbListing listing : data) {
            if(listing.getPrice() <= maxPrice && listing.getPrice() >= minPrice) {
                availableProp.add(listing);
            }
        }
    }
    
    /**
     * Builds the container that holds the data for the Statistics
     * Screen (Panel 3).
     *
     * @param   takes price range in case of change.
     * @return  JPanel with the welcome greeting inside.
     */
    public JPanel makeStatsPage(int minPriceValue, int maxPriceValue, ArrayList<AirbnbListing> data, ArrayList<Area> areas)
    {
        minPrice = minPriceValue;
        maxPrice = maxPriceValue;
        this.data = data;
        this.areas = areas;
        
        //stats container
        statsContainer = new JPanel();
        statsContainer.setLayout(new BorderLayout());
        statsContainer.setBackground(Color.WHITE);

        //title container
        JPanel titleContainer = new JPanel();
        statsContainer.add(titleContainer, BorderLayout.NORTH);
        titleContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        //contents
        JLabel mapLabel = new JLabel("Neighbourhood statistics between £" + minPrice + " - £" + maxPrice, SwingConstants.CENTER);
        mapLabel.setFont(new Font("Century Gothic", Font.BOLD, 1));
        mapLabel.setFont(mapLabel.getFont().deriveFont(28f)); 
        titleContainer.add(mapLabel);
        
        //stats
        //initial appending of four statistics
        secondBoxContent = getStat2();
        firstBoxContent = getStat1();
        thirdBoxContent = getStat3();
        fourthBoxContent = getStat4();

        //stat content pane
        statsGrid = new JPanel();
        statsGrid.setBorder(new EmptyBorder(100, 50, 100, 50));
        statsGrid.setLayout(new GridLayout(2, 2));

        //first stat box
        firstStatBox = new JPanel();
        firstStatBox.setLayout(new BorderLayout());
        firstStatBox.setBorder(new EmptyBorder(5, 5, 5, 5));
        firstStatBox.add(firstBoxContent, BorderLayout.CENTER);

        JButton firstLeftButton = new JButton("<");
        firstLeftButton.addActionListener(e -> buttonClick(1, false));
        firstStatBox.add(firstLeftButton, BorderLayout.WEST);
        JButton firstRightButton = new JButton(">");
        firstRightButton.addActionListener(e -> buttonClick(1, true));
        firstStatBox.add(firstRightButton, BorderLayout.EAST);

        //second stat box
        secondStatBox = new JPanel();
        secondStatBox.setLayout(new BorderLayout());
        secondStatBox.setBorder(new EmptyBorder(5, 5, 5, 5));
        secondStatBox.add(secondBoxContent, BorderLayout.CENTER);

        JButton secondLeftButton = new JButton("<");
        secondLeftButton.addActionListener(e -> buttonClick(2, false));
        secondStatBox.add(secondLeftButton, BorderLayout.WEST);
        JButton secondRightButton = new JButton(">");
        secondRightButton.addActionListener(e -> buttonClick(2, true));
        secondStatBox.add(secondRightButton, BorderLayout.EAST);

        //third stat box
        thirdStatBox = new JPanel();
        thirdStatBox.setLayout(new BorderLayout());
        thirdStatBox.setBorder(new EmptyBorder(5, 5, 5, 5));
        thirdStatBox.add(thirdBoxContent, BorderLayout.CENTER);

        JButton thirdLeftButton = new JButton("<");
        thirdLeftButton.addActionListener(e -> buttonClick(3, false));
        thirdStatBox.add(thirdLeftButton, BorderLayout.WEST);
        JButton thirdRightButton = new JButton(">");
        thirdRightButton.addActionListener(e -> buttonClick(3, true));
        thirdStatBox.add(thirdRightButton, BorderLayout.EAST);

        //fourth stat box
        fourthStatBox = new JPanel();
        fourthStatBox.setLayout(new BorderLayout());
        fourthStatBox.setBorder(new EmptyBorder(5, 5, 5, 5));
        fourthStatBox.add(fourthBoxContent, BorderLayout.CENTER);

        JButton fourthLeftButton = new JButton("<");
        fourthLeftButton.addActionListener(e -> buttonClick(4, false));
        fourthStatBox.add(fourthLeftButton, BorderLayout.WEST);
        JButton fourthRightButton = new JButton(">");
        fourthRightButton.addActionListener(e -> buttonClick(4, true));
        fourthStatBox.add(fourthRightButton, BorderLayout.EAST);

        statsGrid.add(firstStatBox);
        statsGrid.add(secondStatBox);
        statsGrid.add(thirdStatBox);
        statsGrid.add(fourthStatBox);
        
        statsContainer.add(statsGrid, BorderLayout.CENTER);

        return statsContainer;
    }
    
    /**
     * One unified method calculating the position of the next or
     * previous statistic.
     * 
     * @param   boxNum - one of the four statistic display boxes, 1-4.
     *          nextButton - true if next button, false for previous.
     */
    public void buttonClick(int boxNum, boolean nextButton) //boxNum - one of the four display boxes
    {
        switch (boxNum) {
            case 1: firstStatBox.remove(firstBoxContent);
            firstBoxContent = getNextStat(boxNum, nextButton);
            break;
            case 2: secondStatBox.remove(secondBoxContent);
            secondBoxContent = getNextStat(boxNum, nextButton);
            break;
            case 3: thirdStatBox.remove(thirdBoxContent);
            thirdBoxContent = getNextStat(boxNum, nextButton);
            break;
            case 4: fourthStatBox.remove(fourthBoxContent);
            fourthBoxContent = getNextStat(boxNum, nextButton);
            break;
        }

        statsContainer.remove(statsGrid);
        
        statsGrid = new JPanel();
        statsGrid.setBorder(new EmptyBorder(100, 50, 100, 50));
        statsGrid.setLayout(new GridLayout(2, 2));
        
        firstStatBox.add(firstBoxContent, BorderLayout.CENTER);
        secondStatBox.add(secondBoxContent, BorderLayout.CENTER);
        thirdStatBox.add(thirdBoxContent, BorderLayout.CENTER);
        fourthStatBox.add(fourthBoxContent, BorderLayout.CENTER);

        statsGrid.add(firstStatBox);
        statsGrid.add(secondStatBox);
        statsGrid.add(thirdStatBox);
        statsGrid.add(fourthStatBox);
        
        statsContainer.add(statsGrid, BorderLayout.CENTER);

        statsContainer.validate();
        statsContainer.repaint();
    }
    
    /**
     * One unified method calculating and generating the next or
     * previous statistic.
     *  
     * @param   boxNum - one of the four statistic display boxes, 1-4.
     *          nextButton - true if next button, false for previous.
     * @return  JPanel containing the content of a calculated stat box
     *          (content only, no buttons)
     */
    public JPanel getNextStat(int boxNum, boolean nextButton)   //boxNum - one of the four display boxes
    {
        JPanel boxToEdit = new JPanel();
        
        index = (activeStatPages[boxNum - 1]) - 1;
        boolean statFound = false;
        
        if(nextButton == true) {    //next button
            while (statFound == false) {
                index += 1;
                if(index >= 8) {    //8 total statistics
                    index = 0;
                }
                
                boolean contains = Arrays.stream(activeStatPages).anyMatch(i -> i == (index + 1));
                
                if(contains == false) {
                    statFound = true;
                    switch (index + 1) {
                        //if gen box one, take the boxNum and replace that box 
                        //num with content of one, and return the entire grid
                        case 1: boxToEdit = getStat1();
                                resetContentArray(boxNum, 1);
                                break;
                        case 2: boxToEdit = getStat2();
                                resetContentArray(boxNum, 2);
                                break;
                        case 3: boxToEdit = getStat3();
                                resetContentArray(boxNum, 3);
                                break;
                        case 4: boxToEdit = getStat4();
                                resetContentArray(boxNum, 4);
                                break;
                        case 5: boxToEdit = getStat5();
                                resetContentArray(boxNum, 5);
                                break;
                        case 6: boxToEdit = getStat6();
                                resetContentArray(boxNum, 6);
                                break;
                        case 7: boxToEdit = getStat7();
                                resetContentArray(boxNum, 7);
                                break;
                        case 8: boxToEdit = getStat8();
                                resetContentArray(boxNum, 8);
                                break;
                    }
                }
            }
        } else {    //previous button
            while (statFound == false) {
                index -= 1;
                if(index < 0) { //index >= 0
                    index = 7;  //8 total statistics
                }
                
                boolean contains = Arrays.stream(activeStatPages).anyMatch(i -> i == (index + 1));
                
                if(contains == false) {
                    statFound = true;
                    switch (index + 1) {
                        //if random gen box one, take the boxNum and replace that box 
                        //num with content of one, and return the entire grid
                        case 1: boxToEdit = getStat1();
                                resetContentArray(boxNum, 1);
                                break;
                        case 2: boxToEdit = getStat2();
                                resetContentArray(boxNum, 2);
                                break;
                        case 3: boxToEdit = getStat3();
                                resetContentArray(boxNum, 3);
                                break;
                        case 4: boxToEdit = getStat4();
                                resetContentArray(boxNum, 4);
                                break;
                        case 5: boxToEdit = getStat5();
                                resetContentArray(boxNum, 5);
                                break;
                        case 6: boxToEdit = getStat6();
                                resetContentArray(boxNum, 6);
                                break;
                        case 7: boxToEdit = getStat7();
                                resetContentArray(boxNum, 7);
                                break;
                        case 8: boxToEdit = getStat8();
                                resetContentArray(boxNum, 8);
                                break;
                    }
                }
            }
        }
        
        return boxToEdit;
    }
    
    /**
     * Calculates and builds the container that holds 
     * the content for Stat 1 - Average Number of Reviews.
     * 
     * @return  a JPanel containing the content for Stat 1.
     */
    public JPanel getStat1()
    {
        JPanel statBox1 = new JPanel();
        statBox1.setLayout(new GridLayout(2, 1));
        
        JLabel statLabel = new JLabel("Average No. of Reviews:", JLabel.CENTER);
        statBox1.add(statLabel);
        
        int averageReviews = 0;
        
        for(AirbnbListing listing : availableProp) {
            averageReviews += listing.getNumberOfReviews();
        }
        
        JLabel statLabel2 = new JLabel("" + (averageReviews / availableProp.size()), JLabel.CENTER);
        statBox1.add(statLabel2);
        int test = getNoPropIn("Hackney");
        
        return statBox1;
    }
    
    /**
     * Calculates and builds the container that holds 
     * the content for Stat 2 - Number of Properties.
     * 
     * @return  a JPanel containing the content for Stat 2.
     */
    public JPanel getStat2()
    {
        JPanel statBox2 = new JPanel();
        statBox2.setLayout(new GridLayout(2, 1));
        
        JLabel statLabel = new JLabel("No. of Properties:", JLabel.CENTER);
        statBox2.add(statLabel);
        
        availableProp = new ArrayList<>();
        for(AirbnbListing listing : data) {
            if(listing.getPrice() <= maxPrice && listing.getPrice() >= minPrice) {
                availableProp.add(listing);
            }
        }
        
        JLabel statLabel2 = new JLabel("" + availableProp.size(), JLabel.CENTER);
        statBox2.add(statLabel2);
        
        return statBox2;
    }
    
    /**
     * Calculates and builds the container that holds 
     * the content for Stat 3 - Number of Entire Properties.
     * 
     * @return  a JPanel containing the content for Stat 3.
     */
    public JPanel getStat3()
    {
        JPanel statBox3 = new JPanel();
        statBox3.setLayout(new GridLayout(2, 1));
        
        JLabel statLabel = new JLabel("No. of Entire Properties:", JLabel.CENTER);
        statBox3.add(statLabel);
        
        int entireHouseAptCount = 0;
        
        for(AirbnbListing listing : availableProp) {
            //no. of entire properties
            if(listing.getRoom_type().equals("Entire home/apt")) {
                entireHouseAptCount++;
            }
        }
        
        JLabel statLabel2 = new JLabel("" + entireHouseAptCount, JLabel.CENTER);
        statBox3.add(statLabel2);
        
        return statBox3;
    }
    
    /**
     * Calculates and builds the container that holds 
     * the content for Stat 4 - Priciest Neighbourhood.
     * 
     * @return  a JPanel containing the content for Stat 4.
     */
    public JPanel getStat4()
    {
        JPanel statBox4 = new JPanel();
        statBox4.setLayout(new GridLayout(2, 1));
        
        JLabel statLabel = new JLabel("Priciest Neighbourhood:", JLabel.CENTER);
        statBox4.add(statLabel);
        
        JLabel statLabel2 = new JLabel(stat4Calc(), JLabel.CENTER);
        statBox4.add(statLabel2);
        
        return statBox4;
    }
    
    /**
     * Calculates and builds the container that holds 
     * the content for Stat 5 - Cheapest Neighbourhood for group properties.
     * 
     * @return  a JPanel containing the content for Stat 5.
     */
    public JPanel getStat5()
    {
        JPanel statBox5= new JPanel();
        statBox5.setLayout(new GridLayout(2, 1));
        HashMap<String,StatFiveCalculator> neighbourhoodPrices = new HashMap<>();
        
        for(AirbnbListing listing : availableProp)
        {
            String hood = listing.getNeighbourhood();
            if(neighbourhoodPrices.get(hood)== null && listing.getRoom_type().equals("Entire home/apt"))
            {
                neighbourhoodPrices.put(hood,new StatFiveCalculator(listing.getPrice()));
            }
            else if(listing.getRoom_type().equals("Entire home/apt"))
                {
                 neighbourhoodPrices.get(hood).incrementCount();
                 neighbourhoodPrices.get(hood).adjustTotalPrice(listing.getPrice());
                }
        }
        HashMap.Entry<String,StatFiveCalculator> maxEntry = null;
        for(HashMap.Entry<String,StatFiveCalculator> entry: neighbourhoodPrices.entrySet())
        {
            if(maxEntry == null)
                {
                  maxEntry = entry;
                }
            else if(entry.getValue().returnAverage() < maxEntry.getValue().returnAverage())
                {
                    maxEntry = entry;
                }
                
        }
        
        JLabel statLabel = new JLabel("Cheapest Neighbourhood for Group Properties:", JLabel.CENTER);
        statBox5.add(statLabel);
        
        if(maxEntry == null)
        {
            JLabel statLabel2 = new JLabel("There are no group houses in this price range", JLabel.CENTER);
            statBox5.add(statLabel2);
        }
        else
        {
            String cheapestGroupHouses = maxEntry.getKey();
            JLabel statLabel2 = new JLabel(cheapestGroupHouses + " - " + maxEntry.getValue().returnAverage() + "£/GroupHouse", JLabel.CENTER);
            statBox5.add(statLabel2);   
        }
        
        return statBox5;
    }
    
    /**
     * Calculates and builds the container that holds 
     * the content for Stat 6 - Densest Neighbourhood (Listings/km^2).
     * 
     * @return  a JPanel containing the content for Stat 6.
     */
    public JPanel getStat6()
    {
        JPanel statBox6 = new JPanel();
        statBox6.setLayout(new GridLayout(2, 1));
        
        String maxArea = null;
        double maxValue = 0;
        
        for (Area area : areas) {
            double areaValue = 0;
            for (AirbnbListing listing : data) {
                if (listing.getNeighbourhood().equals(area.getName())) {
                    areaValue++;
                }
            }
            
            areaValue = areaValue / area.getSurfaceArea();

            if (areaValue > maxValue) {
                maxValue = areaValue;
                maxArea = area.getName();
            }
        }

        JLabel statLabel = new JLabel("<html> Densest Neighbourhood (Listings/km^2)<html>", JLabel.CENTER);
        statBox6.add(statLabel);
        
        JLabel statLabel2 = new JLabel(maxArea + ": " + (int) maxValue, JLabel.CENTER);
        statBox6.add(statLabel2);
        
        return statBox6;
    }
    
    /**
     * Calculates and builds the container that holds 
     * the content for Stat 7 - Most Reviewed Neighbourhood.
     * 
     * @return  a JPanel containing the content for Stat 7.
     */
    public JPanel getStat7()
    {
        JPanel statBox7 = new JPanel();
        statBox7.setLayout(new GridLayout(2, 1));
        
            //average
        JLabel statLabel = new JLabel("Most Reviewed Neighbourhood:", JLabel.CENTER);
        statBox7.add(statLabel);
               
        JLabel statLabel2 = new JLabel(stat7Calc(), JLabel.CENTER);
        statBox7.add(statLabel2);
        
        return statBox7;
    }

    /**
     * Calculates and builds the container that holds 
     * the content for Stat 8 - Ian.
     * 
     * @return  a JPanel containing the content for Stat 8.
     */
    public JPanel getStat8()
    {
        JPanel statBox8 = new JPanel();
        statBox8.setLayout(new GridLayout(2, 1));
        
        JLabel statLabel = new JLabel("Most Reviewed Host:", JLabel.CENTER);
        statBox8.add(statLabel);
        
        JLabel statLabel2 = new JLabel(stat8Calc(), JLabel.CENTER);
        statBox8.add(statLabel2);
        
        return statBox8;
    }
   
    /**
     * Used to update activeStatArray, when a old stat is discarded 
     * and a new in position.
     * 
     * @param   boxNum - one of the four statistic display boxes, 1-4.
     *          newBox - the new (current) box to add to.
     */
    public void resetContentArray(int boxNum, int newBox)
    {
        activeStatPages[boxNum - 1] = newBox;
    }
    
    /**
     * Builds the container that holds the data for the 
     * statistics pop up (Panel 3) - populates the frame.
     *
     * @param   neighbourhood for count.
     * @return  the number of properties in a neighbourhood, considering price range.
     */
    public int getNoPropIn(String hood)
    {
        int count = 0;
        for (AirbnbListing listing: availableProp) {
            if(listing.getNeighbourhood().equals(hood)) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Method to calculate data for statistic 4.
     */
    public String stat4Calc()
    {
        HashMap<String,Integer> neighbourhoodPrices = new HashMap<>();
        for(AirbnbListing listing : availableProp) 
        {   String hood = listing.getNeighbourhood();
            if(neighbourhoodPrices.get(hood) == null)
            {
               neighbourhoodPrices.put(hood,(listing.getPrice()*listing.getMinimumNights())); 
            }
            else
            {   int newVal = neighbourhoodPrices.get(hood)+(listing.getPrice()*listing.getMinimumNights());
                neighbourhoodPrices.replace(hood,newVal);
            }
        }
        HashMap.Entry<String,Integer> maxEntry = null;
        for(HashMap.Entry<String,Integer> entry: neighbourhoodPrices.entrySet())
        {
            if(maxEntry == null)
            {
              maxEntry = entry;
            }
            else if(entry.getValue().intValue() > maxEntry.getValue().intValue())
            {
                maxEntry = entry;
            }
        }
        String pricestNeighbourhood= maxEntry.getKey();
        
        return pricestNeighbourhood;
    }
    
    /**
     * Method to calculate data for statistic 7.
     */
    public String stat7Calc()
    {
        HashMap<String,Integer> neighbourhoodReview = new HashMap<>();
        for(AirbnbListing listing : availableProp) 
        {   String hood = listing.getNeighbourhood();
            if(neighbourhoodReview.get(hood) == null)
            {
               neighbourhoodReview.put(hood,(listing.getNumberOfReviews())); 
            }
            else
            {   int newVal = neighbourhoodReview.get(hood)+(listing.getNumberOfReviews());
                neighbourhoodReview.replace(hood,newVal);
            }
        }
        
        for(HashMap.Entry<String,Integer> entry: neighbourhoodReview.entrySet())
        {
            int noProp = getNoPropIn(entry.getKey());
            neighbourhoodReview.replace(entry.getKey(),(entry.getValue() / noProp));
        }
        
        HashMap.Entry<String,Integer> maxEntry = null;
        for(HashMap.Entry<String,Integer> entry: neighbourhoodReview.entrySet())
        {
            if(maxEntry == null)
            {
              maxEntry = entry;
            }
            else if(entry.getValue().intValue() > maxEntry.getValue().intValue())
            {
                maxEntry = entry;
            }
        }
        
        if(maxEntry == null)
        {
            String nullText = "There are no group houses in this price range";
            return nullText;
        }
        else
        {
            String reviewNeighbourhood = maxEntry.getKey();
            return reviewNeighbourhood;
        }
    }
    
    /**
     * Method to calculate data for statistic 8.
     */
    public String stat8Calc()
    {
        HashMap<String, Integer> bob = new HashMap<>();
        HashMap<String, String> sam = new HashMap<>();
        for(AirbnbListing listing: availableProp) {
            if (!bob.containsKey(listing.getHost_id())) {
                bob.put(listing.getHost_id(), listing.getNumberOfReviews());
                sam.put(listing.getHost_id(), listing.getHost_name());
            }
            else {
                bob.put(listing.getHost_id(), bob.get(listing.getHost_id()) + listing.getNumberOfReviews());
            }
        }
        
        String hostName = "";
        int max = 0;
        for(String temp: bob.keySet()) { 
            int current = bob.get(temp);
            if(bob.get(temp) > max) {
                max = current;
                hostName = sam.get(temp) + " (ID:" + temp + ")";
            }
        }
        
        return hostName;
    }
}