import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ScoreManager {
    private static final String SCORES_FILE = "scores.bin"; // plik do przechowywania wyników (do zmiany na obronie)
    private static final int MAX_SCORES = 10; // maksymalna liczba przechowywanych wyników

    public PriorityQueue<PlayerScore> scores; // kolejka priorytetowa do przechowywania wyników

    public ScoreManager() {
        scores = new PriorityQueue<>(Comparator.comparing(PlayerScore::getScore).reversed());
        loadScores();
    }

    // dodaje nowy wynik do listy (jeżeli jest w top 10)
    public void addScore(String name, int score) {
        if (isHighScore(score)) {
            scores.add(new PlayerScore(name, score));
            if (scores.size() > MAX_SCORES) {
                scores.poll(); // usuń najniższy wynik, jeśli jest więcej niż 10
            }
            saveScores();
        }
    }

    // wczytuje wyniki z pliku
    private void loadScores() {
        File file = new File(SCORES_FILE);

        // Sprawdzenie czy plik istnieje, jeżeli nie to stworzenie nowego
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    //System.out.println("File created: " + file.getName() + file.getAbsolutePath());
                } else {
                    //System.out.println("File already exists."+ file.getAbsolutePath());
                }
            } catch (IOException e) {
                //System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }

        // Przy wczytywaniu wyników należy założyć, że plik już istnieje
        try (FileInputStream fis = new FileInputStream(file)) {
            while (fis.available() > 0) {
                byte[] nameLenBytes = new byte[1];
                fis.read(nameLenBytes);
                int nameLen = Byte.toUnsignedInt(nameLenBytes[0]);
                byte[] nameBytes = new byte[nameLen];
                fis.read(nameBytes);
                String name = new String(nameBytes);

                byte[] scoreBytes = new byte[4];
                fis.read(scoreBytes);
                int score = ByteBuffer.wrap(scoreBytes).getInt();

                scores.add(new PlayerScore(name, score));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // zapisuje wyniki do pliku
    void saveScores() {
        try (FileOutputStream fos = new FileOutputStream(SCORES_FILE)) {
            for (PlayerScore playerScore : scores) {
                byte[] nameBytes = playerScore.getName().getBytes();
                fos.write(new byte[]{(byte) nameBytes.length});
                fos.write(nameBytes);
                fos.write(ByteBuffer.allocate(4).putInt(playerScore.getScore()).array());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // zwraca listę najlepszych wyników
    public List<PlayerScore> getTopScores() {
        List<PlayerScore> scoreList = new ArrayList<>(scores);
        scoreList.sort(Comparator.comparing(PlayerScore::getScore).reversed());
        return scoreList;
    }

    public boolean isHighScore(int score) {
        List<PlayerScore> topScores = getTopScores();


        if (topScores.size() < 10) {
            return true;
        }

        boolean b = score > topScores.get(topScores.size() - 1).getScore();
        return b;
    }
    
}
