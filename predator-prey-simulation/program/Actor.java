import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of all Actors in the simulation,
 * some are animals others are plants.
 *
 * @version 1.0
 */
public abstract class Actor
{
    // Whether the actor is alive or not.
    private boolean alive;
    // The actor's field.
    private Field field;
    // The actor's position in the field.
    private Location location;
    //Actor's age
    private int age;
    //Actor's food level
    private int foodLevel;
    //Actor's disease (null if actor does not have a disease)
    private String disease;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    /**
     * Create a Actor, foundation of all Actors -
     * foxes, owls, rabbits, mice, squirrels, plants and Bigfoot.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Actor(Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Make this actor act - that is: make it do
     * whatever it wants/needs to do.
     * @param newActors A list to receive newly born actors.
     * @param weather The current weather in the simulation
     */
    abstract public void act(List<Actor> newActors, String weather, String time);

    /**
     * Check whether the actor is alive or not.
     * @return true if the actor is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the actor is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Place the actor at the new location in the given field.
     * @param newLocation The actor's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Increase the age. This could result in it's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Make the actor more hungry. This could result in it's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Return the actor's location.
     * @return The actor's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Return the actor's field.
     * @return The actor's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Returns a random value, 
     * used within logic implementation.
     * @return Random value.
     */
    protected Random getRand()
    {
        return rand;
    }
    
    /**
     * Return the actor's age.
     * @return The actor's age.
     */
    protected int getAge()
    {
        return age;
    }
    
    /**
     * Permits to change actor's age.
     * @param ange The actor's new age.
     */
    protected void setAge(int age)
    {
        this.age = age;
    }
    
    /**
     * Return the actor's food level.
     * @return The actor's food level.
     */
    protected int getFoodLevel()
    {
        return foodLevel;
    }
    
    /**
     * Permits to change actor's food level.
     * @param ange The actor's new food level.
     */
    protected void setFoodLevel(int foodLevel)
    {
        this.foodLevel = foodLevel;
    }
    
    /**
     * Abstract method to retrieve actor's max age.
     * @return The actor's max age.
     */
    abstract public int getMaxAge();
    
    /**
     * Abstract method to retrieve actor's breeding chance.
     * @return The actor's breeding probability.
     */
    abstract public double getBreedingProb();
    
    /**
     * Abstract method to retrieve actor's minimum age to breed.
     * @return The actor's breeding age.
     */
    abstract public int getBreedingAge();
    
    /**
     * Abstract method to retrieve actor's maximum child rate.
     * @return The actor's max litter size.
     */
    abstract public int getMaxLitterSize();
    
    /**
     * Return the actor's disease.
     * @return The actor's disease.
     */
    protected String getDisease() {
        return disease;
    }
    
    /**
     * Sets the actor's disease.
     * @param newDisease The disease to be applied.
     */
    protected void setDisease(String newDisease)
    {
        this.disease = newDisease;
    }
   
    /**
     * Applies the effect of a disease to the actor
     * @param disease The disease to be applied
     * @param randomChance The chance of getting infected
     */
    protected void applyDisease(String disease, int randomChance)
    {
        // Ovewritten in Predator and Prey class
    }
    
    /**
     * Turns the actor into a zombie
     * 
     * @param randomChance The chance of getting infected
     * @param newZombies a list to receive newly created zombies
     */
    protected void applyZombieDisease(int randomChance, List<Actor> newZombies)
    {
        int i = rand.nextInt(100);
        if (i <= randomChance) {
            if (isAlive()) {
                Field field = getField();
                List<Location> free = field.getFreeAdjacentLocations(getLocation());
                Location zombieLocation = getLocation();
                if (!(free.isEmpty())) {
                    zombieLocation = free.remove(0);
                }
                setDead();
                Zombie zombie = new Zombie(field, zombieLocation);
                newZombies.add(zombie);
            }
        }
    }
    
    /**
     * Look for certain animals adjacent to current location and
     * might infect them with the disease.
     */
    protected void infect()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object object = field.getObjectAt(where);
            Actor actor = (Actor) object;
            if (actor != null) {
                if (object.getClass() == this.getClass()) {
                    if (actor.getDisease() != null) {
                        if(actor.getDisease().equals(this.getDisease())) { 
                        }
                        else {
                            actor.applyDisease(getDisease(), 25);
                        }
                    }
                    else {
                        actor.applyDisease(getDisease(), 25);
                    }
                }
            }
        }
    }
    
}
