/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphingcalculator;


import graphingcalculator.Calculator.Complex;
import graphingcalculator.Calculator.Equation;
import graphingcalculator.Calculator.NotationType;
import graphingcalculator.Calculator.Graph;
import static graphingcalculator.Calculator.stringToEquation;
import static graphingcalculator.Calculator.stringToExpression;
import static graphingcalculator.Recources.VERYDARKGRAY;
import static graphingcalculator.Recources.VERYDARKISHGRAY;
import static graphingcalculator.Recources.mathsfont;
import static graphingcalculator.Recources.smallerfont;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 *
 * @author jacob
 */
public class Console
{
    
    public final static String NAME = "Calc Thing V0.9.5 (NOW WITH DARK THEME)";
    private JFrame f;
    private JPanel panel;
    private JTextArea input;
    private JTextArea output;
    private JButton doneButton;
    private JButton clearButton;
    private JButton clearGraph;
    private JButton saveGraph;
    private JButton helpButton;
    private JScrollPane scrollBar;
    private JLabel graph;
    
    private static ArrayList<String> queuedCommands = new ArrayList<>();

    public String getHelp()
    {
        String result = "";
        for (Command c : COMMANDS)
        {
            result += c.printCommand() + "\n";
        }
        return result;
    }
    
    
    private boolean theme = false;
    
    public void setTheme(boolean isDarkTheme)
    {
        theme = isDarkTheme;
        if (isDarkTheme)
        {
            panel.setBackground(VERYDARKGRAY);
            input.setBackground(VERYDARKISHGRAY);
            input.setForeground(Color.WHITE);
            output.setBackground(VERYDARKISHGRAY);
            output.setForeground(Color.WHITE);
        }
        else
        {
            panel.setBackground(Color.WHITE);
            input.setBackground(Color.LIGHT_GRAY);
            input.setForeground(Color.BLACK);
            output.setBackground(Color.LIGHT_GRAY);
            output.setForeground(Color.BLACK);
        }
        HelpScreen.setTheme(theme);
    }
    
    public synchronized void tick()
    {
        if (queuedCommands.size() >= 1 && queuedCommands.get(0) != null)
        {
            Result result = run(queuedCommands.get(0));
            if (result.TEXT != null)
            {
                writeLine(result.TEXT);
            }
            if (result.IMAGE != null)
            {
                changeImage(result.IMAGE);
            }
            queuedCommands.remove(0);
        }
    }

    public Console()
    {
    //Setup JFrame    
        f = new JFrame(NAME);
        
    //Setup JFrame
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final int HEIGHT = 900;
        f.setSize((int) (((double) HEIGHT) * 1.8), HEIGHT);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setResizable(true);
        panel = new JPanel(null);
        f.add(panel);
        panel.setBackground(Color.WHITE);
        BufferedImage icon = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);


        // Create a graphics which can be used to draw into the buffered image
        Graphics2D g = icon.createGraphics();
        g.setStroke(new BasicStroke(20));
        
        g.setFont(new Font("Impact", Font.BOLD, 180));
        g.setColor(Color.BLACK);
        g.drawString("C", 25, 170);
        g.setColor(Color.WHITE);
        g.drawString("C", 40, 155);
        g.setColor(Color.RED);   
        final int STEP = 2;
        for (int i = 0; i < icon.getWidth(); i += STEP) 
        {
            g.drawLine(i, icon.getHeight()-10-((i-(icon.getWidth()/2))/5)*((i-(icon.getWidth()/2))/5), i+STEP, icon.getHeight()-10-((i+STEP-(icon.getWidth()/2))/5)*((i+STEP-(icon.getWidth()/2))/5));
        }
        
        
        f.setIconImage(icon);
        
    
    //Setup Objects/Buttons/TextFeilds etc.

        
        input = new JTextArea("INPUT COMMAND HERE");
        
        input.setEditable(true);
        input.setLineWrap(true);
        panel.add(input);
        input.setBounds(panel.getHeight(), panel.getHeight() - 70, panel.getWidth() - panel.getHeight(), 50);
        input.setFont(mathsfont);
        input.setBackground(Color.LIGHT_GRAY);
        
