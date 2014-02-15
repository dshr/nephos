import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
 
public class View implements ItemListener {
    JPanel cards; //the parent panel, uses CardLayout
    final static String MAINPANEL = "Main View";
    final static String CLOCKPANEL = "Clock View";
    static Font font;
     
    public void addComponentToPane(Container pane) {
        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        String comboBoxItems[] = { MAINPANEL, CLOCKPANEL };
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);
         
        //Create the "cards".
        JPanel card1 = new JPanel();
        card1.setLayout(new BoxLayout(card1, BoxLayout.Y_AXIS));
        JLabel mainText = new JLabel("<html><center>Well hello there!<br>It's <b>cold and windy</b><br>today at merely<br><br><span style=\"padding:20; font-size:80; font-weight:bold;\">05" + (char)186 + "C</span><br><br>And it's also <b>raining</b>,<br>so don't forget to<br><b>take an umbrella</b>!</center></html>", JLabel.CENTER);
        mainText.setFont(font);
        // mainText.setFont(new Font(Font.SANS_SERIF, 0, 30));
        mainText.setAlignmentX(Component.CENTER_ALIGNMENT);
        card1.add(mainText);

        JPanel card2 = new JPanel();
        card2.add(new JTextField("TextField", 20));
         
        //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(card1, MAINPANEL);
        cards.add(card2, CLOCKPANEL);
         
        pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
    }
     
    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
    }
     
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Nephos Weather");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(320, 480));
        // frame.setUndecorated(true);
         
        //Create and set up the content pane.
        View demo = new View();
        demo.addComponentToPane(frame.getContentPane());
         
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
     
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        Font fontBase = null;
        try {
            InputStream myStream = new BufferedInputStream(new FileInputStream("CaviarDreams.ttf"));
            fontBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
            font = fontBase.deriveFont(Font.PLAIN, 30);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("font not loaded.");
        }
         
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
