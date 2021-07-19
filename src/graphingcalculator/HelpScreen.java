/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphingcalculator;

import static graphingcalculator.Console.NAME;
import static graphingcalculator.Recources.VERYDARKGRAY;
import static graphingcalculator.Recources.VERYDARKISHGRAY;
import static graphingcalculator.Recources.normalfont;
import static graphingcalculator.Recources.titlefont;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author jacob
 */
public class HelpScreen
{
    private static boolean alreadyOpen = false;
    
    private static JFrame helpFrame;
    private static JPanel helpPanel;
    private static JTextArea helpTitle;
    private static JTextArea helpScreen;
    
    public static void openHelpWindow(boolean theme, String helpText)
    {
        if (alreadyOpen == false)
        {
            alreadyOpen = true;
            

            //Setup JFrame    
            helpFrame = new JFrame("Help for " + NAME);

        //Setup JFrame
            final int HEIGHT = 700;
            helpFrame.setSize((int) (((double) HEIGHT) * 1.8), HEIGHT);
            helpFrame.setLocationRelativeTo(null);
            helpFrame.setVisible(true);
            helpFrame.setResizable(false);
            helpFrame.addWindowListener(new WindowAdapter()
            {        
                @Override
                public void windowClosing(WindowEvent e)
                {
                    alreadyOpen=false;
                }
            });
            helpPanel = new JPanel();
            helpFrame.add(helpPanel);
            helpPanel.setBackground(Color.WHITE);
            helpTitle = new JTextArea("HELP PAGE FOR " + NAME.toUpperCase());
            helpTitle.setEditable(false);
            helpPanel.add(helpTitle);
            helpTitle.setBounds(0, 0, helpPanel.getWidth(), 50);
            helpTitle.setFont(titlefont);
            helpTitle.setBackground(Color.LIGHT_GRAY);
            helpScreen = new JTextArea(helpText + "\n\nMade by Jacob Adams");
            helpScreen.setEditable(false);
            helpPanel.add(helpScreen);
            helpScreen.setBounds(0, 100, helpPanel.getWidth(), helpPanel.getHeight() - 100);
            helpScreen.setFont(normalfont);
            helpScreen.setBackground(Color.LIGHT_GRAY);
            setTheme(theme);
            
        }
        helpFrame.requestFocus();
    }
    
    public static void setTheme(boolean isDarkTheme)
    {
        if (helpPanel != null)
        {
            if (isDarkTheme)
            {
                helpPanel.setBackground(VERYDARKGRAY);
                helpTitle.setBackground(VERYDARKISHGRAY);
                helpTitle.setForeground(Color.WHITE);
                helpScreen.setBackground(VERYDARKISHGRAY);
                helpScreen.setForeground(Color.WHITE);
            }
            else
            {
                helpPanel.setBackground(Color.WHITE);
                helpTitle.setBackground(Color.LIGHT_GRAY);
                helpTitle.setForeground(Color.BLACK);
                helpScreen.setBackground(Color.LIGHT_GRAY);
                helpScreen.setForeground(Color.BLACK);
            }
        }
    }
}
