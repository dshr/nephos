import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.border.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.awt.geom.Ellipse2D;
import javax.vecmath.Vector2d;

class MyDrawingPanel extends JPanel implements MouseMotionListener {

    static Dimension panelSize;
    static int circleSize;
    public static Calendar displayedTime;
    static Calendar currentTime;

    public static Point center;
    public static Point last;

    public MyDrawingPanel(Dimension size) {
        super();
        setFocusable(true);
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
        currentTime = Calendar.getInstance();
        displayedTime = Calendar.getInstance();
        last = new Point();
        center = new Point(panelSize.width/2, panelSize.height/2);
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
        //calculate the size and position of the indicator circle
        int displayedTimeCircleSize = (int)(0.1 * circleSize);
        double angleInDegrees = -(displayedTime.get(Calendar.HOUR_OF_DAY)*60 + displayedTime.get(Calendar.MINUTE))/4;
        double angle = Math.toRadians(angleInDegrees);

        int displayedTimeCircleX = (int)(panelSize.width/2-((circleSize/2)*Math.sin(angle))) - displayedTimeCircleSize/2;
        int displayedTimeCircleY = (int)(panelSize.height/2-((circleSize/2)*Math.cos(angle))) - displayedTimeCircleSize/2;

        Ellipse2D displayedTimeCircle = new Ellipse2D.Double(displayedTimeCircleX, displayedTimeCircleY, displayedTimeCircleSize, displayedTimeCircleSize);
        g.setColor(new Color(0.0f,0.0f,0.0f, 1.0f));
        // fill in the circle
        g2.fill(displayedTimeCircle);
        g2.draw(displayedTimeCircle);

        int currentTimeCircleSize = (int)(0.03 * circleSize);
        angleInDegrees = -(currentTime.get(Calendar.HOUR_OF_DAY)*60 + currentTime.get(Calendar.MINUTE))/4;
        angle = Math.toRadians(angleInDegrees);

        int currentTimeCircleX = (int)(panelSize.width/2-((circleSize/2)*Math.sin(angle))) - currentTimeCircleSize/2;
        int currentTimeCircleY = (int)(panelSize.height/2-((circleSize/2)*Math.cos(angle))) - currentTimeCircleSize/2;

        Ellipse2D currentTimeCircle = new Ellipse2D.Double(currentTimeCircleX, currentTimeCircleY, currentTimeCircleSize, currentTimeCircleSize);
        g.setColor(new Color(0.0f,0.0f,0.0f, 1.0f));
        // fill in the circle
        g2.fill(currentTimeCircle);
        g2.draw(currentTimeCircle);
    }

    public double CrossProduct(Vector2d v1, Vector2d v2)
    {
        return (v1.x * v2.y) - (v1.y * v2.x);
    }

    public void mouseMoved(MouseEvent e) {
        System.out.println("Dragged");
    }

    public void mouseDragged(MouseEvent e){ 
        Point current = e.getPoint();
        if(last == null)
        {
            last = current;
        }
        else
        {
            Vector2d currentVector = new Vector2d(center.x - current.x, center.y - current.y);
            Vector2d lastVector = new Vector2d(center.x - last.x, center.y - last.y);
            double angle = CrossProduct(lastVector, currentVector);

            if(angle > 1)
            {
                displayedTime.add(Calendar.HOUR_OF_DAY, 1);
                last = current;
            }
            if(angle < -1)
            {
                displayedTime.add(Calendar.HOUR_OF_DAY, -1);
                last = current;
            }


            repaint();
        }
    }
}