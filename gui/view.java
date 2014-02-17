import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

//extending JLabel to provide antialiasing
class MyJLabel extends JLabel {

    public MyJLabel() {
        super();
    }

    public MyJLabel(String label) {
        super(label);
    }

    public MyJLabel(String label, int alignment) {
        super(label, alignment);
    }

    public MyJLabel(String label, ImageIcon icon, int alignment) {
        super(label, icon, alignment);
    }

    // that's the essential part:

    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        super.paint(g);
    }
}
 
public class View implements ItemListener {
    JPanel cards; //the parent panel, uses CardLayout
    final static String MAINPANEL = "Main View";
    final static String CLOCKPANEL = "Clock View";
    static Font font;
    int temp;
    Boolean isRaining;

     
    public void addComponentToPane(Container pane) {

        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        String comboBoxItems[] = { MAINPANEL, CLOCKPANEL };
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);
         
        //Create the "cards".

        //the main view
        JPanel mainViewCard = new JPanel();
        mainViewCard.setLayout(new BoxLayout(mainViewCard, BoxLayout.Y_AXIS));
        MyJLabel mainText = new MyJLabel("<html><center style=\"font-size:30;\">Well hello there!<br>It's <b>cold and windy</b><br>today at merely<br><br><span style=\"padding:20; font-size:80; font-weight:bold;\">05" + (char)186 + "C</span><br><br>And it's also <b>raining</b>,<br>so don't forget to<br><b>take an umbrella</b>!</center></html>", JLabel.CENTER);
        mainText.setFont(new Font("Helvetica Neue", 0, 30));
        mainText.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainViewCard.add(mainText);

        //the clock
        JPanel clockViewCard = new JPanel();
        clockViewCard.setLayout(new BorderLayout());
        // clockViewCard.add(new JTextField("TextField", 20), BorderLayout.PAGE_START);
        MyJLabel clockText = new MyJLabel("<html><center><span style=\"padding:20; font-size:80; font-weight:bold;\">05" + (char)186 + "C</span></center></html>",  JLabel.CENTER);
        clockText.setFont(new Font("Helvetica Neue", 0, 80));
        clockViewCard.add(clockText, BorderLayout.CENTER);

         
        //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(mainViewCard, MAINPANEL);
        cards.add(clockViewCard, CLOCKPANEL);
         
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

        Font fontBase = null;
        try {
            InputStream myStream = new BufferedInputStream(new FileInputStream("DISCO.ttf"));
            fontBase = Font.createFont(Font.TRUETYPE_FONT, new File("DISCO.ttf"));
            font = fontBase.deriveFont(30);
            System.out.println("font loaded");
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
