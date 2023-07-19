import javax.swing.*;
import java.awt.*;

public class ScoreView extends JPanel {
    private int score;


    public ScoreView() {
        setPreferredSize(new Dimension(100, 100));
    }

    public void updateScore(int score) {
        this.score = score;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Serif", Font.PLAIN, 50));
        g.drawString(String.valueOf(score), getWidth() / 2, getHeight() / 2);
    }
}