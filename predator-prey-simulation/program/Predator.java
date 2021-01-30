/**
 * A simple model of all Predators in the simulation, 
 * they are species that hunt other species for food.
 *
 * @version 1.0
 */
public abstract class Predator extends Animal
{
    // The animal food value determines the no. of steps
    // the predator can survive by consuming its kill.
    
    // the food level of a single rabbit
    private static final int RABBIT_FOOD_VALUE = 8;
    // the food level of a single mouse
    private static final int MOUSE_FOOD_VALUE = 3;
    // the food level of a single squirrel
    private static final int SQUIRREL_FOOD_VALUE = 5;

    /**
     * Create a Predator, foundation of all Predator animals -
     * foxes and owls.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Predator(Field field, Location location)
    {
        super(field, location);
    }
    
    /**
     * Returns the food value of a single rabbit.
     * @return rabbit's food value.
     */
    protected int getRabbitFoodValue()
    {
        return RABBIT_FOOD_VALUE;
    }
    
    /**
     * Returns the food value of a single mouse.
     * @return mouse's food value.
     */
    protected int getMouseFoodValue()
    {
        return MOUSE_FOOD_VALUE;
    }
    
    /**
     * Returns the food value of a single squirrel.
     * @return squirrel's food value.
     */
    protected int getSquirrelFoodValue()
    {
        return SQUIRREL_FOOD_VALUE;
    }
    
    /**
     * Applies the effect of a disease to the animal
     * @param disease The disease to be applied
     * @param randomChance The chance of getting infected
     */
    public void applyDisease(String disease, int randomChance)
    {
        int i = getRand().nextInt(100);
        if (i <= randomChance) {
            if (isAlive()) {
                if (disease.equals("Rabies")) {
                    setFoodLevel(getFoodLevel() - 2);
                    setDisease(disease);
                }
            }
        }
    }
    
    /**
     * Checks whether the animal can hunt, given its weather conditions
     * and/or carried diseases
     * @return if the animal can hunt or not.
     */
    protected boolean canHunt(String weather)
    {
        boolean canHunt;
        if (weather == null) {
            canHunt = true;
        }
        else if (weather.equals("Fog")) {
            canHunt = false;
        } else {
            canHunt =  true;
        }
        return canHunt;
    }
}
