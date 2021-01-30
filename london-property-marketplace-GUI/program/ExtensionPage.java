import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * ExtensionPage class, responsible for the creation of the 
 * crime heatmap page - Panel 4.
 *
 * @version 1
 */
public class ExtensionPage
{
    private int minPrice;
    private int maxPrice;
    private ArrayList<MetPoliceListing> data;
    private ArrayList<Integer> NO_ZONES;
    
    /**
     * Constructor for objects of class ExtensionPage
     */
    public ExtensionPage()
    {
        MetPoliceDataLoader loader = new MetPoliceDataLoader();
        data = loader.load();
        
        NO_ZONES = new ArrayList();
        NO_ZONES.add(1);
        NO_ZONES.add(2);
        NO_ZONES.add(6);
        NO_ZONES.add(7);
        NO_ZONES.add(8);
        NO_ZONES.add(40);
        NO_ZONES.add(48);
        NO_ZONES.add(56);
        NO_ZONES.add(64);
        NO_ZONES.add(63);
        NO_ZONES.add(57);
        NO_ZONES.add(49);
    }

    /**
     * Builds the container that holds the data for the 
     * exension Screen (Panel 4).
     */
    public JPanel makeExtension()
    {
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        
        //title container
        JPanel titleContainer = new JPanel();
        container.add(titleContainer, BorderLayout.NORTH);
        titleContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        //contents
        JLabel containerLabel = new JLabel("Crime heatmap of London", SwingConstants.CENTER);
        containerLabel.setFont(new Font("Century Gothic", Font.BOLD, 1));
        containerLabel.setFont(containerLabel.getFont().deriveFont(28f)); 
        titleContainer.add(containerLabel);
        
        //map container
        JPanel mapPanel = new JPanel();
        mapPanel.setLayout(null);
        
        Container grid = new Container();
        grid.setLayout(new GridLayout(8,8));
        grid.setLocation(0,0);
        grid.setSize(1040, 821);
        
        int zoneNum = 1;
        int num = 1;
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JPanel panel = new JPanel();
                if (NO_ZONES.contains(num)) {
                    panel.setBackground(new Color(255,255,255,255));
                    num++;
                } else {
                    Color r = scaleColor(zoneNum);
                    panel.setBackground(r);
                    panel.setLayout(new BorderLayout());
                    
                    Container p = new Container();
                    p.setLayout(null);
                    
                    JButton button = new JButton(""+zoneNum);
                    
                    try {
                        button.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("images/gun.png")).getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
                        button.setSize(50, 30);
                        button.setLocation(100, 70);
                        button.setBorder(BorderFactory.createEmptyBorder());
                        button.setBorderPainted(false);
                        button.setFocusPainted(false);
                        button.setContentAreaFilled(false);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    button.addActionListener(e -> createExtPopUp(button.getText(), data));
                    p.add(button);
                    
                    panel.add(p, BorderLayout.CENTER);
                    
                    zoneNum++;
                    num++;
                }
                grid.add(panel);
            }
        }
        
        mapPanel.add(grid);
        
        ImageIcon mapIcon = new ImageIcon("images/map.jpg");
        Image image = mapIcon.getImage();
        Image newimg = image.getScaledInstance(1040, 821, java.awt.Image.SCALE_SMOOTH);
        mapIcon = new ImageIcon(newimg);
        JLabel mapLabel = new JLabel(mapIcon);
        mapLabel.setSize(1040,821);
        mapLabel.setLocation(0,0);
        mapPanel.add(mapLabel);
        
        container.add(mapPanel);
        
        return container;
    }
    
    /**
     * Creates a colour for the selected zone, using the data in the CSV file
     * 
     * @param zoneNum The zone to use for scaling/shading
     * @return The colour to use for the zone
     */
    public Color scaleColor(int zoneNum) {
        double min = 1;
        double max = 510;
        
        int areaListings = 0;
        
        int r = 0;
        int g = 0;
        int b = 0;
        
        for (MetPoliceListing listing : data) {
            if (Integer.parseInt(listing.getZone()) == zoneNum) {
                areaListings++;
            }
        }
        
        double scaleFactor = (double)((areaListings*255 *10 *6.5)/(data.size()));
        
        if (scaleFactor <= 0) {
            r = 0;
            g = 255;
            b = 0;
        } else if (scaleFactor > max) {
            r = 255;
            g = 0;
            b = 0;
        } else {
            if (scaleFactor > 255) {
                r = 255;
                g = (int)(255 - (scaleFactor - 255));
                b = (int)(255 - (scaleFactor - 255));
            }
            else {
                r = (int)scaleFactor;
                g = 255;
                b = (int)scaleFactor;
            }
        }
        
        return new Color(r, g, b, 126);
    }
    
    /**
     * creates a popup window, to display more information about the
     * crimes in a certain zone.
     * 
     * @param zone The zone to dsiplay information for
     * @param crimeDataset The data that the popup will use
     */
    public void createExtPopUp(String zone, ArrayList<MetPoliceListing> crimeDataset) {
        ExtensionPopUp a = new ExtensionPopUp(zone, crimeDataset);
    }
    
    
}