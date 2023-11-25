import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH * (.5555)); // a ping pong table ratio is 5/9 so we multiply by the ratio and cast it as an int
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20; // 20 px
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = PADDLE_WIDTH * 4;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random rnd;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    GamePanel() {
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new ActionListener());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        rnd = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), rnd.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER); 
    }

    public void newPaddles() {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    public void paint(Graphics g) {
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    public void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    public void checkCollision() {
        // bounces ball off the edges of the screen
        if (ball.y <= 0)
            ball.setYDirection(-ball.yVelocity);
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER)
            ball.setYDirection(-ball.yVelocity);
        // bounces ball of of paddles
        if (ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if (ball.yVelocity > 0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        if (ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if (ball.yVelocity > 0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        

        // this should stop paddles to go off the screen
        if (paddle1.y <= 0) 
            paddle1.y = 0;
        if (paddle1.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) 
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;

        if (paddle2.y <= 0) 
            paddle2.y = 0;
        if (paddle2.y >= (GAME_HEIGHT - PADDLE_HEIGHT)) 
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;
        
        // Give player a point and creates a new ball
        // Player 2
        if (ball.x <= 0) {
            score.player2++;
            newPaddles();
            newBall();
            System.out.println("Current score of Player 2: " + score.player2);
        }
        // Player 1
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.player1++;
            newPaddles();
            newBall();
            System.out.println("Current score of Player 1: " + score.player1);
        }
    }
    // Game Loop Logic
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        // You could make an new running boolean later
        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    public class ActionListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
