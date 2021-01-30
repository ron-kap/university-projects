import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a mouse.
 * Mice age, eat, move, breed, and die.
 * 
 * @version 1.0
 */
public class Mouse extends Prey
{
    // Characteristics shared by all mice (class variables).

    // The age at which a mouse can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a mouse can live.
    private static final int MAX_AGE = 40;
    // The likelihood of a mouse breeding.
    private static final double BREEDING_PROBABILITY = 0.55;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // A mouse's maximum food level.
    private static final int MAX_FOOD_LEVEL = 12;
    
    /**
     * Create a new mouse. A mouse may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the mouse will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Mouse(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(getRand().nextInt(MAX_AGE));
            setFoodLevel(getRand().nextInt(getPlantFoodValue()));
            setSexMale(getRand().nextBoolean());
        }
        else {
            setAge(0);
            setFoodLevel(getPlantFoodValue());
            setSexMale(getRand().nextBoolean());
        }
    }
    
    /**
     * This is what the mouse does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newMice A list to return newly born mice.
     */
    public void act(List<Actor> newMice, String weather, String time)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            if (weather != null) {
                if (weather.equals("Snow")) {
                    return;
                }
            }
            giveBirth(newMice);            
            // Move towards a source of food if found.
            move(canHunt(weather));
        }
    }
    
    /**
     * Look for plants adjacent to the current location.
     * Only the first live plant is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object actor = field.getObjectAt(where);
            if(actor instanceof Plant) {
                Plant plant = (Plant) actor;
                if(plant.isAlive()) { 
                    setFoodLevel(getFoodLevel() + (2*getPlantFoodValue())); 
                    if(getFoodLevel() > MAX_FOOD_LEVEL) {
                        setFoodLevel(MAX_FOOD_LEVEL);
                    }
                    
                    plant.setDead();
                    
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this mouse is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newMice A list to return newly born mice.
     */
    private void giveBirth(List<Actor> newMice)
    {
        // New mouses are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Mouse young = new Mouse(false, field, loc);
            newMice.add(young);
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
     * A mouse can breed if it has reached the breeding age and it can 
     * find a patner of the opposite sex adjacent to it.
     * @return true if the mouse can breed, false otherwise.
     */
    private boolean canBreed()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        boolean returnState = false;
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Mouse) {
                Mouse mouse = (Mouse) animal;
                if((getAge() >= getBreedingAge()) && (mouse.getSexMale() != getSexMale())) { 
                    returnState = true;
                }
            }
        }
        return returnState;
    }
    
    /**
     * Returns the maximum age of all mice.
     * @return The max age for all mice.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Returns the percentage chance that
     * mice will breed.
     * @return The breeding probability for all mice.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns minimum age at which all 
     * mice can start to breed.
     * @return The breeding age for all mice.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Returns the maximum number of offspring 
     * all mice can produce.
     * @return The max litter size for all mice.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}
