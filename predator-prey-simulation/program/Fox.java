import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a fox.
 * Foxes age, hunt, move, breed and die.
 * 
 * @version 1.0
 */
public class Fox extends Predator
{
    // Characteristics shared by all foxes (class variables).
    
    // The age at which a fox can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a fox can live.
    private static final int MAX_AGE = 125;
    // The likelihood of a fox breeding.
    private static final double BREEDING_PROBABILITY = 0.55;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single prey. In effect, this is the
    // number of steps a fox can go before it has to eat again.
       
    // A fox's maximum food level.
    private static final int MAX_FOOD_LEVEL = 25;

    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(getRand().nextInt(MAX_AGE));
            setFoodLevel(getRand().nextInt(getRabbitFoodValue()));
            setSexMale(getRand().nextBoolean());
        }
        else {
            setAge(0);
            setFoodLevel(getRabbitFoodValue());
            setSexMale(getRand().nextBoolean());
        }
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * prey. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Actor> newFoxes, String weather, String time)
    {
        incrementAge();
        if (time.equals("Night")) {
            return;
        }
        incrementHunger();
        if(isAlive()) {
            if (weather != null) {
                if (weather.equals("Snow")) {
                    return;
                }
            }
            
            if(time.equals("Day")) {
                giveBirth(newFoxes);  
                // Move towards a source of food if found.
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
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    setFoodLevel(getFoodLevel() + getRabbitFoodValue()); 
                    if(getFoodLevel() > MAX_FOOD_LEVEL) {
                        setFoodLevel(MAX_FOOD_LEVEL);
                    }
                    
                    if (rabbit.getDisease() != null) {
                        this.applyDisease("Rabies", 75);
                    }
                    
                    rabbit.setDead();
                    
                    return where;
                }
            }
            else if(animal instanceof Mouse) {
                Mouse mouse = (Mouse) animal;
                if(mouse.isAlive()) { 
                    setFoodLevel(getFoodLevel() + getMouseFoodValue()); 
                    if(getFoodLevel() > MAX_FOOD_LEVEL) {
                        setFoodLevel(MAX_FOOD_LEVEL);
                    }
                    
                    if (mouse.getDisease() != null) {
                        this.applyDisease("Rabies", 75);
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
                    
                    if (squirrel.getDisease() != null) {
                        this.applyDisease("Rabies", 75);
                    }
                    
                    squirrel.setDead();
                    
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this fox is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newFoxes A list to return newly born foxes.
     */
    private void giveBirth(List<Actor> newFoxes)
    {
        // New foxes are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Fox young = new Fox(false, field, loc);
            newFoxes.add(young);
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
     * A fox can breed if it has reached the breeding age and it can 
     * find a patner of the opposite sex adjacent to it.
     * @return true if the fox can breed, false otherwise.
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
            if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if((getAge() >= getBreedingAge()) && (fox.getSexMale() != getSexMale())) { 
                    returnState = true;
                }
            }
        }
        return returnState;
    }
    
    /**
     * Returns the maximum age of all foxes.
     * @return The max age for all foxes.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Returns the percentage chance that
     * foxes will breed.
     * @return The breeding probability for all foxes.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns minimum age at which all 
     * foxes can start to breed.
     * @return The breeding age for all foxes.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Returns the maximum number of offspring 
     * all foxes can produce.
     * @return The max litter size for all foxes.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}
