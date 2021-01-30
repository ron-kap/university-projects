import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing various prey and predators.
 * 
 * @version 1.0
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 150;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 120;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.04;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.12;  
    // The probability that a plant will be created in any given grid position.
    private static final double PLANT_CREATION_PROBABILITY = 0.06; 
    // The probability that a mouse will be created in any given grid position.
    private static final double MOUSE_CREATION_PROBABILITY = 0.14; 
    // The probability that a squirrel will be created in any given grid position.
    private static final double SQUIRREL_CREATION_PROBABILITY = 0.14; 
    // The probability that a owl will be created in any given grid position.
    private static final double OWL_CREATION_PROBABILITY = 0.05; 

    // List of actors in the field.
    private List<Actor> actors;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // Calulate time
    private TimeGenerator timeGen;
    // The weather generator that stores and generates types of weather.
    private WeatherGenerator weatherGen;
    // The current type of weather in the simulation.
    private String currentWeather;
    // The step at which the current type of weather will expire.
    private int weatherDuration;
    // The disease generator that stores and generates diseases.
    private DiseaseGenerator diseaseGen;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        actors = new ArrayList<>();
        field = new Field(depth, width);
        timeGen = new TimeGenerator();
        weatherGen = new WeatherGenerator();
        diseaseGen = new DiseaseGenerator();

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.RED);
        view.setColor(Fox.class, Color.BLUE);
        view.setColor(Plant.class, Color.GREEN);
        view.setColor(Mouse.class, Color.PINK);
        view.setColor(Squirrel.class, Color.MAGENTA);
        view.setColor(Owl.class, Color.CYAN);
        view.setColor(Bigfoot.class, Color.BLACK);
        view.setColor(Zombie.class, Color.YELLOW);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            //delay(120);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;
        // Artificial step for time
        timeGen.incrementTempStep();

        // chance to generate a disease every 100 steps
        if ((step % 100) == 1) {
            String disease = diseaseGen.generateDisease(10);
            if (disease != null) {
                applyDisease(disease, actors);
            }
        }
        
        // Provide space for newborn actors.
        List<Actor> newActors = new ArrayList<>();        
        // Let all actors act.
        for(Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
            Actor actor = it.next();
            actor.act(newActors, currentWeather, timeGen.getDayTime());
            if(! actor.isAlive()) {
                it.remove();
            }
        }
               
        // Add the newly born actors to the main lists.
        actors.addAll(newActors);
        
        // chance to generate a type of weather every 25 steps
        if ((step % 25) == 1) {
            currentWeather = weatherGen.generateWeather(10);
            if (currentWeather != null) {
                weatherDuration = step + weatherGen.getWeatherDuration();
            }
        }
        
        // spawn a cluster of plants every day cycle (steps)
        if ((step % 10) == 1) {
            spawnRandomPlants();
        }
        
        // sets the weather to null when the current weather expires
        if ((currentWeather != null) && (step >=weatherDuration)) {
            currentWeather = null;
        }
        
        view.showStatus(step, field, timeGen.getDayTime(), currentWeather);
    }
    
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        timeGen.setTempStep(0);
        currentWeather = null;
        actors.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field, timeGen.getDayTime(), currentWeather);
    }
    
    /**
     * Randomly populate the field with actors.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location);
                    actors.add(fox);
                }
                else if(rand.nextDouble() <= OWL_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Owl owl = new Owl(true, field, location);
                    actors.add(owl);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location);
                    actors.add(rabbit);
                }
                else if(rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Plant plant = new Plant(true, field, location);
                    actors.add(plant);
                }
                else if(rand.nextDouble() <= MOUSE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Mouse mouse = new Mouse(true, field, location);
                    actors.add(mouse);
                }
                else if(rand.nextDouble() <= SQUIRREL_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Squirrel squirrel = new Squirrel(true, field, location);
                    actors.add(squirrel);
                }
                // else leave the location empty.
            }
        }
        
        // Spawns one Bigfoot entity randomly on field, in an empty location.
        Location location = new Location(rand.nextInt(field.getDepth()), rand.nextInt(field.getWidth()));
        while (field.getObjectAt(location) != null) {
            location = new Location(rand.nextInt(field.getDepth()), rand.nextInt(field.getWidth()));
        }
        Bigfoot bigfoot = new Bigfoot(false, field, location);
        actors.add(bigfoot);
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    
    /**
     * Applies a generated disease to the affected actors
     * @param disease The disease to be applied
     * @param actors The list of actors in the simulation
     */
    private void applyDisease(String disease, List<Actor> actors)
    {
        if (disease.equals("Rabies")) {
            for (Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
                Actor actor = it.next();
                if (actor instanceof Predator) {
                    actor.applyDisease(disease, 10);
                }
            }
        }
       
        if (disease.equals("Anthrax")) {
            for (Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
                Actor actor = it.next();
                if (actor instanceof Prey) {
                    actor.applyDisease(disease, 10);
                }
            }
        }
        
        if (disease.equals("Element 115 Exposure")) {
            List<Actor> newZombies = new ArrayList<>();
            for (Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
                Actor actor = it.next();
                if (actor instanceof Animal && !(actor instanceof Bigfoot) && !(actor instanceof Zombie)) {
                    actor.applyZombieDisease(1, newZombies);
                    if(!actor.isAlive()) {
                        it.remove();
                    }
                }
            }
            actors.addAll(newZombies);
            
            boolean bigfootPresent = false;
            for (Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
                Actor actor = it.next();
                if (actor instanceof Bigfoot) {
                    bigfootPresent = true;
                }
            }
            
            Random rand = new Random();
            if (bigfootPresent == false) {
                Location location = new Location(rand.nextInt(field.getDepth()), rand.nextInt(field.getWidth()));
                while (field.getObjectAt(location) != null) {
                    location = new Location(rand.nextInt(field.getDepth()), rand.nextInt(field.getWidth()));
                }
                Bigfoot bigfoot = new Bigfoot(false, field, location);
                actors.add(bigfoot);
            }
        }
    }
    
    /**
     * Creates five plant objects and disperses them to 
     * empty locations within the field.
     */
    private void spawnRandomPlants()
    {
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            Location location = new Location(rand.nextInt(field.getDepth()), rand.nextInt(field.getWidth()));
            while (field.getObjectAt(location) != null) {
                location = new Location(rand.nextInt(field.getDepth()), rand.nextInt(field.getWidth()));
            }
            Plant plant = new Plant(false, field, location);
            actors.add(plant);
        }
    }
}
