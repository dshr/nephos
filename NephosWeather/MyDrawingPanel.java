import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.border.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.awt.geom.Ellipse2D;

class MyDrawingPanel extends JPanel {

    static Dimension panelSize;
    static int circleSize;
    static int hours;
    static int minutes;

    public MyDrawingPanel(Dimension size) {
        super();
        panelSize = size;
        System.out.println("" + size.width + " " + size.height);
        if(size.width > size.height)
        {
            circleSize = (int)(0.8 * size.height);
        }
        else
        {
            circleSize = (int)(0.8 * size.width);
        }
        System.out.println(circleSize);
        setBackground(Color.WHITE);
    }

    public void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintChildren(g);
        //draw big circle
        Ellipse2D circle = new Ellipse2D.Double(panelSize.width/2-(circleSize/2), panelSize.height/2-(circleSize/2), circleSize, circleSize);
        g2.setColor(new Color(0.0f,0.0f,0.0f, 1.0f));
        g2.setStroke(new BasicStroke(5f));
        g2.draw(circle);
        //calculate the size and position of the small circle
        int smallCircleSize = (int)(0.1 * circleSize);
        double angleInDegrees = -(hours*60 + minutes)/4;
        double angle = Math.toRadians(angleInDegrees);

        int smallCircleX = (int)(panelSize.width/2-((circleSize/2)*Math.sin(angle))) - smallCircleSize/2;
        int smallCircleY = (int)(panelSize.height/2-((circleSize/2)*Math.cos(angle))) - smallCircleSize/2;

        Ellipse2D smallCircle = new Ellipse2D.Double(smallCircleX, smallCircleY, smallCircleSize, smallCircleSize);
        g.setColor(new Color(0.0f,0.0f,0.0f, 1.0f));
        // fill in the circle
        g2.fill(smallCircle);
        g2.draw(smallCircle);
    }
}