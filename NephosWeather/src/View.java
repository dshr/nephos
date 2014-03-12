import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.border.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.awt.geom.Ellipse2D;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import static com.googlecode.javacv.cpp.opencv_core.cvFlip;

import com.googlecode.javacpp.Loader;
import static com.googlecode.javacpp.Loader.*;

import java.awt.Dimension;

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

public class View {

    private class MyDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                System.out.println(e.getKeyCode());
                if(e.getKeyCode() == 32)
                {
                    System.out.println("bop!");
                    System.out.println(isBig);
                    changeSize();
                }

				else if((e.getKeyCode() == 39));
				{	
					rightPressed = true;
					checkCard();
				}
                return true;
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                return true;
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                return true;
            }
            return false;
        }
    }

    static MyJPanel cards; //the parent panel, uses CardLayout
    static MyDrawingPanel clockViewCard;
    
	static int hueLowerR = 140;
	static int hueUpperR = 200;
	static Robot bot;


    final static String MAINPANEL = "Main View";
    final static String CLOCKPANEL = "Clock View";
    final static String CALENDARPANEL = "Calendar View";
    final static String MAINSETTINGSPANEL = "Settings View";
    final static String LOCATIONSETTINGSPANEL = "Location Settings";
    final static String ALARMSETTINGSPANEL = "Alarm Settings";
    final static String SOUNDSETTINGSPANEL = "Sound Settings";

    static JFrame frame;
    static Boolean isBig;
	static Boolean rightPressed = false;
	static Boolean isClock = false;

    static Font fontBase;
    int temp;
    Boolean isRaining;
    static Calendar currentDate;

    static NephosAPI weather;
	
	static int count = 1;

    public View(NephosAPI w) {
        try {
            // InputStream myStream = new BufferedInputStream(new FileInputStream("DISCO.ttf"));
            fontBase = Font.createFont(Font.TRUETYPE_FONT, new File("resources/Lato-Lig.ttf"));
            // font = fontBase.deriveFont(Font.PLAIN, 30);
            GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            genv.registerFont(fontBase);
            // System.out.println("font loaded");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("font not loaded.");
        }
        currentDate = Calendar.getInstance();
        weather = w;
        isBig = false;
        // System.out.println(isBig);
    }

    private static String getTempAtDay(int day)
    {
        if(isBig)
        {
            return ": " + weather.getMinTemperatureAtDay(day) + " to " + weather.getMaxTemperatureAtDay(day);
        }
        else
        {
            return "" + weather.getTemperatureAtDay(day);
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
        ImageIcon cal = createImageIcon("resources/images/calendar_32.png", "small cal");
        ImageIcon settings = createImageIcon("resources/images/cog_32.png", "small cog");
        ImageIcon arrow = createImageIcon("resources/images/arrow_32.png", "small arrow");
        ImageIcon audioOn = createImageIcon("resources/images/audio_on_32.png", "small audio on");
        ImageIcon audioOff = createImageIcon("resources/images/audio_off_32.png", "small audio off");
        if(isBig)
        {
            cal = createImageIcon("resources/images/calendar_64.png", "big cal");
            settings = createImageIcon("resources/images/cog_64.png", "big cog");
            arrow = createImageIcon("resources/images/arrow_64.png", "big arrow");
            audioOn = createImageIcon("resources/images/audio_on_64.png", "big audio on");
            audioOff = createImageIcon("resources/images/audio_off_64.png", "big audio off");
        }
         
        //===========Create the "cards".==================

        //the main view
        MyJPanel mainViewCard = new MyJPanel();
        mainViewCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                CardLayout cl = (CardLayout)(cards.getLayout());
                clockViewCard.displayedTime = clockViewCard.currentTime;
                clockViewCard.last = null;
                cl.show(cards, CLOCKPANEL);
                clockViewCard.mouseDragged(e);
				isClock = true;
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
                    "<span style=\"font-weight:bold;\">" + weather.getCurrentTemperature() + (char)186 + "C</span>" +
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
        //first we create the labels so we can send them to them drawing panel
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
                    "<span style=\"font-weight:bold;\">" + weather.getCurrentTemperature() + (char)186 + "C</span>" +
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
        clockViewCard = new MyDrawingPanel(isBig, fontBase, mediumText, clockText2, weather);
        clockViewCard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                CardLayout cl = (CardLayout)(cards.getLayout());
                cl.show(cards, MAINPANEL);
				isClock = false;
            }
        });
        clockViewCard.setLayout(new BoxLayout(clockViewCard, BoxLayout.Y_AXIS));
        MyJPanel clockViewCardNavigation = new MyJPanel();
        clockViewCardNavigation.setLayout(new BoxLayout(clockViewCardNavigation, BoxLayout.X_AXIS));
            JButton clockBackButton = createImageButton(audioOn);
            clockBackButton.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {

                    }
                });
        clockViewCardNavigation.add(Box.createHorizontalGlue());
        clockViewCardNavigation.add(clockBackButton);
        clockViewCard.add(clockViewCardNavigation);
        // clockViewCard.add(Box.createVerticalGlue());
        // timeText.setAlignmentX(Component.CENTER_ALIGNMENT);
        // clockViewCard.add(timeText);
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
        MyJLabel today = createLabelWithSize("Today   " + dateFormat.format(currentDate.getTime()) + "   " + getTempAtDay(0) + (char)186 + "C", mediumText);
        today.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel tomorrow = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   " + getTempAtDay(1) + (char)186 + "C", mediumText);
        tomorrow.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel day3 = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   " + getTempAtDay(2) + (char)186 + "C", mediumText);
        day3.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel day4 = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   " + getTempAtDay(3) + (char)186 + "C", mediumText);
        day4.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel day5 = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   " + getTempAtDay(4) + (char)186 + "C", mediumText);
        day5.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel day6 = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   " + getTempAtDay(5) + (char)186 + "C", mediumText);
        day6.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDate.add(Calendar.DATE, 1);
        MyJLabel day7 = createLabelWithSize(dayOfTheWeek.format(currentDate.getTime()) + "   " + dateFormat.format(currentDate.getTime()) + "   " + getTempAtDay(6) + (char)186 + "C", mediumText);
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
     
    public void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Nephos Weather");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println(isBig);
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
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispatcher());

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
	
	public void checkCard() {
		if(isClock) 
		{
			try {
				move();
			}
		
			catch (Exception ex)
			{
				System.out.println("Error");
			}
		}
	}
	public void move() throws Exception {
			
			rightPressed = false;
			View.count++;
			int timer = 100;
			int result = 0;
			int divider = 0;

			System.out.println("Dragged method running");
			OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        	grabber.start();
			//Create canvas frame for displaying webcam.
			//CanvasFrame canvasW = new CanvasFrame("Webcam");
       
			//Set Canvas frame to close on exit

			IplImage orgImg = grabber.grab();
		  	//Set canvas size as per dimentions of video frame.
		 	//canvasW.setCanvasSize(grabber.getImageWidth(), grabber.getImageHeight());
		  	// threshold the captured image using the lower and upper thresholds
		  	// defined above
                //  see end of the code for explanation on contours                          		        
        
			outerloop:
			while ((orgImg = grabber.grab()) != null) {
				timer --;
				if(timer == 0) {
					break outerloop;
				}
			  // threshold the captured image using the lower and upper thresholds
			  	IplImage thresholdImage = hsvThreshold(orgImg);
			  	CvMemStorage mem = CvMemStorage.create();
			  	CvSeq contours = new CvSeq(orgImg);

			    contours = cvCreateSeq(0, sizeof(CvContour.class),
				sizeof(CvSeq.class), mem);

				cvFindContours(thresholdImage.clone(), mem, contours,
				Loader.sizeof(CvContour.class), CV_RETR_CCOMP,
				CV_CHAIN_APPROX_SIMPLE, cvPoint(0, 0));
		        
		        while (contours != null && !contours.isNull()) {
					divider++;
					System.out.println(divider);
		        	// define a 2D rectangle structure that will be the bounding box of the contour of the object
					CvRect br = cvBoundingRect(contours, 1);
					// draw a GREEN rectangle around the contour (of the object detected)
					cvRectangle(orgImg, cvPoint(br.x(), br.y()),
					cvPoint(br.x() + br.width(), br.y() + br.height()),
					CvScalar.GREEN, 3, CV_AA, 0);
				
					if(divider%2==0) 
					{
						result = result - br.x();
		        		if(result > 0)
		        		{
		            		clockViewCard.displayedTime.add(Calendar.HOUR_OF_DAY, 3);
							System.out.println("add 3");
							break outerloop;
		        		}
	
				        else if(result < 0)
		        		{
		            		clockViewCard.displayedTime.add(Calendar.HOUR_OF_DAY, -3);
							System.out.println("minus 3");
							result = 0;
							break outerloop;
		        		}
						//else if(result ==0) System.out.println("Nothing");
					}
					else result = br.x();
					cvWaitKey(2);
		            // do all of the above for the next contour
					contours = contours.h_next();
				}  		
				 // show the detected 'coloured' objects with rectangles drawn on on the initially captured image        
				 //canvasW.showImage(orgImg);  			
			  }
		grabber.stop();
		//canvasW.dispose();
     	clockViewCard.repaint();	      
	}


	//Taken from OpenCV sameple code
	static IplImage hsvThreshold(IplImage orgImg) {
		// 8-bit, 3- color =(RGB)
		IplImage imgHSV = cvCreateImage(cvGetSize(orgImg), 8, 3);

		cvCvtColor(orgImg, imgHSV, CV_BGR2HSV);
		// 8-bit 1- color = monochrome
		IplImage imgThreshold = cvCreateImage(cvGetSize(orgImg), 8, 1);
		// cvScalar : ( H , S , V, A)
		cvInRangeS(imgHSV, cvScalar(hueLowerR, 100, 100, 0),
				cvScalar(hueUpperR, 255, 255, 0), imgThreshold);
		cvReleaseImage(imgHSV);
		// filter the thresholded image to smooth things
		cvSmooth(imgThreshold, imgThreshold, CV_MEDIAN, 13);
		// return the processed image
		return imgThreshold;
	}

	//Taken from OpenCV sameple code
	static Dimension getCoordinates(IplImage thresholdImage) {
		int posX = 0;
		int posY = 0;
		CvMoments moments = new CvMoments();
		cvMoments(thresholdImage, moments, 1);
		// cv Spatial moment : Mji=sumx,y(I(x,y)â€¢xjâ€¢yi)
		// where I(x,y) is the intensity of the pixel (x, y).
		double momX10 = cvGetSpatialMoment(moments, 1, 0); // (x,y)
		double momY01 = cvGetSpatialMoment(moments, 0, 1);// (x,y)
		double area = cvGetCentralMoment(moments, 0, 0);
		posX = (int) (momX10 / area);
		posY = (int) (momY01 / area);
		return new Dimension(posX, posY);
	}	
}

