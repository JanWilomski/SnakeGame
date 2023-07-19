import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private JComboBox<String> difficultySelector;
    private JButton playButton;
    private JButton exitButton;

    public MainMenu() {
        difficultySelector = new JComboBox<>(new String[] { "Easy", "Medium", "Hard" });
        playButton = new JButton("Play");
        exitButton = new JButton("Exit");

        playButton.addActionListener(e -> {
            String difficulty = (String) difficultySelector.getSelectedItem();
            int speed;
            if ("Easy".equals(difficulty)) {
                speed = 160;
            } else if ("Medium".equals(difficulty)) {
                speed = 120;
            } else {
                speed = 80;
            }

            SnakeModel model = new SnakeModel();
            SnakeView view = new SnakeView(model.getYsize(), model.getXsize());
            ScoreView scoreView = new ScoreView();
            SnakeController controller = new SnakeController(model, view, scoreView);
            controller.setSpeed(speed);
            controller.input();

            JFrame frame = new JFrame("Snake Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 1080);
            frame.setLayout(new BorderLayout());
            frame.add(view, BorderLayout.CENTER);
            frame.add(scoreView, BorderLayout.NORTH);
            frame.setVisible(true);

            view.setShowGrid(false);
            view.setIntercellSpacing(new Dimension(0, 0));

            controller.setSpeed(speed);
            controller.start();
            setVisible(false);
        });

        exitButton.addActionListener(e -> System.exit(0));

        setLayout(new GridLayout(3, 1));
        add(difficultySelector);
        add(playButton);
        add(exitButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
