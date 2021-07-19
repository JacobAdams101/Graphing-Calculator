
package graphingcalculator;

import javax.swing.JPanel;

/**
 *
 * @author jacob
 */
public class GraphingCalculator extends JPanel
{
    
    static int numberOfLines = 0;
    
    public static Console console;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //HelpScreen.openHelpWindow();
        
        console = new Console();
        while (true)
        {
            console.tick();
        }
    }
}
