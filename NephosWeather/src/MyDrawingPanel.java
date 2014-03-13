// package nephos;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.swing.border.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.awt.geom.Ellipse2D;
import javax.vecmath.Vector2d;


public class MyDrawingPanel extends JPanel implements MouseMotionListener{

    static Dimension panelSize;
    static int circleSize;
    public static Calendar displayedTime;
    public static Calendar currentTime;

    public static Point center;
    public static Point last;

    static Font fontBase;
    static int textSize;

    static MyJLabel tempText;

    Boolean isBig;
    NephosAPI weather;

    private void printSimpleString(String s, int width, int XPos, int YPos, Graphics2D g2){  
            int stringLen = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();  
            int start = width/2 - stringLen/2;  
            g2.drawString(s, start + XPos, YPos);  
    } 

    private int calculateHoursDiff(Calendar date1, Calendar date2){
        int diff=(int)((date1.getTimeInMillis() - date2.getTimeInMillis())/(60*60 * 1000));
        return diff;
    }

    public MyDrawingPanel(Boolean _isBig, Font f, int _textSize, MyJLabel _tempText, NephosAPI _weather) {
        super();
        setFocusable(true);
        isBig = _isBig;
        tempText = _tempText;
        weather = _weather;
        if(isBig)
        {
            panelSize = new Dimension(1024, 800);
        }
        else
        {
            panelSize = new Dimension(320, 480);
        }
        if(panelSize.width > panelSize.height)
        {
            circleSize = (int)(0.7 * panelSize.height);
        }
        else
        {
            circleSize = (int)(0.8 * panelSize.width);
        }
        // System.out.println(circleSize);
        setBackground(Color.WHITE);
        currentTime = Calendar.getInstance();
        displayedTime = Calendar.getInstance();
        last = new Point();
        int centerPointHeight = (int)((panelSize.height/2) * 1.05);
        center = new Point(panelSize.width/2, centerPointHeight);
        fontBase = f;
        textSize = _textSize;
		// View.isClock = true;
    }

    public void paintChildren(Graphics g) {
        currentTime = Calendar.getInstance();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintChildren(g);
        //draw big circle
        Ellipse2D circle = new Ellipse2D.Double(center.x-(circleSize/2), center.y-(circleSize/2), circleSize, circleSize);
        g2.setColor(new Color(0.0f,0.0f,0.0f, 1.0f));
        g2.setStroke(new BasicStroke(5f));
        g2.draw(circle);
        //calculate the size and position of the indicator circle
        int displayedTimeCircleSize = (int)(0.1 * circleSize);
        double angleInDegrees = -(displayedTime.get(Calendar.HOUR_OF_DAY)*60 + displayedTime.get(Calendar.MINUTE))/4;
        double angle = Math.toRadians(angleInDegrees);

        int displayedTimeCircleX = (int)(center.x-((circleSize/2)*Math.sin(angle))) - displayedTimeCircleSize/2;
        int displayedTimeCircleY = (int)(center.y-((circleSize/2)*Math.cos(angle))) - displayedTimeCircleSize/2;

        Ellipse2D displayedTimeCircle = new Ellipse2D.Double(displayedTimeCircleX, displayedTimeCircleY, displayedTimeCircleSize, displayedTimeCircleSize);
        g.setColor(new Color(0.0f,0.0f,0.0f, 1.0f));
        // fill in the circle
        g2.fill(displayedTimeCircle);
        g2.draw(displayedTimeCircle);

        int currentTimeCircleSize = (int)(0.03 * circleSize);
        angleInDegrees = -(currentTime.get(Calendar.HOUR_OF_DAY)*60 + currentTime.get(Calendar.MINUTE))/4;
        angle = Math.toRadians(angleInDegrees);

        int currentTimeCircleX = (int)(center.x-((circleSize/2)*Math.sin(angle))) - currentTimeCircleSize/2;
        int currentTimeCircleY = (int)(center.y-((circleSize/2)*Math.cos(angle))) - currentTimeCircleSize/2;

        Ellipse2D currentTimeCircle = new Ellipse2D.Double(currentTimeCircleX, currentTimeCircleY, currentTimeCircleSize, currentTimeCircleSize);
        g.setColor(new Color(0.0f,0.0f,0.0f, 1.0f));
        // fill in the circle
        g2.fill(currentTimeCircle);
        g2.draw(currentTimeCircle);

        SimpleDateFormat timeFormat = new SimpleDateFormat("EEEE dd/MM HH:mm");
        g.setFont(fontBase.deriveFont(Font.PLAIN, textSize));
        String date = new String(timeFormat.format(displayedTime.getTime()));
        int yPos = (int) (panelSize.height*0.18);
        if(isBig) yPos = (int) (panelSize.height*0.1); 
        printSimpleString(date, panelSize.width, 0, yPos, g2);

    }

    public double CrossProduct(Vector2d v1, Vector2d v2)
    {
        return (v1.x * v2.y) - (v1.y * v2.x);
    }

    public void mouseMoved(MouseEvent e) {
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
            int delta = 0;

            int timeDifference = calculateHoursDiff(displayedTime, currentTime);
            if((timeDifference > 48)||(timeDifference < 0))
            {
                delta = 0;
            }
            else
            {
                if(angle > 400)
                {
                    delta = 1;
                    if(timeDifference == 48) delta = 0;
                    last = current;
                }
                if(angle < -400)
                {
                    delta = -1;
                    if(timeDifference == 0) delta = 0;
                    last = current;
                }
                String newText = "<html>" +
                    "<center>" + 
                        "<span style=\"font-weight:bold;\">" + Integer.toString(weather.getTemperatureAtHour(timeDifference)) + (char)186 + "C</span>" +
                    "</center>"+ 
                "</html>";
                tempText.setText(newText);
            }
            displayedTime.add(Calendar.HOUR_OF_DAY, delta);
            repaint();
        }
    }
}

