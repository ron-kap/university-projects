import java.util.Random;

/**
 * A weather generator. This class is used to store
 * the types of weather in the simulation and generate them randomly.
 *
 * @version 1.0
 */
public class WeatherGenerator
{
    // the types of weather in the simulation
    private static final String[] WEATHER_TYPES = {"Snow", "Fog"};
    // the duration of the types of weather
    private int weatherDuration = 5;

    /**
     * Creates a weather generator
     */
    public WeatherGenerator()
    {
        
    }

    /**
     * Generates weather.
     * Uses the randomChance parameter as the percentage chance
     * of generating a type of weather, then randomly selects a
     * type of weather from the array.
     *
     * @param  randomChance the chance of a type of weather being generated
     * @return the type of weather to be applied to the simulation
     */
    public String generateWeather(int randomChance)
    {
        Random rand = new Random();
        int i1 = rand.nextInt(100);
        if (i1 <= randomChance) {
            int i2 = rand.nextInt(WEATHER_TYPES.length);
            String weather = WEATHER_TYPES[i2];
            return weather;
        }
        else {
            return null;
        }
    }
    
    /**
     * Return the duration of all types of weather.
     * @return the duration of all types of weather.
     */
    public int getWeatherDuration()
    {
        return weatherDuration;
    }
}
