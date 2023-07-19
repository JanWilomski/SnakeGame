import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

class SnakeView extends JTable {

    private GameOverViewListener gameOverViewListener;

    private int[][] previousBoard;
    private int ySize;
    private int xSize;

    int headX;
    int headY;

    private JButton playAgainButton;

    private Point previousHead;




    public SnakeView(int ySize, int xSize) {
        this.ySize = ySize;
        this.xSize = xSize;

        this.previousBoard = new int[ySize][xSize];

        playAgainButton = new JButton("Play again");
        playAgainButton.addActionListener(e -> {
            if (gameOverViewListener != null) {
                gameOverViewListener.onGameOver();
            }
        });
        playAgainButton.setVisible(false);
        add(playAgainButton);



        setModel(new DefaultTableModel(ySize, xSize));
        setMinimumSize(new Dimension(300, 300));
        setMaximumSize(new Dimension(1080, 1080));
        setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel panel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        if (previousBoard[row][column] == 2) {
                            g.setColor(Color.RED);
                            g.fillOval(getWidth()/8, getHeight()/8, getWidth()-(getWidth()/4), getHeight()-(getHeight()/4));
                        }
                    }
                };

                switch (previousBoard[row][column]) {
                    case 1:

                        if (headX == column && headY == row) {
                            panel.setBackground(Color.orange);
                        } else {
                            panel.setBackground(Color.yellow);
                        }
                        break;

                    default:
                        if ((row + column) % 2 == 0) {
                            panel.setBackground(Color.GREEN.darker());
                        } else {
                            panel.setBackground(Color.GREEN.darker().darker());
                        }
                        break;
                }
                return panel;
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int tableHeight = getHeight();
                int tableWidth = getWidth();

                int cellSize = Math.min(tableHeight / ySize, tableWidth / xSize);

                setRowHeight(cellSize);

                TableColumnModel columnModel = getColumnModel();

                for (int i = 0; i < xSize; i++) {
                    columnModel.getColumn(i).setPreferredWidth(cellSize);
                }

                revalidate();
                repaint();
            }
        });

    }



    public void updateView(int[][] currentBoard, Point head) {
        if (previousHead != null) {
            repaint(getCellRect(previousHead.y, previousHead.x, true));
        }
        headX = head.x;
        headY = head.y;
        previousHead = head;
        repaint(getCellRect(headY, headX, true));
        for (int y = 0; y < currentBoard.length; y++) {
            for (int x = 0; x < currentBoard[y].length; x++) {
                if (currentBoard[y][x] != previousBoard[y][x]) {
                    repaint(getCellRect(y, x, true));
                    previousBoard[y][x] = currentBoard[y][x];
                }
            }
        }
    }


    public void setPlayAgainListener(GameOverViewListener gameOverViewListener) {
        this.gameOverViewListener = gameOverViewListener;
    }

    public String askForPlayerName() {
        return JOptionPane.showInputDialog("Enter your name:");
    }



    public void showGameOver(String scoresMessage, boolean isHighScore) {
        if (isHighScore) {
            JOptionPane.showMessageDialog(null, scoresMessage, "Top Scores", JOptionPane.INFORMATION_MESSAGE);
        }
        playAgainButton.setVisible(true); // pokazujemy przycisk
    }

    public void showPlayAgainButton() {
        playAgainButton.setVisible(true);
        playAgainButton.setEnabled(true);
        this.revalidate();
        this.repaint();
    }
    public void hidePlayAgainButton() {
        playAgainButton.setVisible(false);
    }
    public String getPlayerName() {
        return JOptionPane.showInputDialog(this, "Enter your name:");
    }

}