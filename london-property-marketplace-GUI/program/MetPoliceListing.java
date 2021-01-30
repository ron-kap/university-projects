
/**
 * Represents one listing of a crime on the crime CSV file.
 * This is essentially one row in the data table. Each column
 * has a corresponding field.
 *
 * @version 1
 */
public class MetPoliceListing
{
    /**
     * The id of the crime
     */
    private String id;
    
    /**
     * The zone that the crime was commited in
     */
    private String zone;
    
    /**
     * The type of crime commited
     */
    private String typeOfCrime;
    
    /**
     * The date of the crime
     */
    private String date;
    
    /**
     * Constructor for objects of class MetPoliceListing
     * 
     * @param id The id of the crime
     * @param zone The zone that the crime was commited in
     * @param typeOfCrime The type of crime commited
     * @param date The date of the crime
     */
    public MetPoliceListing(String id, String zone, String typeOfCrime, String date)
    {
        this.id =id;
        this.zone=zone;
        this.typeOfCrime=typeOfCrime;
        this.date=date;
    }

    public String getId()
    {
        return id;
    }
    
    public String getZone()
    {
        return zone;
    }
    
    public String getTypeOfCrime()
    {
        return typeOfCrime;
    }
    
    public String getDate()
    {
        return date;
    }
}
