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

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //making snake head
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food for snake to chase
    Tile food;
    Random rando;

    //definingsnake velocity
    int velocityX;
    int velocityY;
    Timer Loops;

    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        rando = new Random();
        placeFood();

        velocityX = 1;
        velocityY = 0;

        //game timer
        Loops = new Timer(100, this); //how long it takes to start timer, milliseconds gone between frames
        Loops.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        for(int i = 0; i < boardWidth/tileSize; i++) {

            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }


        g.setColor(Color.red);

        g.fill3DRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, true);


        g.setColor(Color.green);

        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);

        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }


        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood(){
        food.x = rando.nextInt(boardWidth/tileSize);
        food.y = rando.nextInt(boardHeight/tileSize);
    }

    public void move() {

        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //moving snake body
        for (int i = snakeBody.size()-1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnake = snakeBody.get(i-1);
                snakePart.x = prevSnake.x;
                snakePart.y = prevSnake.y;
            }
        }
        //moving  snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //loop for terminating
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);

            //collision method
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }

        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || //passed left border or right border
                snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) { //passed top border or bottom border
            gameOver = true;
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            Loops.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