        input.addKeyListener(new KeyListener()
        {
 
            @Override
            public void keyTyped(KeyEvent event)
            {
                //System.out.println("key typed");
            }

            @Override
            public void keyReleased(KeyEvent event)
            {
                //System.out.println("key released");
            }

            @Override
            public void keyPressed(KeyEvent event)
            {
                //System.out.println("key pressed");
                //System.out.println(event.getKeyCode());
                if (event.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    queuedCommands.add(input.getText().replace("\n", ""));
                    clearInputLine();
                    event.consume();
                }
                else if (event.getKeyCode() == KeyEvent.VK_TAB)
                {
                    
                    String suggestion = getSuggestion(input.getText());
                    if (suggestion != null)
                    {
                        //System.out.println("Suggestion: " + suggestion);
                        input.append(suggestion);
                        input.select(input.getText().length() - suggestion.length(), input.getText().length());
                        
                    }
                    event.consume();
                    
                }
            }
        });
        
         
        output = new JTextArea("type \"help\" to open the help menu\n");
        output.setEditable(false);
        
        output.setFont(mathsfont);
        output.setLineWrap(true);
        output.setBackground(Color.LIGHT_GRAY);
        scrollBar = new JScrollPane(output,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollBar.setBorder(BorderFactory.createEmptyBorder());
        scrollBar.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollBar.setBounds(panel.getHeight(), 0, panel.getWidth() - panel.getHeight(), panel.getHeight() - (input.getHeight() + 50));
        panel.add(scrollBar);
        //panel.add(output);
        
        
        doneButton = new JButton("GO");
        doneButton.setFont(smallerfont);
        doneButton.setBackground(Color.LIGHT_GRAY);
        panel.add(doneButton);
        doneButton.setBounds(panel.getHeight() - 70, panel.getHeight() - 70, 70, 50);
        doneButton.addActionListener(new ActionListener() 
        {
            //Button Action Listener
            @Override
            public void actionPerformed(ActionEvent e)
            {
                queuedCommands.add(input.getText().replace("\n", ""));
                clearInputLine();
            }
        });
        
        clearButton = new JButton("CLEAR");
        clearButton.setFont(smallerfont);
        clearButton.setBackground(Color.LIGHT_GRAY);
        panel.add(clearButton);
        clearButton.setBounds(panel.getHeight() - 150, panel.getHeight() - 70, 80, 50);
        clearButton.addActionListener(new ActionListener() 
        {
            //Button Action Listener
            @Override
            public void actionPerformed(ActionEvent e)
            {
                clearOutput();
            }
        });
        helpButton = new JButton("HELP");
        helpButton.setFont(smallerfont);
        helpButton.setBackground(Color.LIGHT_GRAY);
        panel.add(helpButton);
        helpButton.setBounds(panel.getHeight() - 230, panel.getHeight() - 70, 80, 50);
        helpButton.addActionListener(new ActionListener() 
        {
            //Button Action Listener
            @Override
            public void actionPerformed(ActionEvent e)
            {
                HelpScreen.openHelpWindow(theme, getHelp());
            }
        });
        clearGraph = new JButton("NEW GRAPH");
        clearGraph.setFont(smallerfont);
        clearGraph.setBackground(Color.LIGHT_GRAY);
        panel.add(clearGraph);
        clearGraph.setBounds(0, 0, 140, 50);
        clearGraph.addActionListener(new ActionListener() 
        {
            //Button Action Listener
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Color bg;
                if (GraphingCalculator.console.theme)
                {
                    bg = VERYDARKGRAY;
                }
                else
                {
                    bg = Color.WHITE;
                }
                if (currentGraph != null)
                {
                    currentGraph = new Graph(currentGraph.XMIN, currentGraph.XMAX, currentGraph.YMIN, currentGraph.YMAX, graph.getWidth(), graph.getHeight(), bg);
                }
                else
                {
                    currentGraph = new Graph(-10, 10, -10, 10, graph.getWidth(), graph.getHeight(), bg);
                }
                changeImage(currentGraph.GRAPH);
            }
        });
        saveGraph = new JButton("SAVE GRAPH");
        saveGraph.setFont(smallerfont);
        saveGraph.setBackground(Color.LIGHT_GRAY);
        panel.add(saveGraph);
        saveGraph.setBounds(140, 0, 140, 50);
        saveGraph.addActionListener(new ActionListener() 
        {
            //Button Action Listener
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (currentGraph != null)
                {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy MM dd HH mm ss");  
                    LocalDateTime now = LocalDateTime.now();  
                    currentGraph.saveGraph("graph " + dtf.format(now));
                }
            }
        });
        graph = new JLabel();
        panel.add(graph);
        graph.setBounds(0, 0, panel.getHeight(), panel.getHeight());
        graph.setBackground(Color.LIGHT_GRAY);
        
        input.requestFocus();
        input.selectAll();
        
        panel.addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                input.setBounds(panel.getHeight(), panel.getHeight() - 70, panel.getWidth() - panel.getHeight(), 50);
                scrollBar.setBounds(panel.getHeight(), 0, panel.getWidth() - panel.getHeight(), panel.getHeight() - (input.getHeight() + 50));
                doneButton.setBounds(panel.getHeight() - 70, panel.getHeight() - 70, 70, 50);
                clearButton.setBounds(panel.getHeight() - 150, panel.getHeight() - 70, 80, 50);
                helpButton.setBounds(panel.getHeight() - 230, panel.getHeight() - 70, 80, 50);
                graph.setBounds(0, 0, panel.getHeight(), panel.getHeight());
            }
        });
        
        setTheme(true); //Because dark theme is the one true theme :)
        
        COMMANDS = setupCommands();
    }
    private void clearOutput()
    {
        output.setText("");
    }
    private String getSuggestion(String text)
    {
        for (Command x : COMMANDS)
        {
            String result = x.matches(text);
            if (result != null)
            {
                return result.substring(text.length(), result.length());
            }
        }
        return null;
    }
    
    public static class Result
    {
        public final String TEXT;
        public final BufferedImage IMAGE;
        public Result(String text, BufferedImage image)
        {
            this.TEXT = text;
            this.IMAGE = image;
        }
    }
    
    public static abstract class Command
    {        
        final String[] KEYWORDS;
        final String[] ARGUMENTS;
        public Command(String[] keywords, String[] arguments)
        {
            this.KEYWORDS = keywords;
            this.ARGUMENTS = arguments;
        }
        public boolean isCommand(String command)
        {
            command = command.toLowerCase();
            for (String x : KEYWORDS)
            {
                if (x.equals(command))
                {
                    return true;
                }
            }
            return false;
        }
        public String matches(String command)
        {
            
            if (command.length() == 0)
            {
                return null;
            }
            command = command.toLowerCase();
            for (String x : KEYWORDS)
            {
                if (x.length() >= command.length())
                {
                    if (x.substring(0, command.length()).equals(command))
                    {
                        return x;
                    }
                }
            }
            return null;
        }
        public abstract Result run(String[][] arguments);
        
        int getArgumentCount(String[][] arguments)
        {
            return arguments[arguments.length-1].length;
        }
        
        String[] getMyArguments(String[][] arguments, int size)
        {
            return arguments[size-1];
        }
        /**
         * Prints our the command
         * @return 
         */
        public String printCommand()
        {
            String result = KEYWORDS[0];
            int i;
            for (i = 0; i < ARGUMENTS.length; i++)
            {
                result += " [" + ARGUMENTS[i] + "]";
            }
            return result;
        }
    }
    
    public Graph currentGraph;
    public LastAnswer lastAnswer = new LastAnswer();
    
    public NotationType currentNotation = NotationType.INFIX;
    
    private final Command[] COMMANDS;
    
    public final Command[] setupCommands()
    {        
        return new Command[]
        {
            new Command(new String[] {"simplify", "simp"}, new String[] {"Equation/Expression"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) == 1)
                    {
                        if (lastAnswer.isEquation())
                        {
                            lastAnswer.set(lastAnswer.lastEquation.simplify());
                            return new Result(lastAnswer.lastEquation.write(currentNotation), null);
                        }
                        else if (lastAnswer.isExpression())
                        {
                            lastAnswer.set(lastAnswer.lastExpression.Simplify());
                            return new Result(lastAnswer.lastExpression.write(currentNotation), null);
                        }
                    }
                    else //If any other number assume 2 arguments
                    {
                        String[] myArguments = getMyArguments(arguments, 2);
                        if (myArguments[1].contains("=") || myArguments[1].contains(">") || myArguments[1].contains("<"))
                        {
                            lastAnswer.set(stringToEquation(myArguments[1], currentNotation).simplify());
                            return new Result(lastAnswer.lastEquation.write(currentNotation), null);
                        }
                        else
                        {
                            lastAnswer.set(stringToExpression(myArguments[1], currentNotation).Simplify());
                            return new Result(lastAnswer.lastExpression.write(currentNotation), null);
                        }
                    }
                    return new Result("Could not simplify with the given parameters!", null);
                }
            },
            new Command(new String[] {"let", "dim"}, new String[] {"Equation/Expression"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) >= 2)
                    {
                        String[] myArguments = getMyArguments(arguments, 2);
                        if (myArguments[1].contains("=") || myArguments[1].contains(">") || myArguments[1].contains("<"))
                        {
                            lastAnswer.set(stringToEquation(myArguments[1], currentNotation));
                            return new Result(lastAnswer.lastEquation.write(currentNotation), null);
                        }
                        else
                        {
                            lastAnswer.set(stringToExpression(myArguments[1], currentNotation));
                            return new Result(lastAnswer.lastExpression.write(currentNotation), null);
                        }
                    }
                    return new Result("Could not caclulate the value with the given parameters!", null);
                }
            },
            new Command(new String[] {"value", "valueof", "calc", "calculate", "result", "resultof"}, new String[] {"Equation/Expression"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) == 1)
                    {
                        if (lastAnswer.isExpression())
                        {
                            lastAnswer.set(lastAnswer.lastExpression.valueOf());
                            return new Result(lastAnswer.lastValue[0].write(), null);
                        }
                    }
                    if (getArgumentCount(arguments) >= 2)
                    {
                        String[] myArguments = getMyArguments(arguments, 2);
                        lastAnswer.set(stringToExpression(myArguments[1], currentNotation).valueOf());
                        return new Result(lastAnswer.lastValue[0].write(), null);
                    }
                    return new Result("Could not caclulate the value with the given parameters!", null);
                }
            },
            new Command(new String[] {"solveequation", "solve", "solutions", "findsolutions"}, new String[] {"Variable to Solve For", "Equation"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) == 2)
                    {
                        String[] myArguments = getMyArguments(arguments, 2);
                        if (lastAnswer.isEquation())
                        {
                            Complex[] values = lastAnswer.lastEquation.solve(myArguments[1]);
                            if (values.length > 0)
                            {
                                lastAnswer.set(values);
                                return new Result(printArray(values), null);
                            }
                            return new Result("No solutions found!", null);
                        }
                    }
                    else if (getArgumentCount(arguments) >= 3)
                    {
                        String[] myArguments = getMyArguments(arguments, 3);
                        Complex[] values = stringToEquation(myArguments[2], currentNotation).solve(myArguments[1]);
                        if (values.length > 0)
                        {
                            lastAnswer.set(values[0]);
                            return new Result(printArray(values), null);
                        }
                        return new Result("No solutions found!", null);
                    }

                    return new Result("Could not solve with the given parameters!", null);
                }
            },
            new Command(new String[] {"solveexpression", "roots", "findroots", "expressionroots"}, new String[] {"Variable to Solve For", "Expression"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) == 2)
                    {
                        String[] myArguments = getMyArguments(arguments, 2);
                        if (lastAnswer.isExpression())
                        {
                            Complex[] values = lastAnswer.lastExpression.findMyRoots(myArguments[1]);
                            if (values.length > 0)
                            {
                                lastAnswer.set(values);
                                return new Result(printArray(values), null);
                            }
                            return new Result("No solutions found!", null);
                        }
                    }
                    if (getArgumentCount(arguments) >= 3)
                    {
                        String[] myArguments = getMyArguments(arguments, 3);
                        Complex[] values = stringToExpression(myArguments[2], currentNotation).findMyRoots(myArguments[1]);
                        if (values.length > 0)
                        {
                            lastAnswer.set(values[0]);
                            return new Result(printArray(values), null);
                        }
                        return new Result("No solutions found!", null);
                    }

                    return new Result("Could not solve with the given parameters!", null);
                }
            },
            new Command(new String[] {"maclaurin", "maclaurinexpansion"}, new String[] {"Variable to Expand", "Max Power", "Expression"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) >= 4)
                    {
                        String[] myArguments = getMyArguments(arguments, 4);
                        lastAnswer.set(stringToExpression(myArguments[3], currentNotation).taylorSeries(new Complex(), myArguments[1], Integer.parseInt(myArguments[2])));
                        return new Result(lastAnswer.lastExpression.write(currentNotation), null);
                    }
                    else if (getArgumentCount(arguments) == 3)
                    {
                        String[] myArguments = getMyArguments(arguments, 3);
                        if (lastAnswer.isExpression())
                        {
                            lastAnswer.set(lastAnswer.lastExpression.taylorSeries(new Complex(), myArguments[1], Integer.parseInt(myArguments[2])));
                            return new Result(lastAnswer.lastExpression.write(currentNotation), null);
                        }
                    }
                    return new Result("Could not perform the taylor series with given parameters!", null);
                }
            },
            new Command(new String[] {"taylorexpansion", "taylorseries", "taylor"}, new String[] {"Starting Position", "Variable to Expand", "Max Power", "Expression"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) >= 5)
                    {
                        String[] myArguments = getMyArguments(arguments, 5);
                        lastAnswer.set(stringToExpression(myArguments[4], currentNotation).taylorSeries(new Complex(Double.parseDouble(myArguments[1])), myArguments[2], Integer.parseInt(myArguments[3])));
                        return new Result(lastAnswer.lastExpression.write(currentNotation), null);
                    }
                    else if (getArgumentCount(arguments) == 4)
                    {
                        String[] myArguments = getMyArguments(arguments, 4);
                        if (lastAnswer.isExpression())
                        {
                            lastAnswer.set(lastAnswer.lastExpression.taylorSeries(new Complex(Double.parseDouble(myArguments[1])), myArguments[2], Integer.parseInt(myArguments[3])));
                            return new Result(lastAnswer.lastExpression.write(currentNotation), null);
                        }
                    }
                    return new Result("Could not perform the taylor series with given parameters!", null);
                }
            },
            new Command(new String[] {"newgraph", "creategraph", "replacegraph"}, new String[] {"Min X", "Max X", "Min Y", "Max Y"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) >= 5)
                    {
                        String[] myArguments = getMyArguments(arguments, 5);
                        Color bg;
                        if (GraphingCalculator.console.theme)
                        {
                            bg = VERYDARKGRAY;
                        }
                        else
                        {
                            bg = Color.WHITE;
                        }
                        currentGraph = new Graph(Double.parseDouble(myArguments[1]), Double.parseDouble(myArguments[2]), Double.parseDouble(myArguments[3]), Double.parseDouble(myArguments[4]), graph.getWidth(), graph.getHeight(), bg);
                        return new Result("New Graph Created", currentGraph.GRAPH);
                    }
                    return new Result("Could not make a new graph with given parameters", null);
                }
            },
            new Command(new String[] {"savegraph", "saveplot"}, new String[] {"File Name"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) == 2)
                    {
                        String[] myArguments = getMyArguments(arguments, 2);
                        currentGraph.saveGraph(myArguments[1]);
                        return new Result("Graph Saved As " + myArguments[1], null);
                    }
                    return new Result("Could not save graph with given inputs", null);
                }
            },
            new Command(new String[] {"differentiate", "diff", "differentialof", "differential"}, new String[] {"With Respect To", "Function Value (e.g. y=f(x))", "Expression/Equation"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) == 3)
                    {
                        String[] myArguments = getMyArguments(arguments, 3);
                        if (lastAnswer.isEquation())
                        {
                            lastAnswer.set(lastAnswer.lastEquation.differentiate(myArguments[1], myArguments[2]).simplify());
                            return new Result(lastAnswer.lastEquation.write(currentNotation), null);
                        }
                        if (lastAnswer.isExpression())
                        {
                            lastAnswer.set(lastAnswer.lastExpression.differentiate(myArguments[1], myArguments[2], new ArrayList<Calculator.Substitution>()).Simplify());
                            return new Result(lastAnswer.lastExpression.write(currentNotation), null);
                        }
                    }
                    if (getArgumentCount(arguments) >= 4)
                    {
                        String[] myArguments = getMyArguments(arguments, 4);
                        if (myArguments[3].contains("=") || myArguments[3].contains(">") || myArguments[3].contains("<"))
                        {
                            lastAnswer.set(stringToEquation(myArguments[3], currentNotation).differentiate(myArguments[1], myArguments[2]).simplify());
                            return new Result(lastAnswer.lastEquation.write(currentNotation), null);
                        }
                        else
                        {
                            lastAnswer.set(stringToExpression(myArguments[3], currentNotation).differentiate(myArguments[1], myArguments[2], new ArrayList<>()).Simplify());
                            return new Result(lastAnswer.lastExpression.write(currentNotation), null);
                        }
                    }
                    return new Result("Could not differetiate with given parameters!", null);
                }
            },
            new Command(new String[] {"graphequation", "graphe", "graph", "graphto"}, new String[]{"X Axis Variable", "Y Axis Variable", "Equation"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    //currentGraph = new Graph(-10, 10, -10, 10, 500, 500);
                    
                    if (getArgumentCount(arguments) == 3)
                    {
                        String[] myArguments = getMyArguments(arguments, 3);
                        if (lastAnswer.isEquation())
                        {
                            if (lastAnswer.lastEquation != null)
                            {
                                lastAnswer.lastEquation.graphTo(currentGraph, Color.RED, myArguments[1], false, myArguments[2], false);
                                return new Result("Equation Graphed!", currentGraph.GRAPH);
                            }
                        }
                    }
                    else if (getArgumentCount(arguments) >= 4)
                    {
                        
                        String[] myArguments = getMyArguments(arguments, 4);
                        Equation eq = stringToEquation(myArguments[3], currentNotation);
                        if (eq != null)
                        {
                            eq.graphTo(currentGraph, Color.RED, myArguments[1], false, myArguments[2], false);
                            return new Result("Equation Graphed!", currentGraph.GRAPH);
                        }
                    }
                    return new Result("Could not graph anything", null);
                }
            },
            new Command(new String[] {"graphpolarequation", "graphpolar", "polar", "graphpolarto"}, new String[]{"Angle Variable", "Radius Variable", "Equation"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    //currentGraph = new Graph(-10, 10, -10, 10, 500, 500);
                    
                    if (getArgumentCount(arguments) == 3)
                    {
                        String[] myArguments = getMyArguments(arguments, 3);
                        if (lastAnswer.isEquation())
                        {
                            if (lastAnswer.lastEquation != null)
                            {
                                lastAnswer.lastEquation.graphPolarTo(currentGraph, Color.RED, myArguments[1], myArguments[2]);
                                return new Result("Equation Graphed!", currentGraph.GRAPH);
                            }
                        }
                    }
                    else if (getArgumentCount(arguments) >= 4)
                    {
                        
                        String[] myArguments = getMyArguments(arguments, 4);
                        Equation eq = stringToEquation(myArguments[3], currentNotation);
                        if (eq != null)
                        {
                            eq.graphPolarTo(currentGraph, Color.RED, myArguments[1], myArguments[2]);
                            return new Result("Equation Graphed!", currentGraph.GRAPH);
                        }
                    }
                    return new Result("Could not graph anything", null);
                }
            },
            new Command(new String[] {"graphcomplexequation", "graphecomplex", "graphcomplex", "graphcomplexto"}, new String[]{"Variable to Plot Real and Complex", "Equation"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    //currentGraph = new Graph(-10, 10, -10, 10, 500, 500);
                    
                    if (getArgumentCount(arguments) == 2)
                    {
                        String[] myArguments = getMyArguments(arguments, 2);
                        if (lastAnswer.isEquation())
                        {
                            if (lastAnswer.lastEquation != null)
                            {
                                lastAnswer.lastEquation.graphTo(currentGraph, Color.RED, myArguments[1], false, myArguments[1], true);
                                return new Result("Equation Graphed!", currentGraph.GRAPH);
                            }
                        }
                    }
                    else if (getArgumentCount(arguments) >= 3)
                    {
                        String[] myArguments = getMyArguments(arguments, 3);
                        Equation eq = stringToEquation(myArguments[2], currentNotation);
                        if (eq != null)
                        {
                            eq.graphTo(currentGraph, Color.RED, myArguments[1], false, myArguments[1], true);
                            return new Result("Equation Graphed!", currentGraph.GRAPH);
                        }
                    }
                    return new Result("Could not graph anything", null);
                }
            },
            new Command(new String[] {"plotpoints", "plotcomplex", "plot"}, new String[]{})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) == 1)
                    {
                        if (lastAnswer.isValue())
                        {
                            for (Complex value : lastAnswer.lastValue)
                            {
                                new Calculator.Value(value).graphTo(currentGraph, Color.BLUE);
                            }
                            return new Result("Value Plotted!", currentGraph.GRAPH);
                        }
                    }
                    return new Result("Cannot plot point", null);
                }
            },
            new Command(new String[] {"setnotation", "notation"}, new String[]{"Notation Type"})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) == 2)
                    {
                        String[] myArguments = getMyArguments(arguments, 2);
                        switch (myArguments[1].toLowerCase())
                        {
                            case "prefix":
                                currentNotation = NotationType.PREFIX;
                                return new Result("Notation Set!", null);
                            case "infix":
                                currentNotation = NotationType.INFIX;
                                return new Result("Notation Set!", null);
                            case "postfix":
                                currentNotation = NotationType.POSTFIX;
                                return new Result("Notation Set!", null);
                            default:
                                break;
                        }
                        return new Result("Invalid Notation! TRY: prefix, infix, postfix", null);
                    }
                    return new Result("Could not set notation with given inputs", null);
                }
            },
            new Command(new String[] {"settheme", "theme"}, new String[]{})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    if (getArgumentCount(arguments) == 2)
                    {
                        String[] myArguments = getMyArguments(arguments, 2);
                        if (myArguments[1].toLowerCase().equals("dark"))
                        {
                            GraphingCalculator.console.setTheme(true);
                            return new Result("Set theme", null);
                        }
                        else if (myArguments[1].toLowerCase().equals("light"))
                        {
                            GraphingCalculator.console.setTheme(false);
                            return new Result("Set theme", null);
                        }
                    }
                    return new Result("Could not set theme", null);
                }
            },
            new Command(new String[] {"help"}, new String[]{})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    HelpScreen.openHelpWindow(theme, getHelp());
                    return new Result("Opening help window", null);
                }
            },
            new Command(new String[] {"clear"}, new String[]{})
            {
                @Override
                public synchronized Result run(String[][] arguments)
                {
                    GraphingCalculator.console.clearOutput();
                    return new Result("Cleared Output!", null);
                }
            }
        };
    }
    
    public static class LastAnswer
    {
        public Complex lastValue[];
        public Calculator.Equation lastEquation;
        public Calculator.Term lastExpression;
        
        public LastAnswer()
        {
            lastValue = null;
            lastEquation = null;
            lastExpression = null;
        }
        public void set(Complex x)
        {
            this.lastValue = new Complex[]{x};
            this.lastEquation = null;
            this.lastExpression = null;
        }
        public void set(Complex[] x)
        {
            this.lastValue = x;
            this.lastEquation = null;
            this.lastExpression = null;
        }
        public void set(Calculator.Equation x)
        {
            this.lastEquation = x;
            this.lastValue = null;
            this.lastExpression = null;
        }
        public void set(Calculator.Term x)
        {
            this.lastExpression = x;
            this.lastValue = null;
            this.lastEquation = null;
        }
        public boolean isEquation()
        {
            return lastEquation != null;
        }
        public boolean isExpression()
        {
            return lastExpression != null;
        }
        public boolean isValue()
        {
            return lastValue != null;
        }
        public boolean isSingleValue()
        {
            return lastValue.length == 1;
        }
    }
    /**
     * Test
     * @param command
     * @return 
     */
    public synchronized Result run(String command)
    {
        
        String[][] arguments = new String[5][];
        int i;
        for (i = 0; i < arguments.length; i++)
        {
            arguments[i] = command.split(" ", i+1);
        }
        
        for (Command c : COMMANDS)
        {
            if (c.isCommand(arguments[1][0]))
            {
                try
                {
                    return c.run(arguments);
                }
                catch (Exception e)
                {
                    return new Result(e.getMessage(), null);
                }
            }
        }
        return new Result("Unknown Command", null);
    }
    
    private static String printArray(Complex[] array)
    {
        String result = "[";
        for (Complex x : array)
        {
            result += x.write() + ", ";
        }
        return result.substring(0, result.length()-2) + "]";
    }
    
    private void changeImage(BufferedImage bi)
    {
        graph.setIcon(new ImageIcon(bi));
    }
    public synchronized void clearInputLine()
    {
        input.setText("");
    }
    public void writeLine(String t)
    {
        output.setText(output.getText() + t + "\n");
    }

}
