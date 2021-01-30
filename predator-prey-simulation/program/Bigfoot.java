import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * A simple model of Bigfoot.
 * Bigfoot is a rare predator that appears only once within a 
 * simulation, it only eats meat and has a short life-span.
 * 
 * @version 1.0
 */
public class Bigfoot extends Animal
{
    // Characteristics shared Bigfoot (class variables).
    
    // The age at which Bigfoot can start to breed (Never).
    private static final int BREEDING_AGE = 0;
    // The age to which Bigfoot can live.
    private static final int MAX_AGE = 50;
    // The likelihood of Bigfoot breeding (Never).
    private static final double BREEDING_PROBABILITY = 0;
    // The maximum number of births (Never).
    private static final int MAX_LITTER_SIZE = 0;
    // Bigfoot's maximum food level.
    //(FOOD_LEVEL > MAX_AGE -- hence can't starve).
    private static final int MAX_FOOD_LEVEL = 99;

    /**
     * Create Bigfoot. Bigfoot can only be created as a 
     * new born (age zero and not hungry).
     * 
     * @param randomAge If true (Never), Bigfoot will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Bigfoot(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        setAge(0);
        setFoodLevel(99);
        setSexMale(true);
    }
    
    /**
     * This is what Bigfoot does most of the time: it hunts for
     * prey. In the process, it might die.
     * @param newBigfoot A list to return newly born Bigfoot (Never).
     */
    public void act(List<Actor> newBigfoot, String weather, String time)
    {
        incrementAge();
        if(isAlive()) {          
            // Move towards a source of food if found.
            move(canHunt(weather));
        }
    }
    
    /**
     * Look for prey adjacent to the current location.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood()
    {
        List<Location> locations = bigfootFindFood();
        if(locations.isEmpty()) {
            return null;
        } else {
            int index = getRand().nextInt(locations.size());
            return locations.get(index);
        }
    }
    
    /**
     * Look for prey adjacent to the current location.
     * All live zombies are eaten, creates an ArrayList.
     * @return Where food was found, or null if it wasn't.
     */
    protected List<Location> bigfootFindFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        
        List<Location> killLocations = new ArrayList<>();
        while(it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            Actor animal = (Actor) object;
            if (animal instanceof Zombie) {
                if(animal.isAlive()) { 
                    // adds all zombies to "killlist" then sets dead.
                    // consumes all adjacent zombies.
                    killLocations.add(animal.getLocation());
                    animal.setDead();
                }
            }
        }
        return killLocations;
    }
    
    /**
     * Returns the maximum age of Bigfoot.
     * @return The max age for Bigfoot.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Returns the percentage chance that Bigfoot will breed (Never).
     * @return The breeding probability for Bigfoot.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns minimum age at which Bigfootcan start to breed (Never).
     * @return The breeding age for Bigfoot.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Returns the maximum number of offspring Bigfoot can produce (Never).
     * @return The max litter size for Bigfoot.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}
