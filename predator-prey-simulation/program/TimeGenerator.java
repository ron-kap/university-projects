
/**
 * A time generator. This class is used to calculate time within the simulation.
 *
 * @version 1
 */
public class TimeGenerator
{
    private int tempStep;
    private String time;

    /**
     * Constructor for objects of class TimeGenerator
     */
    public TimeGenerator()
    {
        tempStep = 0;
    }

    /**
     * Calculates whether the Simulation has entered Day
     * or Night phases.
     * @return Day or Night strings.
     */
    public String getDayTime()
    {
        //day cycle = 2/3
        //0-19 = DAY
        //20-29 = NIGHT
        String returnState;
        
        if(tempStep == 30) {
            tempStep = 0;
        }
       
        if(tempStep < 20) {
            //ticks night to day
            returnState = "Day";
        }
        else {
            //ticks day to night
            returnState = "Night";
        } 
        
        time = returnState;
        return returnState;
    }
    
    /**
     * Allows new value for tempStep.
     * @param step New value for tempStep.
     */
    public void setTempStep(int step)
    {
        tempStep = step;
    }
    
    /**
     * Returns the number tempSteps used for tracking time.
     * @return The value tempStep.
     */
    public int getTempStep()
    {
        return tempStep;
    }
    
    /**
     * Adds one to tempStep, ticks time.
     */
    public void incrementTempStep()
    {
        tempStep++;
    }
    
    /**
     * Returns the time of day.
     * @return time of day.
     */
    public String getTime()
    {
        getDayTime();
        return time;
    }
}
