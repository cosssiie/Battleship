import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {

    private static final String MENU_MUSIC_PATH = "Battleship/mainMenuMusic.wav";
    private static final String GAME_MUSIC_PATH = "Battleship/gameMusic.wav";
    private static final String IMAGE_PATH = "Battleship/ship_photo.jpg";
    private Clip menuMusicClip;
    private Clip gameMusicClip;
    private Panel gamePanel;
    public static JButton autoPlaceButton;
    public static JButton nextLevelButton;
    public static JButton playSnakeButton;
    private JLabel levelLabel;
    private int currentLevel;
    public JLabel playerMovesLabel;
    public JLabel aiMovesLabel;

    private static final int MEDIUM_LEVEL_MAX_MOVES = 65;
    private Timer gameTimer;
    private Timer displayTimer;
    private final int TIMER_DELAY = 180000; // 3 хвилини в мілісекундах
    private int timeRemaining = TIMER_DELAY / 1000; // Час, що залишився, в секундах
    private JLabel timerLabel;

    public Menu(String name) {
        setSize(600, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        menuMusicClip = playBackgroundMusic(MENU_MUSIC_PATH);
        ImagePanel imagePanel = new ImagePanel(IMAGE_PATH);
        imagePanel.setLayout(null);

        RoundedButton instructionButton = new RoundedButton("Інструкція");
        instructionButton.setBackground(new Color(255, 255, 255, 250));
        instructionButton.setFont(new Font("Arial", Font.BOLD, 20));
        instructionButton.setForeground(Color.BLACK);
        instructionButton.setBounds(200, 530, 200, 50);

        instructionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (this != null) {
                    CustomMessage customMessage = null;
                    CustomMessage finalCustomMessage = customMessage;
                    customMessage = new CustomMessage(
                            "Battleship/rules.png",
                            1245, 25, 1300, 80,
                            () -> {
                                finalCustomMessage.closeWindow();
                            }
                    );
                    customMessage.showWindow();
                }
            }
        });

        instructionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                instructionButton.setBackground(new Color(192, 196, 197));
                instructionButton.setBounds(210, 535, 185, 42);
                instructionButton.setFont(new Font("Arial", Font.BOLD, 18));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                instructionButton.setBackground(new Color(255, 255, 255, 250));
                instructionButton.setFont(new Font("Arial", Font.BOLD, 20));
                instructionButton.setBounds(200, 530, 200, 50);
            }
        });

        RoundedButton startButton = new RoundedButton("Start");
        startButton.setBackground(new Color(255, 255, 255, 250));
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setForeground(Color.BLACK);
        startButton.setBounds(200, 470, 200, 50);
        startButton.addActionListener(e -> {
            CustomMessage customMessage = new CustomMessage(
                    "Battleship/Easy.png",
                    100, 110, 235, 140,
                    this::easyLevel
            );
            customMessage.showWindow();
        });
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(192, 196, 197));
                startButton.setBounds(210, 475, 185, 42);
                startButton.setFont(new Font("Arial", Font.BOLD, 18));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(255, 255, 255, 250));
                startButton.setBounds(200, 470, 200, 50);
                startButton.setFont(new Font("Arial", Font.BOLD, 20));
            }
        });

        playSnakeButton = new RoundedButton("Play Snake");
        playSnakeButton.setBackground(new Color(9, 44, 82, 250));
        setPlaySnakeButtonPosition(currentLevel);
        playSnakeButton.setFont(new Font("Arial", Font.BOLD, 20));
        playSnakeButton.setForeground(Color.WHITE);
        playSnakeButton.setVisible(false);

        playSnakeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (this != null) {
                    CustomMessage customMessage = new CustomMessage(
                            "Battleship/10Points.png",
                            100, 110, 235, 140,
                            () -> {
                                playSnakeGame();
                            }
                    );
                    customMessage.showWindow();
                }
            }
        });

        imagePanel.add(startButton);
        imagePanel.add(instructionButton);
        add(imagePanel);
        setVisible(true);
    }

    private void setPlaySnakeButtonPosition(int level) {
        if (level == 0) { // Easy level
            playSnakeButton.setBounds(400, 460, 200, 50);
        } else if (level == 1) { // Medium level
            playSnakeButton.setBounds(550, 550, 200, 50);
        } else if (level == 2) { // Medium level
            playSnakeButton.setBounds(630, 620, 200, 50);
        }
    }

    private void initLevel(JFrame gameFrame, String levelText, int difficultyChoice, int panelSizeX, int panelSizeY, int autoPlaceX, int autoPlaceY, int nextLevelX, int nextLevelY) {
        stopMusic(menuMusicClip);
        gameMusicClip = playBackgroundMusic(GAME_MUSIC_PATH);

        levelLabel = new JLabel(levelText);
        levelLabel.setBounds((gameFrame.getWidth() / 2 - 150 / 2), 15, 200, 30);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 24));

        gamePanel = new Panel(difficultyChoice, panelSizeX, panelSizeY, this);
        gamePanel.setLayout(null);
        gameFrame.getContentPane().add(gamePanel);

        autoPlaceButton = new RoundedButton("Auto Place Ships");
        autoPlaceButton.setBackground(new Color(19, 80, 217, 179));
        autoPlaceButton.setFont(new Font("Arial", Font.BOLD, 20));
        autoPlaceButton.setForeground(Color.WHITE);
        autoPlaceButton.setBounds(autoPlaceX, autoPlaceY, 200, 50);
        autoPlaceButton.addActionListener(e -> {
            if (gamePanel != null) {
                gamePanel.autoPlaceShips();
            }
        });

        autoPlaceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                autoPlaceButton.setBackground(new Color(8, 49, 131, 182));
                autoPlaceButton.setFont(new Font("Arial", Font.BOLD, 18));
                autoPlaceButton.setBounds(autoPlaceX + 10, autoPlaceY + 5, 185, 42);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                autoPlaceButton.setBackground(new Color(19, 80, 217, 179));
                autoPlaceButton.setFont(new Font("Arial", Font.BOLD, 20));
                autoPlaceButton.setBounds(autoPlaceX, autoPlaceY, 200, 50);
            }
        });

        gamePanel.add(levelLabel);
        gamePanel.add(autoPlaceButton);

        nextLevelButton = new RoundedButton("Next Level");
        nextLevelButton.setBackground(new Color(9, 44, 82, 250));
        nextLevelButton.setFont(new Font("Arial", Font.BOLD, 20));
        nextLevelButton.setForeground(Color.WHITE);
        nextLevelButton.setBounds(nextLevelX, nextLevelY, 200, 50);
        nextLevelButton.setVisible(false);
        gamePanel.add(nextLevelButton);

        nextLevelButton.addActionListener(e -> {
            if (gamePanel != null) {
                gameFrame.dispose();
                if (difficultyChoice == 0) {
                    CustomMessage customMessage = new CustomMessage(
                            "Battleship/Medium.png",
                            100, 110, 235, 140,
                            () -> mediumLevel(1)
                    );
                    customMessage.showWindow();
                } else {
                    CustomMessage customMessage = new CustomMessage(
                            "Battleship/Hard.png",
                            100, 110, 235, 140,
                            () -> hardLevel(2)
                    );
                    customMessage.showWindow();
                }
            }
        });

        nextLevelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                nextLevelButton.setBackground(new Color(4, 29, 56, 250));
                nextLevelButton.setFont(new Font("Arial", Font.BOLD, 18));
                nextLevelButton.setForeground(Color.WHITE);
                nextLevelButton.setBounds(nextLevelX + 10, nextLevelY + 5, 185, 42);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                nextLevelButton.setBackground(new Color(9, 44, 82, 250));
                nextLevelButton.setFont(new Font("Arial", Font.BOLD, 20));
                nextLevelButton.setBounds(nextLevelX, nextLevelY, 200, 50);
            }
        });

        setPlaySnakeButtonPosition(currentLevel);
        playSnakeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                playSnakeButton.setBackground(new Color(4, 29, 56, 250));
                playSnakeButton.setFont(new Font("Arial", Font.BOLD, 18));
                playSnakeButton.setForeground(Color.WHITE);
                playSnakeButton.setBounds(nextLevelX + 10, nextLevelY + 5, 185, 42);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                playSnakeButton.setBackground(new Color(9, 44, 82, 250));
                playSnakeButton.setFont(new Font("Arial", Font.BOLD, 20));
                playSnakeButton.setBounds(nextLevelX, nextLevelY, 200, 50);
            }
        });

        gamePanel.add(playSnakeButton);

        gameFrame.setVisible(true);
        this.dispose();
    }

    private void easyLevel() {
        currentLevel = 0;
        stopMusic(gameMusicClip);
        Selection.BOAT_SIZES = new int[]{2, 2, 3, 4, 5};
        JFrame gameFrame = new JFrame("Battleship");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(680, 570);
        gameFrame.setLocationRelativeTo(null);

        initLevel(gameFrame, "Easy Level", 0, 10, 10, 400, 390, 400, 460);
    }

    private void mediumLevel(int difficultyChoice) {
        currentLevel = 1;
        stopMusic(gameMusicClip);
        Selection.BOAT_SIZES = new int[]{2, 2, 3, 3, 4, 5};
        JFrame gameFrame = new JFrame("Battleship");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(860, 650);
        gameFrame.setLocationRelativeTo(null);
        initLevel(gameFrame, "Medium Level", difficultyChoice, 13, 13, 550, 480, 550, 550);
        counterMoves();
    }

    public void counterMoves() {
        playerMovesLabel = new JLabel("Player Moves: " + Selection.playerMovesCount);
        playerMovesLabel.setBounds(10, 10, 200, 30);
        playerMovesLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        aiMovesLabel = new JLabel("AI Moves: " + Selection.AIMovesCount);
        aiMovesLabel.setBounds(10, 40, 200, 30);
        aiMovesLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        gamePanel.add(playerMovesLabel);
        gamePanel.add(aiMovesLabel);

        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playerMovesLabel.setText("Player Moves: " + Selection.playerMovesCount);
                aiMovesLabel.setText("AI Moves: " + Selection.AIMovesCount);

                if (Selection.playerMovesCount >= MEDIUM_LEVEL_MAX_MOVES || Selection.AIMovesCount >= MEDIUM_LEVEL_MAX_MOVES) {
                    Panel.gameState = Panel.GameState.GameOver;
                    Menu.playSnakeButton.setVisible(true);
                    Panel.statusPanel.showGameOver(false);

                    CustomMessage customMessage = new CustomMessage(
                            "Battleship/Limit.png",
                            100, 110, 235, 140,
                            () -> playSnakeGame()
                    );
                }
            }
        });
    }

    private void hardLevel(int difficultyChoice) {
        currentLevel = 2;
        stopMusic(gameMusicClip);
        Selection.BOAT_SIZES = new int[]{2, 2, 3, 3, 4, 4, 5};
        JFrame gameFrame = new JFrame("Battleship");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(990, 730);
        gameFrame.setLocationRelativeTo(null);
        gamePanel = new Panel(difficultyChoice, 15, 15, this);
        gamePanel.setLayout(null);
        gameFrame.getContentPane().add(gamePanel);

        CloudAnimation cloudAnimation = new CloudAnimation();
        cloudAnimation.setBounds(0, 0, gameFrame.getWidth(), gameFrame.getHeight());
        gameFrame.add(cloudAnimation);

        // Ініціалізація таймера
        initializeTimer();

        // Створення та додавання JLabel для таймера
        timerLabel = new JLabel("Час: " + formatTime(timeRemaining));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Додавання JLabel до gameFrame
        gameFrame.setLayout(new BorderLayout());
        gameFrame.add(timerLabel, BorderLayout.NORTH);
        gameFrame.add(gamePanel, BorderLayout.CENTER);

        initLevel(gameFrame, "Hard Level", difficultyChoice, 15, 15, 630, 550, 630, 620);

        // Додаємо слухача для завершення рівня
        gamePanel.addGameCompletionListener(() -> showCompletionAnimation());
        gameFrame.setVisible(true);
        this.dispose();
    }

    public void playSnakeGame() {
        stopMusic(gameMusicClip);
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        frame.pack();
        snakeGame.requestFocus();

        snakeGame.addGameCompletionListener(new SnakeGame.GameCompletionListener() {
            @Override
            public void onGameWin() {
                frame.dispose();
                if (currentLevel == 0) {
                    mediumLevel(1);
                } else if (currentLevel == 1) {
                    hardLevel(2);
                } else if (currentLevel == 2) {
                    hardLevel(2);
                }
            }

            @Override
            public void onGameOver() {
                frame.dispose();
                CustomMessage customMessage = new CustomMessage(
                        "Battleship/YouLost.png",
                        100, 110, 235, 140,
                        () -> {
                            setVisible(true);
                        }
                );
                customMessage.showWindow();
            }
        });
    }

    public static Clip playBackgroundMusic(String audioPath) {
        try {
            File audioFile = new File(audioPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Clip playSounds(String audioPath) {
        try {
            File audioFile = new File(audioPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void stopMusic(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    private class ImagePanel extends JPanel {
        private Image image;

        public ImagePanel(String imagePath) {
            this.image = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            FontMetrics fm = g2.getFontMetrics();
            Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
            int textX = (getWidth() - stringBounds.width) / 2;
            int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), textX, textY);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.dispose();
        }
    }

    private void showCompletionAnimation() {
        JFrame animationFrame = new JFrame("Вітаємо! Ви пройшли всі рівні!");
        AnimationPanel animationPanel = new AnimationPanel("Battleship\\7DZz.gif");
        animationFrame.add(animationPanel);
        animationFrame.pack();
        animationFrame.setLocationRelativeTo(null);
        animationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        animationFrame.setVisible(true);
    }

    // Метод для ініціалізації таймера
    private void initializeTimer() {
        // Таймер для завершення гри
        gameTimer = new Timer(TIMER_DELAY, e -> endGame());
        gameTimer.setRepeats(false);
        gameTimer.start();

        // Таймер для оновлення відображення часу
        displayTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                timerLabel.setText("Час: " + formatTime(timeRemaining));
            }
        });
        displayTimer.start();
    }

    // Метод для завершення гри
    private void endGame() {
        gameTimer.stop();
        displayTimer.stop();

        CustomMessage customMessage = new CustomMessage(
                "Battleship/timePassed.png",
                100, 110, 235, 140,
                () -> System.exit(0)
        );
        customMessage.showWindow();

    }

    // Метод для форматування часу в хвилини:секунди
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    public static void main(String[] args) {
        Menu game = new Menu("Морський бій");
    }
}
