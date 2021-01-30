
/**
 * Used to calculate the Cheapest Neighbourhood for group properties in the StatPage class
 * Contains logic for calculating average price of all group properties in a given neighbourhood
 * 
 * @version 1
 */
public class StatFiveCalculator
{
    // instance variables - replace the example below with your own
    private int totalPrice;
    private int averagePrice;
    private int count;

    /**
     * Constructor for objects of class StatFiveCalculator
     */
    public StatFiveCalculator(int firstPrice)
    {
       count = 1;
       totalPrice = firstPrice;
       averagePrice = totalPrice;
    }
    /**
     * Increments the total price and recalculates the average price
     * @Param the price of the new group property in a neighbourhood
     */
    public void adjustTotalPrice(int newPrice)
    {
        totalPrice = totalPrice + newPrice;
        averagePrice = totalPrice/count;
    }
    /**
     * Increments Count by 1
     */
    public void incrementCount()
    {
        count ++;
    }
    /**
     * @return AveragePrice, the average price of the group properties in a given neighbourhood
     */
    public int returnAverage()
    {
        return averagePrice;
    }
    
}
