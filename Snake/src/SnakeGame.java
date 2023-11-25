import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;
        
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int BOARD_WIDTH = 600;
    int BOARD_HEIGHT = BOARD_WIDTH;
    final int TILESIZE = 25;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    Tile food;

    Random random;
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int BOARD_WIDTH, int BOARD_HEIGHT) {
        this.BOARD_WIDTH = BOARD_WIDTH;
        setPreferredSize(new Dimension(this.BOARD_WIDTH, this.BOARD_HEIGHT));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        PlaceFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this); // in miliseconds
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Draw(g);
    }

    public void Draw(Graphics g) {
        // DRAW GRID LINES (REMOVE LATER IF YOU WANT TO)
        // for (int i = 0; i < BOARD_WIDTH / TILESIZE; i++) {
        //     g.drawLine(i * TILESIZE, 0, i * TILESIZE, BOARD_HEIGHT);
        //     g.drawLine(0, i * TILESIZE, BOARD_WIDTH, i * TILESIZE);
        // }

        // DRAW FOOD
        g.setColor(Color.red);
        g.fill3DRect(food.x * TILESIZE, food.y * TILESIZE, TILESIZE, TILESIZE, true);

        // DRAW SNAKE
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * TILESIZE, snakeHead.y * TILESIZE, TILESIZE, TILESIZE, true);

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * TILESIZE, snakePart.y * TILESIZE, TILESIZE, TILESIZE, true);
        }

        // TEXT 
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), TILESIZE - 16, TILESIZE); // THE SCORE WILL BE CALCULATED BY THE SIZE OF THE SNAKE
        } else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), TILESIZE - 16, TILESIZE);
        }
    }

    public void PlaceFood() {
        food.x = random.nextInt(BOARD_WIDTH / TILESIZE);
        food.y = random.nextInt(BOARD_HEIGHT / TILESIZE);
    }

    public boolean Collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void Move() {
        // Eat Food Collision Check
        if (Collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            PlaceFood();
        }
        
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // SNAKE COLLIDES WITH BODY
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (Collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        // SNAKE COLLIDES WITH THE WALLS
        if (snakeHead.x * TILESIZE < 0 || snakeHead.x * TILESIZE > BOARD_WIDTH ||
            snakeHead.y * TILESIZE < 0 || snakeHead.y * TILESIZE > BOARD_HEIGHT) {
            gameOver = true;
        }
    }

    // Game Loop Like Unity Update
    @Override
    public void actionPerformed(ActionEvent e) {
        Move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }
    
    // KEY CHECKS
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (velocityY != 1) {
                    velocityX = 0;
                    velocityY = -1;
                } break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (velocityY != -1) {
                    velocityX = 0;
                    velocityY = 1;
                } break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (velocityX != 1) {
                    velocityX = -1;
                    velocityY = 0;
                } break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (velocityX != -1) {
                    velocityX = 1;
                    velocityY = 0;
                } break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}