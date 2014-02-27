import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.border.*;

//extending JLabel to provide antialiasing
class MyJLabel extends JLabel{

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
    final static String MAINSETTINGSPANEL = "Settings View";
    final static String LOCATIONSETTINGSPANEL = "Location Settings";
    final static String ALARMSETTINGSPANEL = "Alarm Settings";
    final static String SOUNDSETTINGSPANEL = "Sound Settings";

    static JFrame frame;
    static Boolean isBig;

    static Font font;
    int temp;
    Boolean isRaining;

    private static JButton createSimpleButton(String text) { // a method to create flat buttons
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFont(new Font("Helvetica Neue", 0, 50));
        return button;
    }

    private static MyJLabel createLabelWithSize(String text, int size){ // a method to create our labels
        MyJLabel label = new MyJLabel(text, JLabel.CENTER);
        label.setFont(new Font("Helvetica Neue", 0, size));
        label.setMaximumSize(label.getPreferredSize());
        return label;
    }

    private static MyJLabel createTransparentLabelWithSize(String text, int size){ // a method to create invisible labels
        MyJLabel label = new MyJLabel(text, JLabel.CENTER){
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(getBackground());
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
        label.setFont(new Font("Helvetica Neue", 0, size));
        return label;
    }

    private static JCheckBox createCheckBox(String text){ //a method to create our checkboxes
        JCheckBox cb = new JCheckBox(text);
        cb.setForeground(Color.BLACK);
        cb.setBackground(Color.WHITE);
        cb.setBorderPainted(false);
        cb.setFont(new Font("Helvetica Neue", 0, 50));
        return cb;
    }
     
    public void addComponentToPane(Container pane) {

        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        String comboBoxItems[] = { MAINPANEL, CLOCKPANEL, MAINSETTINGSPANEL, LOCATIONSETTINGSPANEL, ALARMSETTINGSPANEL, SOUNDSETTINGSPANEL };
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);
         
        //===========Create the "cards".==================

        //the main view
        JPanel mainViewCard = new JPanel();
        mainViewCard.setLayout(new BoxLayout(mainViewCard, BoxLayout.Y_AXIS));
        MyJLabel mainText1 = createLabelWithSize(
            "<html>" +
                "<center>" + 
                    "Well hello there!<br>" + 
                    "It's <b>cold and windy</b><br>" + 
                    "today at merely" + 
                "</center>"+ 
            "</html>", 30);
        MyJLabel mainText2 = createLabelWithSize(
            "<html>" +
                "<center>" + 
                    "<span style=\"font-weight:bold;\">05" + (char)186 + "C</span>" +
                "</center>"+ 
            "</html>", 90);
        MyJLabel mainText3 = createLabelWithSize(
            "<html>" +
                "<center>" + 
                    "And it's also <b>raining</b>,<br>" + 
                    "so don't forget to<br>" + 
                    "<b>take an umbrella</b>!" + 
                "</center>"+ 
            "</html>", 30);
        mainViewCard.add(Box.createVerticalGlue());
        mainText1.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainViewCard.add(mainText1);
        mainViewCard.add(Box.createVerticalGlue());
        mainText2.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainViewCard.add(mainText2);
        mainViewCard.add(Box.createVerticalGlue());
        mainText3.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainViewCard.add(mainText3);
        mainViewCard.add(Box.createVerticalGlue());


        //the clock
        JPanel clockViewCard = new JPanel();
        clockViewCard.setLayout(new BoxLayout(clockViewCard, BoxLayout.Y_AXIS));
        MyJLabel clockText1 = createTransparentLabelWithSize(
            "<html>" +
                "<center>" + 
                    "Well hello there!<br>" + 
                    "It's <b>cold and windy</b><br>" + 
                    "today at merely" + 
                "</center>"+ 
            "</html>", 30);
        MyJLabel clockText2 = createLabelWithSize(
            "<html>" +
                "<center>" + 
                    "<span style=\"font-weight:bold;\">05" + (char)186 + "C</span>" +
                "</center>"+ 
            "</html>", 90);
        MyJLabel clockText3 = createTransparentLabelWithSize(
            "<html>" +
                "<center>" + 
                    "And it's also <b>raining</b>,<br>" + 
                    "so don't forget to<br>" + 
                    "<b>take an umbrella</b>!" + 
                "</center>"+ 
            "</html>", 30);
        clockViewCard.add(Box.createVerticalGlue());
        clockText1.setAlignmentX(Component.CENTER_ALIGNMENT);
        clockViewCard.add(clockText1);
        clockViewCard.add(Box.createVerticalGlue());
        clockText2.setAlignmentX(Component.CENTER_ALIGNMENT);
        clockViewCard.add(clockText2);
        clockViewCard.add(Box.createVerticalGlue());
        clockText3.setAlignmentX(Component.CENTER_ALIGNMENT);
        clockViewCard.add(clockText3);
        clockViewCard.add(Box.createVerticalGlue());


        //the main settings window
        JPanel mainSettingsViewCard = new JPanel();
        mainSettingsViewCard.setLayout(new BoxLayout(mainSettingsViewCard, BoxLayout.Y_AXIS));
        JButton locationButton = createSimpleButton("Location");
        locationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton alarmButton = createSimpleButton("Alarm");
        alarmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton soundButton = createSimpleButton("Sound");
        soundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainSettingsViewCard.add(Box.createVerticalGlue());
        mainSettingsViewCard.add(locationButton, BorderLayout.CENTER);
        mainSettingsViewCard.add(Box.createVerticalGlue());
        mainSettingsViewCard.add(alarmButton, BorderLayout.CENTER);
        mainSettingsViewCard.add(Box.createVerticalGlue());
        mainSettingsViewCard.add(soundButton, BorderLayout.CENTER);
        mainSettingsViewCard.add(Box.createVerticalGlue());


        //the location settings window
        JPanel locationSettingsViewCard = new JPanel();
        JPanel locationSettingsContents = new JPanel();
        locationSettingsContents.setLayout(new BoxLayout(locationSettingsContents, BoxLayout.Y_AXIS));
        JCheckBox automaticEnabled = createCheckBox("Automatic");
        automaticEnabled.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationSettingsContents.add(automaticEnabled);
        locationSettingsViewCard.add(locationSettingsContents, BorderLayout.CENTER);


        JPanel alarmSettingsViewCard = new JPanel();


        JPanel soundettingsViewCard = new JPanel();



         
        //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(mainViewCard, MAINPANEL);
        cards.add(clockViewCard, CLOCKPANEL);
        cards.add(mainSettingsViewCard, MAINSETTINGSPANEL);
        cards.add(locationSettingsViewCard, LOCATIONSETTINGSPANEL);
        cards.add(alarmSettingsViewCard, ALARMSETTINGSPANEL);
        cards.add(soundettingsViewCard, SOUNDSETTINGSPANEL);
         
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
        frame = new JFrame("Nephos Weather");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(320, 480));
        frame.setResizable(false);
        isBig = false;
        // frame.setUndecorated(true); //<- removes the top bar thing
         
        //Create and set up the content pane.
        View demo = new View();
        demo.addComponentToPane(frame.getContentPane());
         
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void changeSize()
    {
        if(isBig)
        {
            frame.setPreferredSize(new Dimension(320, 480));
            isBig = false;
        }
        else
        {
            frame.setPreferredSize(new Dimension(1024, 786));
            isBig = true;
        }
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
