import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a plant.
 * Plants age, breed and die.
 *
 * @version 1.0
 */
public class Plant extends Actor
{
    // Characteristics shared by all plants (class variables).

    // The age at which a plant can start to breed.
    private static final int BREEDING_AGE = 2;
    // The age to which a plant can live.
    private static final int MAX_AGE = 5;
    // The likelihood of a plant breeding.
    private static final double BREEDING_PROBABILITY = 0.60;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;

    /**
     * Create a new plant. A plant may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the plant will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        setAge(0);
        if(randomAge) {
            setAge(getRand().nextInt(MAX_AGE));
        }
    }

    /**
     * This is what the plant does most of the time: it breeds.
     * In the process it might die.
     * @param newPlants A list to return new plants.
     */
    public void act(List<Actor> newPlants, String weather, String time)
    {
        if (time.equals("Night")) {
            return;
        }
        if (weather != null) {
                if (weather.equals("Snow")) {
                    return;
                }
        }
            
        incrementAge();
        
        if(isAlive()) {
            giveBirth(newPlants);
        }
    }
    
    /**
     * Check whether or not this plant is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newPlants A list to return new plants.
     */
    private void giveBirth(List<Actor> newPlants)
    {
        // New plants are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Plant young = new Plant(false, field, loc);
            newPlants.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && getRand().nextDouble() <= BREEDING_PROBABILITY) {
            births = getRand().nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A plant can breed if it has reached the breeding age.
     * @return true if the plant can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return getAge() >= BREEDING_AGE;
    }
    
    /**
     * Returns the maximum age of all plants.
     * @return The max age for all plants.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Returns the percentage chance that
     * plants will breed.
     * @return The breeding probability for all plants.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns minimum age at which all 
     * plants can start to breed.
     * @return The breeding age for all plants.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Returns the maximum number of offspring 
     * all plants can produce.
     * @return The max litter size for all plants.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}
