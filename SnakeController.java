import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.stream.Collectors;

public class SnakeController implements Runnable, GameTickListener, GameOverListener, FoodEatenListener, PlayAgainListener {
    private SnakeModel model;
    private SnakeView view;
    private ScoreView scoreView;
    private ScoreManager scoreManager;

    private boolean running;
    public int speed=1;

    private boolean gameStarted=false;

    public SnakeController(SnakeModel model, SnakeView view, ScoreView scoreView) {
        this.model = model;
        this.view = view;
        this.scoreView = new ScoreView();
        this.running = false;
        this.scoreManager = new ScoreManager();

        model.addGameTickListener(this);
        model.addGameOverListener(this);
        model.addFoodEatenListener(this);

    }

    void input(){
        view.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        view.getActionMap().put("moveRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!gameStarted) {
                    gameStarted = true;
                    start();
                }
                if(model.getSnakeLength()==1){
                    model.setDirection(0);
                }else if(model.getDirection()!=2) {
                    model.setDirection(0);
                }
            }
        });
        view.getInputMap().put(KeyStroke.getKeyStroke("UP"), "moveUp");
        view.getActionMap().put("moveUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!gameStarted) {
                    gameStarted = true;
                    start();
                }
                if(model.getSnakeLength()==1){
                    model.setDirection(1);
                }else if(model.getDirection()!=3) {
                    model.setDirection(1);
                }
            }
        });
        view.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        view.getActionMap().put("moveLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!gameStarted) {
                    gameStarted = true;
                    start();
                }
                if(model.getSnakeLength()==1){
                    model.setDirection(2);
                }else if(model.getDirection()!=0) {
                    model.setDirection(2);
                }
            }
        });
        view.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        view.getActionMap().put("moveDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!gameStarted) {
                    gameStarted = true;
                    running=true;
                    start();
                }
                if(model.getSnakeLength()==1){
                    model.setDirection(3);
                }else if(model.getDirection()!=1) {
                    model.setDirection(3);
                }
            }
        });
    }



    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    public ScoreView getScoreView() {
        return scoreView;
    }

    public void run() {

        while(gameStarted) {
            try {
                model.advanceSnake();
                Thread.sleep(speed);
                scoreView.updateScore(model.getScore());
            } catch(InterruptedException e) {
                e.printStackTrace();
            } catch(RuntimeException e) {
                running = false;
                gameStarted = false;

            }
        }
    }



    @Override
    public void gameTickEvent() {
        view.updateView(model.getBoard(), model.getSnakeHead());
        scoreView.updateScore(model.getScore());
        //updateScore(model.getScore());

    }






    @Override
    public void gameOverEvent(int score) {
        stop();

        view.showPlayAgainButton();
        boolean isHighScore = scoreManager.isHighScore(score);
        if (isHighScore) {
            String playerName = view.getPlayerName();
            scoreManager.addScore(playerName, score);
            scoreManager.saveScores();
        }

        List<PlayerScore> topScores = scoreManager.getTopScores();
        String scoresMessage = topScores.stream()
                .map(ps -> ps.getName() + ": " + ps.getScore())
                .collect(Collectors.joining("\n"));
        view.showGameOver(scoresMessage, isHighScore);
        view.showPlayAgainButton();
    }




    @Override
    public void foodEatenEvent(int score) {
        scoreView.updateScore(score);
    }

    @Override
    public void onPlayAgain() {
        model.resetGame();
        view.hidePlayAgainButton();
        start();
    }
}

