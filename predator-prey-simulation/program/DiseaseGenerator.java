import java.util.Random;

/**
 * A disease generator. This class is used to store
 * the diseases of the simulation and generate them randomly.
 *
 * @version 1
 */
public class DiseaseGenerator
{
    // the diseases in the simulation
    private static final String[] DISEASES = {"Rabies", "Anthrax", "Element 115 Exposure"};
    
    /**
     * Create a disease generator
     */
    public DiseaseGenerator()
    {
        
    }

    /**
     * Generates a disease.
     * Uses the randomChance parameter as the percentage chance
     * of generating a disease, then randomly selects a disease
     * from the array.
     *
     * @param  randomChance the chance of a disease being generated
     * @return the disease to be applied to the simulation
     */
    public String generateDisease(int randomChance)
    {
        Random rand = new Random();
        int i1 = rand.nextInt(100);
        if (i1 <= randomChance) {
            int i2 = rand.nextInt(DISEASES.length);
            String disease = DISEASES[i2];
            return disease;
        }
        else {
            return null;
        }
    }
}
