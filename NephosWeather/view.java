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

class MyJPanel extends JPanel {

    public MyJPanel() {
        super();
        setBackground(Color.WHITE);
    }

    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );
        super.paint(g);
    }
}

class View implements ItemListener {

    MyJPanel cards; //the parent panel, uses CardLayout

    final static String MAINPANEL = "Main View";
    final static String CLOCKPANEL = "Clock View";
    final static String MAINSETTINGSPANEL = "Settings View";
    final static String LOCATIONSETTINGSPANEL = "Location Settings";
    final static String ALARMSETTINGSPANEL = "Alarm Settings";
    final static String SOUNDSETTINGSPANEL = "Sound Settings";

    static JFrame frame;
    static Boolean isBig;

    static Font fontBase;
    int temp;
    Boolean isRaining;

    public View() {
        try {
            // InputStream myStream = new BufferedInputStream(new FileInputStream("DISCO.ttf"));
            fontBase = Font.createFont(Font.TRUETYPE_FONT, new File("Lato-Lig.ttf"));
            // font = fontBase.deriveFont(Font.PLAIN, 30);
            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            genv.registerFont(fontBase);
            System.out.println("font loaded");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("font not loaded.");
        }
    }

    private static JButton createSimpleButton(String text, int size) { // a method to create flat buttons
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFont(fontBase.deriveFont(Font.PLAIN, size));
        return button;
    }

    private static MyJLabel createLabelWithSize(String text, int size){ // a method to create our labels
        MyJLabel label = new MyJLabel(text, JLabel.CENTER);
        label.setFont(fontBase.deriveFont(Font.PLAIN, size));
        label.setMaximumSize(label.getPreferredSize());
        return label;
    }

