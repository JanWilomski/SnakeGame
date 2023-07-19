import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RankingView extends JPanel {

    private JLabel title;
    private JTextArea rankingArea;

    public RankingView() {
        setLayout(new BorderLayout());

        title = new JLabel("Top Scores");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));

        rankingArea = new JTextArea();
        rankingArea.setEditable(false);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(rankingArea), BorderLayout.CENTER);
    }

    public void updateRanking(List<PlayerScore> topScores) {
        StringBuilder sb = new StringBuilder();
        for (PlayerScore score : topScores) {
            sb.append(score.getName()).append(": ").append(score.getScore()).append("\n");
        }
        rankingArea.setText(sb.toString());
    }
}

