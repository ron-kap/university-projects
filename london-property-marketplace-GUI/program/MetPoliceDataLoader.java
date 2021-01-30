import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;
/**
 * Responsible for loading all of the police listings and returning them as an ArrayList 
 *
 * @version 1
 */
public class MetPoliceDataLoader
{
    /**
     * Return an ArrayList containing the rows in the crime data set csv file.
     */
    public ArrayList<MetPoliceListing> load() {
        ArrayList<MetPoliceListing> listings = new ArrayList<MetPoliceListing>();
        try{
            URL url = getClass().getResource("LondonCrime.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String id = line[0];
                String zone = line[1];
                String typeOfCrime = line[2];
                String date = line[3];

                MetPoliceListing listing = new MetPoliceListing(id, zone, typeOfCrime,date);
                listings.add(listing);
            }
        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        return listings;
    }
}
