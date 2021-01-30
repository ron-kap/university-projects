/**
 * A simple model of all Prey in the simulation, 
 * they are species that are hunted by another for food.
 *
 * @version 1.0
 */
public abstract class Prey extends Animal
{
    // The food value of a single plant. In effect, this is the
    // number of steps a Prey animal can go before it has to eat again.
    private static final int PLANT_FOOD_VALUE = 6;
    // A boolean used to block eating if the prey has anthrax
    private boolean anthraxEffect;

    /**
     * Create a Prey, foundation of all Prey animals -
     * rabbits, mice and squirrels.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Prey(Field field, Location location)
    {
        super(field, location);
        anthraxEffect = false;
    }

    /**
     * Returns the food value of a single plant.
     * @return plant's food value.
     */
    protected int getPlantFoodValue()
    {
        return PLANT_FOOD_VALUE;
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
                if (disease.equals("Anthrax")) {
                    anthraxEffect = true;
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
        if (anthraxEffect == false) {
            canHunt = true;
        } else {
            canHunt = false;
        }
        return canHunt;
    }
}
