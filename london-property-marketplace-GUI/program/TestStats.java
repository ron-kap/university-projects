import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;

/**
 * @version 1
 */
public class TestStats
{
    private AirbnbDataLoader loader = new AirbnbDataLoader();
    private ArrayList<AirbnbListing> data = loader.load();
    private StatsPage statsPage;
    
    private int minPrice;
    private int maxPrice;

    /**
     * Constructor for objects of class TestStats
     */
    public TestStats()
    {
        minPrice = 8;
        maxPrice = 20;
        statsPage = new StatsPage(minPrice, maxPrice, data);
        JPanel testPanel = statsPage.getStat2();
    }
    
    /**
     * Auto tests all four test methods.
     */
    public void testAll()
    {
        testNumberOfPropertiesHackney();
        testStatistic4();
        testStatistic7();
        testStatistic8();
    }

    /**
     * Tests the number of properties method.
     */
    public void testNumberOfPropertiesHackney()
    {
        int result = statsPage.getNoPropIn("Hackney");
        int expectedResult = 65;
        System.out.println("Test || getNoPropIn() - Hackney ");
        System.out.println("Hackney should have " + expectedResult + " properties between the Price Range £" + minPrice + " - £" + maxPrice);
        System.out.println("Test result is: " + result);
        if(result == expectedResult) {
            System.out.println("Test Successful" + "\n");
        } else {
            System.out.println("Test Unsuccessful" + "\n");
        }
    }
    
    /**
     * Tests stat 4.
     */
    public void testStatistic4()
    {
        String result = statsPage.stat4Calc();
        String expectedResult = "Newham";
        System.out.println("Test || stat4Calc(): £8 - £20");
        System.out.println("Priciest Neighbourhood should be " + expectedResult + " between the Price Range £" + minPrice + " - £" + maxPrice);
        System.out.println("Test result is: " + result);
        if(result.equals(expectedResult)) {
            System.out.println("Test Successful" + "\n");
        } else {
            System.out.println("Test Unsuccessful" + "\n");
        }
    }
    
    /**
     * Tests stat 7.
     */
    public void testStatistic7()
    {
        String result = statsPage.stat7Calc();
        String expectedResult = "Westminster";
        System.out.println("Test || stat7Calc(): £8 - £20");
        System.out.println("Most Reviewed Neighbourhood should be " + expectedResult + " between the Price Range £" + minPrice + " - £" + maxPrice);
        System.out.println("Test result is: " + result);
        if(result.equals(expectedResult)) {
            System.out.println("Test Successful" + "\n");
        } else {
            System.out.println("Test Unsuccessful" + "\n");
        }
    }
    
    /**
     * Tests stat 8.
     */
    public void testStatistic8()
    {
        String result = statsPage.stat8Calc();
        String expectedResult = "Richard (ID:14148041)";
        System.out.println("Test || stat7Calc(): £8 - £20");
        System.out.println("Most Reviewed Host should be " + expectedResult + " between the Price Range £" + minPrice + " - £" + maxPrice);
        System.out.println("Test result is: " + result);
        if(result.equals(expectedResult)) {
            System.out.println("Test Successful" + "\n");
        } else {
            System.out.println("Test Unsuccessful" + "\n");
        }
    }
}
