import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.Font.*;

/**
 * WelcomePage class, responsible for initial screen display - Panel 1.
 *
 * @version 1
 */
public class WelcomePage
{
    private JLabel priceLabel;

    /**
     * Constructor for objects of class WelcomePage
     */
    public WelcomePage()
    {
        // empty
    }

    /**
     * Builds the container that holds the data for the inital Welcome
     * Screen (Panel 1).
     *
     * @param minPriceValue The minimum desired price.
     * @param maxPriceValue The maximum desired price.
     * @return  JPanel with the welcome greeting inside.
     */
    public JPanel makeWelcome(int minPriceValue, int maxPriceValue)
    {
        //welcome container
        JPanel welcomeContainer = new JPanel();
        welcomeContainer.setLayout(new BorderLayout());

        //title container
        JPanel titleContainer = new JPanel();
        welcomeContainer.add(titleContainer, BorderLayout.NORTH);
        titleContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        //contents
        JLabel welcomeLabel = new JLabel("Welcome to the Property Finder", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Century Gothic", Font.BOLD, 1));
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(28f)); 
        titleContainer.add(welcomeLabel);

        //instructions container
        JPanel instrucContainer = new JPanel();
        welcomeContainer.add(instrucContainer, BorderLayout.CENTER);
        instrucContainer.setLayout(new GridLayout(4, 1));
        instrucContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        //contents
        //heading
        JLabel instrucLabel = new JLabel("Here are some of the basics of the program:", SwingConstants.CENTER);
        instrucLabel.setFont(new Font("Century Gothic", Font.BOLD, 1));
        instrucLabel.setFont(instrucLabel.getFont().deriveFont(16f));
        instrucContainer.add(instrucLabel);
        //first instruction
        JLabel instrucLabel2 = new JLabel("<html>1) Using the dropdown boxes above select a vaild price range for properties you would like to view, then press the Next Button.</html>", SwingConstants.CENTER);
        instrucLabel2.setFont(new Font("Century Gothic", Font.PLAIN, 1));
        instrucLabel2.setFont(instrucLabel2.getFont().deriveFont(16f));
        instrucContainer.add(instrucLabel2);
        //second instruction
        JLabel instrucLabel3 = new JLabel("<html>2) On the Map Page, click the neighbourhood in which you would like to view proporties.</html>", SwingConstants.CENTER);
        instrucLabel3.setFont(new Font("Century Gothic", Font.PLAIN, 1));
        instrucLabel3.setFont(instrucLabel3.getFont().deriveFont(16f));
        instrucContainer.add(instrucLabel3);
        //third instruction
        JLabel instrucLabel4 = new JLabel("<html>3) Cycle through general statistics, or select a property listing to view more details.</html>", SwingConstants.CENTER);
        instrucLabel4.setFont(new Font("Century Gothic", Font.PLAIN, 1));
        instrucLabel4.setFont(instrucLabel4.getFont().deriveFont(16f));
        instrucContainer.add(instrucLabel4);

        //price label container
        JPanel priceContainer = new JPanel();
        priceContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        priceContainer.setBorder(new EmptyBorder(5, 20, 5, 20));
        welcomeContainer.add(priceContainer, BorderLayout.SOUTH);
        //contents
        priceLabel = new JLabel("Your chosen budget is £" + minPriceValue + " to £" + maxPriceValue);
        if(minPriceValue == maxPriceValue) {
            priceLabel.setText("Your chosen budget is £" + minPriceValue);
        }
        priceLabel.setFont(instrucLabel4.getFont().deriveFont(16f));
        priceContainer.add(priceLabel);

        return welcomeContainer;
    }

    /**
     * Mutator method to allow Price Label change, mainly to blank.
     * @param priceLabelText New text to be displayed
     */
    public void setPriceLabel(String priceLabelText)
    {
        priceLabel.setText(priceLabelText);
    }
}
