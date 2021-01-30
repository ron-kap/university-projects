import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a zombie.
 * Zombies age, move, infect, and die.
 * 
 * @version 1.0
 */
public class Zombie extends Animal
{
    // Characteristics shared by all zombies (class variables).
    
    // The age to which a zombie can live.
    private static final int MAX_AGE = 3;
    // The age at which zombies can start to breed (Never).
    private static final int BREEDING_AGE = 0;
    // The likelihood of zombies breeding (Never).
    private static final double BREEDING_PROBABILITY = 0;
    // The maximum number of births (Never).
    private static final int MAX_LITTER_SIZE = 0;
    
    // Individual characteristics (instance fields).
    // The zombie's age.
    private int age;
    
    /**
     * Create a zombie.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Zombie(Field field, Location location)
    {
        super(field, location);
        age = 0;
        setDisease("Element 115 Exposure");
    }
    
    /**
     * This is what the zombie does: it infects
     * other animals. In the process, it might die of old age.
     * 
     * @param newZombies a list to receive newly created zombies
     * @weather the current type of weather in the simulator
     */
    public void act(List<Actor> newZombies, String weather, String time)
    {
        incrementAge();
        if(isAlive()) {
            if (weather != null) {
                if (weather.equals("Snow")) {
                    return;
                }
            }            
            // Move towards a source of food if found.
            zombieInfect(newZombies);
            move(canHunt(weather));
        }
    }
    
    /**
     * Returns the maximum age of zombies.
     * @return The max age for zombies.
     */
    protected Location findFood() {
        return null;
    }
    
    /**
     * Returns the maximum age of zombies.
     * @return The max age for zombies.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Returns the percentage chance that zombies will breed (Never).
     * @return The breeding probability for zombies.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Returns minimum age at which zombies can start to breed (Never).
     * @return The breeding age for zombies.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }
    
    /**
     * Returns the maximum number of offspring zombies can produce (Never).
     * @return The max litter size for zombies.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
    
    /**
     * Look for certain animals adjacent to current location and
     * might infect them with the disease.
     * Infects up to 3 animals in a single step
     * @ param newZombies a list to receive newly created zombies
     */
    protected void zombieInfect(List<Actor> newZombies)
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        int infections = 0;
        while(it.hasNext() && infections <= 2) { // only infect 2 animals.
            Location where = it.next();
            Object object = field.getObjectAt(where);
            if(object instanceof Animal && !(object instanceof Zombie) && !(object instanceof Bigfoot)) {
                // cannot infect other zombies or Bigfoot.
                Animal animal = (Animal) object;
                if (animal.isAlive()) {
                    animal.applyZombieDisease(25, newZombies);
                    infections++;
                }
            }
        }
    }
}
