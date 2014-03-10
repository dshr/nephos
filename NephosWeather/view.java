import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.border.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.awt.geom.Ellipse2D;

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

    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
    }
}

class MyJPanel extends JPanel {

    public MyJPanel() {
        super();
        setBackground(Color.WHITE);
    }

    public void paint(Graphics g) {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
    }
}


class View {

    static MyJPanel cards; //the parent panel, uses CardLayout

    final static String MAINPANEL = "Main View";
    final static String CLOCKPANEL = "Clock View";
    final static String CALENDARPANEL = "Calendar View";
    final static String MAINSETTINGSPANEL = "Settings View";
    final static String LOCATIONSETTINGSPANEL = "Location Settings";
    final static String ALARMSETTINGSPANEL = "Alarm Settings";
    final static String SOUNDSETTINGSPANEL = "Sound Settings";

    static JFrame frame;
    static Boolean isBig = false;

    static Font fontBase;
    int temp;
    Boolean isRaining;
    static Calendar currentDate;

    static MyDrawingPanel clockViewCard;

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
        currentDate = Calendar.getInstance();
    }

    private static JButton createSimpleButton(String text, int size) { // a method to create flat buttons
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFont(fontBase.deriveFont(Font.PLAIN, size));
        return button;
    }

    private static JButton createImageButton(ImageIcon icon) { // a method to create flat buttons
        JButton button = new JButton(icon);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setBorderPainted(false);
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
                        // g.setColor(new Color(0.0f,0.0f,0.0f, 0.0f));
                        super.paintComponent(g);
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getWidth(), getHeight());
                    }
                };
        label.setForeground(new Color(0.0f,0.0f,0.0f,0.0f));
        label.setFont(fontBase.deriveFont(Font.PLAIN, size));
        label.setOpaque(false);
        
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

    private ImageIcon createImageIcon(String path, String description) {
    java.net.URL imgURL = getClass().getResource(path);
    if (imgURL != null) {
        return new ImageIcon(imgURL, description);
    } else {
        System.err.println("Couldn't find file: " + path);
        return null;
    }
}
     
    public void addComponentToPane(Container pane) {

        //calculate font sizes
        int smallText = 20;
        int mediumText = 30;
        int largeText = 50;
        int hugeText = 90;
        if(isBig)
        {
            smallText = 40;
            mediumText = 60;
            largeText = 100;
            hugeText = 180;
        }
        //load up icons
        ImageIcon cal = createImageIcon("images/calendar_32.png", "small cal");
        ImageIcon settings = createImageIcon("images/cog_32.png", "small cog");
        ImageIcon arrow = createImageIcon("images/arrow_32.png", "small arrow");
        if(isBig)
        {
            cal = createImageIcon("images/calendar_64.png", "big cal");
            settings = createImageIcon("images/cog_64.png", "big cog");
            arrow = createImageIcon("images/arrow_64.png", "big arrow");
        }
         
        //===========Create the "cards".==================

        //the main view
        MyJPanel mainViewCard = new MyJPanel();
        mainViewCard.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards, CLOCKPANEL);
                clockViewCard.mouseDragged(e);
            }
        });
        mainViewCard.setLayout(new BoxLayout(mainViewCard, BoxLayout.Y_AXIS));
        MyJPanel mainViewCardNavigation = new MyJPanel();
            mainViewCardNavigation.setLayout(new BoxLayout(mainViewCardNavigation, BoxLayout.X_AXIS));
            JButton calendarButton = createImageButton(cal);
                calendarButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CardLayout cl = (CardLayout)(cards.getLayout());
                        cl.show(cards, CALENDARPANEL);
                    }
                });
            JButton settingsButton = createImageButton(settings);
                settingsButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CardLayout cl = (CardLayout)(cards.getLayout());
                        cl.show(cards, MAINSETTINGSPANEL);
                    }
                });
        mainViewCardNavigation.add(calendarButton);
        mainViewCardNavigation.add(Box.createHorizontalGlue());
        mainViewCardNavigation.add(settingsButton);

        MyJLabel mainText1 = createLabelWithSize(
            "<html>" +
                "<center>" + 
                    "Well hello there!<br>" + 
                    "It's <b>cold and windy</b><br>" + 
                    "today at merely" + 
                "</center>"+ 
            "</html>", mediumText);
        MyJLabel mainText2 = createLabelWithSize(
            "<html>" +
                "<center>" + 
                    "<span style=\"font-weight:bold;\">05" + (char)186 + "C</span>" +
                "</center>"+ 
            "</html>", hugeText);
        MyJLabel mainText3 = createLabelWithSize(
            "<html>" +
                "<center>" + 
                    "And it's also <b>raining</b>,<br>" + 
                    "so don't forget to<br>" + 
                    "<b>take an umbrella</b>!" + 
                "</center>"+ 
            "</html>", mediumText);
        mainViewCard.add(mainViewCardNavigation);
        mainViewCard.add(Box.createRigidArea(new Dimension(0,1)));
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
        clockViewCard = new MyDrawingPanel(new Dimension(320, 420));
        if(isBig) clockViewCard = new MyDrawingPanel(new Dimension(1024, 726));
        clockViewCard.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e){
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards, MAINPANEL);
            }
        });
        clockViewCard.setLayout(new BoxLayout(clockViewCard, BoxLayout.Y_AXIS));
        MyJLabel clockText1 = createTransparentLabelWithSize(
            "<html>" +
                "<center style=\"font-color: rgba(255,0,0,0.3);\">" + 
                    "Well hello there!<br>" + 
                    "It's <b>cold and windy</b><br>" + 
                    "today at merely" + 
                "</center>"+ 
            "</html>", mediumText);
        MyJLabel clockText2 = createLabelWithSize(
            "<html>" +
                "<center>" + 
                    "<span style=\"font-weight:bold;\">05" + (char)186 + "C</span>" +
                "</center>"+ 
            "</html>", hugeText);
        MyJLabel clockText3 = createTransparentLabelWithSize(
            "<html>" +
                "<center style=\"font-color: rgba(255,0,0,0.3);\">" + 
                    "And it's also <b>raining</b>,<br>" + 
                    "so don't forget to<br>" + 
                    "<b>take an umbrella</b>!" + 
                "</center>"+ 
            "</html>", mediumText);
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
        clockViewCard.addMouseMotionListener(
                new MouseMotionAdapter(){
                    public void mouseDragged(MouseEvent e){ 
                        clockViewCard.mouseDragged(e);
                    }
        });
        

        // the calendar
        MyJPanel calendarViewCard = new MyJPanel();
        calendarViewCard.setLayout(new BoxLayout(calendarViewCard, BoxLayout.Y_AXIS));
        MyJPanel calendarViewCardNavigation = new MyJPanel();
        calendarViewCardNavigation.setLayout(new BoxLayout(calendarViewCardNavigation, BoxLayout.X_AXIS));
            JButton calendarBackButton = createImageButton(arrow);
            calendarBackButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CardLayout cl = (CardLayout)(cards.getLayout());
                        cl.show(cards, MAINPANEL);
                    }
                });
        calendarViewCardNavigation.add(calendarBackButton);
        calendarViewCardNavigation.add(Box.createHorizontalGlue());
        // calendarViewCardNavigation.setAlignmentX(Component.CENTER_ALIGNMENT);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        SimpleDateFormat dayOfTheWeek = new SimpleDateFormat("EEE");
        if(isBig)
        {
            dayOfTheWeek = new SimpleDateFormat("EEEE");
        }
        MyJLabel today = createLabelWithSize("Today   " + dateFormat.format(currentDate.getTime()) + "   05" + (char)186 + "C", mediumText);
        today.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel tomorrow = createLabelWithSize("Tomorrow   " + dateFormat.format(currentDate.getTime()) + "   05" + (char)186 + "C", mediumText);
        tomorrow.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel day3 = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   05" + (char)186 + "C", mediumText);
        day3.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel day4 = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   05" + (char)186 + "C", mediumText);
        day4.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel day5 = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   05" + (char)186 + "C", mediumText);
        day5.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel day6 = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   05" + (char)186 + "C", mediumText);
        day6.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel day7 = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   05" + (char)186 + "C", mediumText);
        day7.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendarViewCard.add(calendarViewCardNavigation);
        calendarViewCard.add(Box.createVerticalGlue());
        calendarViewCard.add(today);
        calendarViewCard.add(tomorrow);
        calendarViewCard.add(day3);
        calendarViewCard.add(day4);
        calendarViewCard.add(day5);
        calendarViewCard.add(day6);
        calendarViewCard.add(day7);
        calendarViewCard.add(Box.createVerticalGlue());

        //the main settings window
        MyJPanel mainSettingsViewCard = new MyJPanel();
        mainSettingsViewCard.setLayout(new BoxLayout(mainSettingsViewCard, BoxLayout.Y_AXIS));
        MyJPanel mainSettingsViewCardNavigation = new MyJPanel();
        mainSettingsViewCardNavigation.setLayout(new BoxLayout(mainSettingsViewCardNavigation, BoxLayout.X_AXIS));
            JButton mainSettingsBackButton = createImageButton(arrow);
            mainSettingsBackButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CardLayout cl = (CardLayout)(cards.getLayout());
                        cl.show(cards, MAINPANEL);
                    }
                });
        mainSettingsViewCardNavigation.add(mainSettingsBackButton);
        mainSettingsViewCardNavigation.add(Box.createHorizontalGlue());
        JButton locationButton = createSimpleButton("Location", largeText);
        locationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        locationButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CardLayout cl = (CardLayout)(cards.getLayout());
                        cl.show(cards, LOCATIONSETTINGSPANEL);
                    }
                });
        JButton alarmButton = createSimpleButton("Alarm", largeText);
        alarmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        alarmButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CardLayout cl = (CardLayout)(cards.getLayout());
                        cl.show(cards, ALARMSETTINGSPANEL);
                    }
                });
        JButton soundButton = createSimpleButton("Sound", largeText);
        soundButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CardLayout cl = (CardLayout)(cards.getLayout());
                        cl.show(cards, SOUNDSETTINGSPANEL);
                    }
                });
        soundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainSettingsViewCard.add(mainSettingsViewCardNavigation);
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
        MyJPanel locationSettingsViewCardNavigation = new MyJPanel();
        locationSettingsViewCardNavigation.setLayout(new BoxLayout(locationSettingsViewCardNavigation, BoxLayout.X_AXIS));
            JButton locationSettingsBackButton = createImageButton(arrow);
            locationSettingsBackButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CardLayout cl = (CardLayout)(cards.getLayout());
                        cl.show(cards, MAINSETTINGSPANEL);
                    }
                });
        locationSettingsViewCardNavigation.add(locationSettingsBackButton);
        locationSettingsViewCardNavigation.add(Box.createHorizontalGlue());
        JCheckBox automaticEnabled = createCheckBox("Automatic", largeText);

        MyJPanel locationInputPanel = new MyJPanel();
            JTextField locationInput = createTextField(10, mediumText);
        locationInputPanel.add(locationInput);

        locationSettingsViewCard.add(locationSettingsViewCardNavigation);
        locationSettingsViewCard.add(Box.createVerticalGlue());
        locationSettingsViewCard.add(automaticEnabled);
        locationSettingsViewCard.add(locationInputPanel);
        locationSettingsViewCard.add(Box.createVerticalGlue());
        

        //alarm settings
        MyJPanel alarmSettingsViewCard = new MyJPanel();
        alarmSettingsViewCard.setLayout(new BoxLayout(alarmSettingsViewCard, BoxLayout.Y_AXIS));
        MyJPanel alarmSettingsViewCardNavigation = new MyJPanel();
        alarmSettingsViewCardNavigation.setLayout(new BoxLayout(alarmSettingsViewCardNavigation, BoxLayout.X_AXIS));
            JButton alarmSettingsBackButton = createImageButton(arrow);
            alarmSettingsBackButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CardLayout cl = (CardLayout)(cards.getLayout());
                        cl.show(cards, MAINSETTINGSPANEL);
                    }
                });
        alarmSettingsViewCardNavigation.add(alarmSettingsBackButton);
        alarmSettingsViewCardNavigation.add(Box.createHorizontalGlue());

        JCheckBox alarmOn = createCheckBox("On", largeText);

        MyJPanel timeInputPanel = new MyJPanel();
            JTextField timeInput = createTextField(10, mediumText);
        timeInputPanel.add(timeInput);

        JButton selectSound = createSimpleButton("Select Sound", largeText);
        selectSound.setAlignmentX(Component.CENTER_ALIGNMENT);
        JCheckBox useColouredObject = createCheckBox("<html><center>Use Coloured Object To Turn Alarm Off</center></html>", 40);

        alarmSettingsViewCard.add(alarmSettingsViewCardNavigation);
        alarmSettingsViewCard.add(Box.createVerticalGlue());
        alarmSettingsViewCard.add(alarmOn);
        alarmSettingsViewCard.add(Box.createVerticalGlue());
        alarmSettingsViewCard.add(timeInputPanel);
        alarmSettingsViewCard.add(Box.createVerticalGlue());
        alarmSettingsViewCard.add(selectSound);
        alarmSettingsViewCard.add(Box.createVerticalGlue());
        alarmSettingsViewCard.add(useColouredObject);
        alarmSettingsViewCard.add(Box.createVerticalGlue());


        //sound settings
        MyJPanel soundSettingsViewCard = new MyJPanel();
        soundSettingsViewCard.setLayout(new BoxLayout(soundSettingsViewCard, BoxLayout.Y_AXIS));
        MyJPanel soundSettingsViewCardNavigation = new MyJPanel();
        soundSettingsViewCardNavigation.setLayout(new BoxLayout(soundSettingsViewCardNavigation, BoxLayout.X_AXIS));
            JButton soundSettingsBackButton = createImageButton(arrow);
            soundSettingsBackButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        CardLayout cl = (CardLayout)(cards.getLayout());
                        cl.show(cards, MAINSETTINGSPANEL);
                    }
                });
        soundSettingsViewCardNavigation.add(soundSettingsBackButton);
        soundSettingsViewCardNavigation.add(Box.createHorizontalGlue());
        JCheckBox soundOn = createCheckBox("On", largeText);
        soundOn.setAlignmentX(Component.CENTER_ALIGNMENT);

        MyJPanel soundLevelPanel = new MyJPanel();
        // soundLevelPanel.setLayout(new BoxLayout(soundLevelPanel, BoxLayout.Y_AXIS));
            JSlider soundLevel = new JSlider(JSlider.HORIZONTAL, 0, 100, 75);
            MyJLabel soundLevelLabel = createLabelWithSize("Volume", smallText);
        soundLevelPanel.add(soundLevelLabel);
        soundLevelPanel.add(soundLevel);

        soundSettingsViewCard.add(soundSettingsViewCardNavigation);
        soundSettingsViewCard.add(soundOn);
        soundSettingsViewCard.add(soundLevelPanel);
        // soundSettingsViewCard.add(soundLevelLabel);
        soundSettingsViewCard.add(Box.createVerticalGlue());
         
        //Create the panel that contains the "cards".
        cards = new MyJPanel();
        cards.setLayout(new CardLayout());
        cards.add(mainViewCard, MAINPANEL);
        cards.add(clockViewCard, CLOCKPANEL);
        cards.add(calendarViewCard, CALENDARPANEL);
        cards.add(mainSettingsViewCard, MAINSETTINGSPANEL);
        cards.add(locationSettingsViewCard, LOCATIONSETTINGSPANEL);
        cards.add(alarmSettingsViewCard, ALARMSETTINGSPANEL);
        cards.add(soundSettingsViewCard, SOUNDSETTINGSPANEL);
         
        // pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
    }
     
    // public void itemStateChanged(ItemEvent evt) {
    //     CardLayout cl = (CardLayout)(cards.getLayout());
    //     cl.show(cards, (String)evt.getItem());
    // }
     
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Nephos Weather");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if(isBig)
        {
            frame.setPreferredSize(new Dimension(1024, 786));
        }
        else
        {
            frame.setPreferredSize(new Dimension(320, 480));
        }
        frame.setResizable(false);
        // frame.setUndecorated(true); //<- removes the top bar thing
         
        //Create and set up the content pane.
        // View demo = new View();
        // demo.
        addComponentToPane(frame.getContentPane());
         
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void changeSize()
    {
        frame.setVisible(false);
        frame.dispose();
        if(isBig)
        {
            isBig = false;
            createAndShowGUI();
        }
        else
        {
            isBig = true;
            createAndShowGUI();
        }
    }
}
