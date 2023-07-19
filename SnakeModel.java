import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class SnakeModel {

    public static final int XSIZE = 26;
    public static final int YSIZE = 26;

    private static final int EMPTY = 0;
    public static final int SNAKE = 1;
    public static final int FRUIT = 2;
    private ArrayList<GameTickListener> gameTickListeners;
    private ArrayList<GameOverListener> gameOverListeners;
    private ArrayList<FoodEatenListener> foodEatenListeners;

    private final ArrayList<GameStatusListener> listeners = new ArrayList<>();

    private int score;

    private int[][] board;
    private LinkedList<Point> snakePoints;
    private Point fruit;
    private int direction;

    private int pendingDirection;




    public SnakeModel() {

        board = new int[YSIZE][XSIZE];
        snakePoints = new LinkedList<>();
        snakePoints.add(new Point(YSIZE / 2, XSIZE / 2));
        fruit = new Point((int) (Math.random() * YSIZE), (int) (Math.random() * XSIZE));
        direction = 0; // 0 - right, 1 - up, 2 - left, 3 - down

        gameTickListeners = new ArrayList<>();
        gameOverListeners = new ArrayList<>();
        foodEatenListeners = new ArrayList<>();


    }

    public void addGameStatusListener(GameStatusListener listener){
        listeners.add(listener);
    }

    public void addGameTickListener(GameTickListener listener) {
        gameTickListeners.add(listener);
    }

    public void addGameOverListener(GameOverListener listener) {
        gameOverListeners.add(listener);
    }

    public void addFoodEatenListener(FoodEatenListener listener) {
        foodEatenListeners.add(listener);
    }

    public int[][] getBoard() {
        return board;
    }

    public Point getFruit() {
        return fruit;
    }

    public void setDirection(int direction) {
        this.pendingDirection = direction;
    }

    public void advanceSnake() {
        // update snake position
        Point head = new Point(snakePoints.peekFirst());
        direction=pendingDirection;
        switch (direction) {
            case 0:
                head.x++;
                break;
            case 1:
                head.y--;
                break;
            case 2:
                head.x--;
                break;
            case 3:
                head.y++;
                break;
        }


        if (head.x < 0 || head.y < 0 || head.x >= XSIZE || head.y >= YSIZE || board[head.y][head.x] == SNAKE) {
            gameOverListeners.forEach(listener -> listener.gameOverEvent(score));
        }


        if (head.equals(fruit)) {
            do {
                fruit = new Point((int) (Math.random() * YSIZE), (int) (Math.random() * XSIZE));
            } while (board[fruit.y][fruit.x] != EMPTY);
            score++;
            foodEatenListeners.forEach(listener -> listener.foodEatenEvent(score));


        } else {
            Point tail = snakePoints.removeLast();
            board[tail.y][tail.x] = EMPTY;
        }



        snakePoints.addFirst(head);
        board[head.y][head.x] = SNAKE;
        board[fruit.y][fruit.x] = FRUIT;
        gameTickListeners.forEach(listener -> listener.gameTickEvent());
    }

    public Point getSnakeHead() {
        return snakePoints.get(0);
    }

    public void resetGame() {
        board = new int[YSIZE][XSIZE];
        snakePoints = new LinkedList<>();
        snakePoints.add(new Point(YSIZE / 2, XSIZE / 2));
        fruit = new Point((int) (Math.random() * YSIZE), (int) (Math.random() * XSIZE));
        direction = 0; // 0 - right, 1 - up, 2 - left, 3 - down
        score = 0;


        for (Point point : snakePoints) {
            board[point.y][point.x] = SNAKE;
        }
        board[fruit.y][fruit.x] = FRUIT;
    }

    public int getScore() {
        return score;
    }

    public int getXsize(){
        return XSIZE;
    }
    public int getYsize(){
        return YSIZE;
    }

    public int getDirection() {
        return direction;
    }

    public int getSnakeLength(){
        return snakePoints.size();
    }

    public LinkedList<Point> getSnakePoints() {
        return snakePoints;
    }
}