    private static MyJLabel createTransparentLabelWithSize(String text, int size){ // a method to create invisible labels
        MyJLabel label = new MyJLabel(text, JLabel.CENTER){
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
        label.setFont(fontBase.deriveFont(Font.PLAIN, size));
        return label;
    }

    private static JCheckBox createCheckBox(String text, int size){ //a method to create our checkboxes
        JCheckBox cb = new JCheckBox(text);
        cb.setForeground(Color.BLACK);
        cb.setBackground(Color.WHITE);
        cb.setBorderPainted(false);
        cb.setFont(fontBase.deriveFont(Font.PLAIN, size));
        cb.setAlignmentX(Component.CENTER_ALIGNMENT);
        return cb;
    }

    private static JTextField createTextField(int cols, int size){
        JTextField tf = new JTextField(cols);
        tf.setFont(fontBase.deriveFont(Font.PLAIN, size));
        tf.setAlignmentX(Component.CENTER_ALIGNMENT);
        tf.setHorizontalAlignment(JTextField.CENTER);
        return tf;
    }
     
    public void addComponentToPane(Container pane) {

        //Put the JComboBox in a MyJPanel to get a nicer look.
        MyJPanel comboBoxPane = new MyJPanel(); //use FlowLayout
        String comboBoxItems[] = { MAINPANEL, CLOCKPANEL, MAINSETTINGSPANEL, LOCATIONSETTINGSPANEL, ALARMSETTINGSPANEL, SOUNDSETTINGSPANEL };
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);
         
        //===========Create the "cards".==================

        //the main view
        MyJPanel mainViewCard = new MyJPanel();
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
        MyJPanel clockViewCard = new MyJPanel();
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
        MyJPanel mainSettingsViewCard = new MyJPanel();
        mainSettingsViewCard.setLayout(new BoxLayout(mainSettingsViewCard, BoxLayout.Y_AXIS));
        JButton locationButton = createSimpleButton("Location", 50);
        locationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton alarmButton = createSimpleButton("Alarm", 50);
        alarmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton soundButton = createSimpleButton("Sound", 50);
        soundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainSettingsViewCard.add(Box.createVerticalGlue());
        mainSettingsViewCard.add(locationButton);
        mainSettingsViewCard.add(Box.createVerticalGlue());
        mainSettingsViewCard.add(alarmButton);
        mainSettingsViewCard.add(Box.createVerticalGlue());
        mainSettingsViewCard.add(soundButton);
        mainSettingsViewCard.add(Box.createVerticalGlue());


        //the location settings window
        MyJPanel locationSettingsViewCard = new MyJPanel();
        locationSettingsViewCard.setLayout(new BoxLayout(locationSettingsViewCard, BoxLayout.Y_AXIS));

        JCheckBox automaticEnabled = createCheckBox("Automatic", 50);

        MyJPanel locationInputPanel = new MyJPanel();
            JTextField locationInput = createTextField(10, 30);
        locationInputPanel.add(locationInput);

        locationSettingsViewCard.add(Box.createVerticalGlue());
        locationSettingsViewCard.add(automaticEnabled);
        locationSettingsViewCard.add(locationInputPanel);
        locationSettingsViewCard.add(Box.createVerticalGlue());
        


        MyJPanel alarmSettingsViewCard = new MyJPanel();
        alarmSettingsViewCard.setLayout(new BoxLayout(alarmSettingsViewCard, BoxLayout.Y_AXIS));

        JCheckBox alarmOn = createCheckBox("On", 50);

        MyJPanel timeInputPanel = new MyJPanel();
            JTextField timeInput = createTextField(10, 30);
        timeInputPanel.add(timeInput);

        JButton selectSound = createSimpleButton("Select Sound", 50);
        selectSound.setAlignmentX(Component.CENTER_ALIGNMENT);
        JCheckBox useColouredObject = createCheckBox("<html><center>Use Coloured Object To Turn Alarm Off</center></html>", 40);

        alarmSettingsViewCard.add(Box.createVerticalGlue());
        alarmSettingsViewCard.add(alarmOn);
        alarmSettingsViewCard.add(Box.createVerticalGlue());
        alarmSettingsViewCard.add(timeInputPanel);
        alarmSettingsViewCard.add(Box.createVerticalGlue());
        alarmSettingsViewCard.add(selectSound);
        alarmSettingsViewCard.add(Box.createVerticalGlue());
        alarmSettingsViewCard.add(useColouredObject);
        alarmSettingsViewCard.add(Box.createVerticalGlue());

        MyJPanel soundSettingsViewCard = new MyJPanel();
        soundSettingsViewCard.setLayout(new BoxLayout(soundSettingsViewCard, BoxLayout.Y_AXIS));

        JCheckBox soundOn = createCheckBox("On", 50);
        soundOn.setAlignmentX(Component.CENTER_ALIGNMENT);

        MyJPanel soundLevelPanel = new MyJPanel();
        // soundLevelPanel.setLayout(new BoxLayout(soundLevelPanel, BoxLayout.Y_AXIS));
            JSlider soundLevel = new JSlider(JSlider.HORIZONTAL, 0, 100, 75);
            MyJLabel soundLevelLabel = createLabelWithSize("Volume", 20);
        soundLevelPanel.add(soundLevelLabel);
        soundLevelPanel.add(soundLevel);

        soundSettingsViewCard.add(soundOn);
        soundSettingsViewCard.add(soundLevelPanel);
        // soundSettingsViewCard.add(soundLevelLabel);
        soundSettingsViewCard.add(Box.createVerticalGlue());
         
        //Create the panel that contains the "cards".
        cards = new MyJPanel();
        cards.setLayout(new CardLayout());
        cards.add(mainViewCard, MAINPANEL);
        cards.add(clockViewCard, CLOCKPANEL);
        cards.add(mainSettingsViewCard, MAINSETTINGSPANEL);
        cards.add(locationSettingsViewCard, LOCATIONSETTINGSPANEL);
        cards.add(alarmSettingsViewCard, ALARMSETTINGSPANEL);
        cards.add(soundSettingsViewCard, SOUNDSETTINGSPANEL);
         
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
    public static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Nephos Weather");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(320, 480));
        // frame.setResizable(false);
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
            frame.setVisible(false);
            frame.setPreferredSize(new Dimension(320, 480));
            isBig = false;
            frame.pack();
            frame.setVisible(true);
        }
        else
        {
            frame.setVisible(false);
            frame.setPreferredSize(new Dimension(1024, 786));
            isBig = true;
            frame.pack();
            frame.setVisible(true);
        }
    }
}
