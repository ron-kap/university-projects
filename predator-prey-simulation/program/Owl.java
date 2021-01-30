import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a owl.
 * Owls age, hunt, move, breed and die.
 * 
 * @version 1.0
 */
public class Owl extends Predator
{
    // Characteristics shared by all owls (class variables).
    
    // The age at which a owl can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a owl can live.
    private static final int MAX_AGE = 120;
    // The likelihood of a owl breeding.
    private static final double BREEDING_PROBABILITY = 0.55;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;  //5
    // A owl's maximum food level.
    private static final int MAX_FOOD_LEVEL = 25;  //20

    /**
     * Create a owl. A owl can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the owl will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Owl(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(getRand().nextInt(MAX_AGE));
            setFoodLevel(getRand().nextInt(getSquirrelFoodValue()));
            setSexMale(getRand().nextBoolean());
        }
        else {
            setAge(0);
            setFoodLevel(getSquirrelFoodValue());
            setSexMale(getRand().nextBoolean());
        }
    }
    
    /**
     * This is what the owl does most of the time: it hunts for
     * prey. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newowl A list to return newly born owls.
     */
    public void act(List<Actor> newOwls, String weather, String time)
    {
        incrementAge();
        if (weather != null) {
            if (weather.equals("Snow")) {
                return;
            }
        }
        incrementHunger();
        if(isAlive()) {
            giveBirth(newOwls);
            
            // Move towards a source of food if found.
            move(canHunt(weather));
            
            if(time.equals("Night") && isAlive()) {
                move(canHunt(weather));
            }
        }
    }
    
    /**
     * Look for prey adjacent to the current location.
     * Only the first live prey is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Mouse) {
                Mouse mouse = (Mouse) animal;
                if(mouse.isAlive()) { 
                    setFoodLevel(getFoodLevel() + (2 * getMouseFoodValue())); 
                    if(getFoodLevel() > MAX_FOOD_LEVEL) {
                        setFoodLevel(MAX_FOOD_LEVEL);
                    }
                    
                    mouse.setDead();
                    
                    return where;
                }
            }
            else if(animal instanceof Squirrel) {
                Squirrel squirrel = (Squirrel) animal;
                if(squirrel.isAlive()) { 
                    setFoodLevel(getFoodLevel() + getSquirrelFoodValue()); 
                    if(getFoodLevel() > MAX_FOOD_LEVEL) {
                        setFoodLevel(MAX_FOOD_LEVEL);
                    }
                    
                    squirrel.setDead();
                    
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this owl is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newowl A list to return newly born owls.
     */
    private void giveBirth(List<Actor> newOwls)
    {
        // New owls are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Owl young = new Owl(false, field, loc);
            newOwls.add(young);
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
     * A owl can breed if it has reached the breeding age and it can 
     * find a patner of the opposite sex adjacent to it.
     * @return true if the owl can breed, false otherwise.
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
            if(animal instanceof Owl) {
                Owl owl = (Owl) animal;
                if((getAge() >= getBreedingAge()) && (owl.getSexMale() != getSexMale())) { 
                    returnState = true;
                }
            }
        }
        return returnState;
    }
    
    /**
     * Returns the maximum age of all owl.
     * @return The max age for all owl.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Returns the percentage chance that
     * owl will breed.
     * @return The breeding probability for all owl.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns minimum age at which all 
     * owl can start to breed.
     * @return The breeding age for all owl.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Returns the maximum number of offspring 
     * all owl can produce.
     * @return The max litter size for all owl.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}
