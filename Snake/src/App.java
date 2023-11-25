import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        final int BOARD_WIDTH = 600;
        final int BOARD_HEIGHT = BOARD_WIDTH;
        
        // Window Creation
        JFrame frame = new JFrame("Snake Game by Farhan Jamil");
        frame.setVisible(true);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(BOARD_WIDTH, BOARD_HEIGHT);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();
    }
}