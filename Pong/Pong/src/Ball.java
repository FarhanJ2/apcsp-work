import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Ball extends Rectangle {
    Random rnd;
    int xVelocity;
    int yVelocity;
    int initialSpeed = 2;

    Ball(int x, int y, int width, int height) {
        super(x, y, width, height);
        rnd = new Random();
        int randomXDirection = rnd.nextInt(2);
        if (randomXDirection == 0) 
            randomXDirection--;
        setXDirection(randomXDirection * initialSpeed);

        int randomYDirection = rnd.nextInt(2);
        if (randomYDirection == 0) 
            randomYDirection--;
        setYDirection(randomYDirection * initialSpeed);
    }

    public void setXDirection(int randomXDirection) {
        xVelocity = randomXDirection;
    }

    public void setYDirection(int randomYDirection) {
        yVelocity = randomYDirection;
    }

    public void move() {
        x += xVelocity;
        y += yVelocity;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(x, y, height, width);
    }
}
