import javax.swing.*;
// for layout managers
import java.awt.*;
// for awt events
import java.awt.event.*;
import javax.swing.event.*;
// for shapes
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
//for images
import java.awt.image.BufferedImage; 
import java.io.*;
import javax.imageio.ImageIO;
//for blurred stuff
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import com.googlecode.javacv.CanvasFrame;
//for current time
import java.util.Calendar;

// don't forget to implement ActionListener for buttons, ChangeListener for slider
public class Mockup extends JPanel{

    private BufferedImage myPicture;
    private JPanel drawPanel;

	public Mockup() {
		// setup layout manager
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		//Initialise window content

        // create and add my drawing panel
        drawPanel = new MyDrawingPanel();
        add(drawPanel);
    }

    public static void main(String[] args) {

        //Create and set up the window.
        JFrame frame = new JFrame("WeatherApp");

		// Create our version of the Panel
		Mockup ourPanel = new Mockup();

		// add to frame
        frame.getContentPane().add(ourPanel);

		// good idea to add window event listeners
		
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
            public void windowDeiconified(WindowEvent e) { ; }
            public void windowIconified(WindowEvent e) { ; }
        });

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}

class MyDrawingPanel extends JPanel implements MouseMotionListener {
// customised drawing panel

	// variables for the drawing of circle and box - location and size
	int circleX = 150;
	int circleY = 250;
	int circleSize = 250;

    Graphics2D g2;
    Ellipse2D circle;
    Ellipse2D clock;
    BufferedImage bgImage;

    int clockSize = 50;
    int temp = -10;
    int hours = 0;
    int minutes = 0;

	public MyDrawingPanel() {
		// set preferred size of drawing area
		setPreferredSize(new Dimension(300, 500));
		// add mouse event listeners
        addMouseMotionListener(this);

        Calendar currentDate = Calendar.getInstance();
        hours = currentDate.get(Calendar.HOUR_OF_DAY);
        minutes = currentDate.get(Calendar.MINUTE);
	}
	
	public void setCircleSize(int newSize)
	{ // access function
		circleSize = newSize;
		repaint();
	}

    public void paint(Graphics g) {
    	// get the Graphics2D drawing context (for nice drawing)
        g2 = (Graphics2D) g;
        // make the drawing anti-aliased (i.e. smooth lines)
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // now draw our objects

        //put the background
        try
        {
            BufferedImage bgImage = ImageIO.read(new File("bg.jpg"));
            g.drawImage(bgImage, 0, 0, null);
        } 
        catch (IOException ex) 
        {
            // handle exception...
            System.out.println("can't open image");
        }

        try
        {
            bgImage = ImageIO.read(new File("bg.jpg"));
            IplImage blurredImage = IplImage.createFrom(bgImage);
            cvSmooth(blurredImage, blurredImage, CV_GAUSSIAN, 7);
            BufferedImage blurredBG = blurredImage.getBufferedImage();
            circle = new Ellipse2D.Double(circleX-(circleSize/2), circleY-(circleSize/2), circleSize, circleSize);
            g2.setClip(circle);
            g2.drawImage(blurredBG, 0, 0, null);
            g2.setClip(null);
            g.setColor(new Color(0.2f,0.2f,0.4f, 0.2f));
            // fill in the circle
            g2.fill(circle);
            g2.setColor(new Color(1.0f,1.0f,1.0f, 1.0f));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(circle);
        } 
        catch (IOException ex) 
        {
            // handle exception...
            System.out.println("can't open image");
        }

        Font font = new Font("Helvetica", Font.PLAIN, 56);
        g.setFont(font);
        g.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        g2.drawString(Integer.toString(temp) + (char)186 + "C", 65, 275);

        font = new Font("Helvetica", Font.PLAIN, 24);
        g.setFont(font);
        g.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        g2.drawString(String.format("%02d", hours) + ":" + String.format("%02d", minutes), 120, 220);

        double angleInDegrees = -(hours*60 + minutes)/4;
        double angle = Math.toRadians(angleInDegrees);

        int clockX = (int)(circleX-((circleSize/2)*Math.sin(angle))) - clockSize/2;
        int clockY = (int)(circleY-((circleSize/2)*Math.cos(angle))) - clockSize/2;

        try
        {
            bgImage = ImageIO.read(new File("bg.jpg"));
            IplImage blurredImage = IplImage.createFrom(bgImage);
            cvSmooth(blurredImage, blurredImage, CV_GAUSSIAN, 7);
            BufferedImage blurredBG = blurredImage.getBufferedImage();
            clock = new Ellipse2D.Double(clockX, clockY, clockSize, clockSize);
            g2.setClip(clock);
            g2.drawImage(blurredBG, 0, 0, null);
            g2.setClip(null);
            g.setColor(new Color(0.2f,0.2f,0.3f, 0.6f));
            // fill in the circle
            g2.fill(clock);
            g2.setColor(new Color(1.0f,1.0f,1.0f, 1.0f));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(clock);
        } 
        catch (IOException ex) 
        {
            // handle exception...
            System.out.println("can't open image");
        }
	}

    public void mouseMoved(MouseEvent e) {
        
    }

    public void mouseDragged(MouseEvent e) {
        minutes++;
        if(minutes == 60)
        {
            hours++;
            minutes = 0;
        }
        if(hours == 24)
        {
            hours = 0;
        }
        repaint();
    }
	
}

