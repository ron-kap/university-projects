import java.util.List;

/**
 * A simple model of all Animals in the simulation, 
 * they are biological creatures that must eat to survive.
 *
 * @version 1.0
 */
public abstract class Animal extends Actor
{
    // Animal's sex (male == true)
    private boolean sexMale;
    
    /**
     * Create a Animal, foundation of all Animal creatures -
     * foxes, owls, rabbits, mice, squirrels and Bigfoot.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Animal(Field field, Location location)
    {
        super(field, location);
    }
    
    /**
     * Return the animal's sex.
     * @return The animal's sex.
     */
    public boolean getSexMale()
    {
        return sexMale;
    }
    
    /**
     * Permits to change animal's sex.
     * @param ange The animal's new sex.
     */
    public void setSexMale(boolean sexMale)
    {
        this.sexMale = sexMale;
    }
    
    /**
     * Abstract method to retrieve animal's prey location.
     * @return Where food was found.
     */
    abstract protected Location findFood();
    
    /**
     * This is what the animal does most of the time: 
     * it moves and hunts for food.
     */
    protected void move(boolean canHunt)
    {
        if (!(getDisease() == null)) {
            infect();
        }
        
        Location newLocation = null;
        if (canHunt == true) {
            newLocation = findFood();
        }
        
        if(newLocation == null) { 
            // No food found - try to move to a free location.
            newLocation = getField().freeAdjacentLocation(getLocation());
        }
        // See if it was possible to move.
        if(newLocation != null) {
            setLocation(newLocation);
        }
        else {
            // Overcrowding.
            setDead();
        }
    }
    
    /**
     * Checks whether the animal can hunt, given its weather conditions
     * and/or carried diseases
     * @return if the animal can hunt or not.
     */
    protected boolean canHunt(String weather)
    {
        return true;
    }
}
