import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.awt.Point;
import java.awt.image.BufferedImage;
public class Menu extends JFrame {

    private static final String MENU_MUSIC_PATH = "Battleship//mainMenuMusic.wav";
    private static final String GAME_MUSIC_PATH = "Battleship//gameMusic.wav";
    private static final String IMAGE_PATH = "Battleship//ship_photo.jpg";
    private static final String IMAGE_WAVES = "Battleship//waves.jpeg";
    private Clip menuMusicClip;
    private Panel gamePanel;
    public static JButton autoPlaceButton;
    public static JButton nextLevelButton;

    private JLabel levelLabel;
    private JLabel playerMovesLabel;
    private JLabel aiMovesLabel;
    private static final int MEDIUM_LEVEL_MAX_MOVES = 65;
    private int playerMoves;
    private int aiMoves;

    private Set<String> playerMovesSet;

    public Menu(String name) {
        setSize(600, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        ImagePanel imagePanel = new ImagePanel(IMAGE_PATH);
        imagePanel.setLayout(null);

        // Додавання кнопки для інструкції
        RoundedButton instructionButton = new RoundedButton("Інструкція");
        instructionButton.setBackground(new Color(255, 255, 255, 250));
        instructionButton.setFont(new Font("Arial", Font.BOLD, 20));
        instructionButton.setForeground(Color.BLACK);
        instructionButton.setBounds(200, 530, 200, 50);
        instructionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Instructions().setVisible(true);
            }
        });

        RoundedButton startButton = new RoundedButton("Start");
        startButton.setBackground(new Color(255, 255, 255, 250));
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setForeground(Color.BLACK);
        startButton.setBounds(200, 470, 200, 50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Легкий рівень гри почався!");
                easyLevel(0);
            }
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

        imagePanel.add(startButton);
        imagePanel.add(instructionButton);
        add(imagePanel);
        setVisible(true);
    }

    private void setBackgroundImage(String imagePath) {
        ImagePanel imagePanel = new ImagePanel(imagePath);
        setContentPane(imagePanel);
        imagePanel.setLayout(null);
        validate();
        repaint();
    }

    private void initLevel(JFrame gameFrame, String levelText, int difficultyChoice, int panelSizeX, int panelSizeY, int autoPlaceX, int autoPlaceY, int nextLevelX, int nextLevelY) {
        levelLabel = new JLabel(levelText);
        levelLabel.setBounds((gameFrame.getWidth() - 200) / 2, 15, 200, 30);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 24));

        gamePanel = new Panel(difficultyChoice, panelSizeX, panelSizeY);
        gamePanel.setLayout(null);
        gameFrame.getContentPane().add(gamePanel);

        autoPlaceButton = new RoundedButton("Auto Place Ships");
        autoPlaceButton.setBackground(new Color(19, 80, 217, 179));
        autoPlaceButton.setFont(new Font("Arial", Font.BOLD, 20));
        autoPlaceButton.setForeground(Color.WHITE);
        autoPlaceButton.setBounds(autoPlaceX, autoPlaceY, 200, 50);
        autoPlaceButton.addActionListener(e -> {
            System.out.println("Кнопка");
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

        nextLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Наступний рівень!");
                if (gamePanel != null) {
                    gameFrame.dispose();
                    if (difficultyChoice == 0) {
                        mediumLevel(1);
                    } else {
                        hardLevel(2);
                    }
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

        gameFrame.setVisible(true);
        this.dispose();
    }

    private void easyLevel(int difficultyChoice) {
        Selection.BOAT_SIZES = new int[]{5, 4, 3, 2, 2};
        setBackgroundImage("Battleship//waves.jpeg");
        stopMusic(menuMusicClip);
        JFrame gameFrame = new JFrame("Battleship");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(680, 570);
        gameFrame.setLocationRelativeTo(null);
        initLevel(gameFrame, "Easy Level", difficultyChoice, 10, 10, 400, 390, 400, 460);
    }

    private void mediumLevel(int difficultyChoice) {
        Selection.BOAT_SIZES = new int[]{5, 4, 3, 3, 2, 2, 2};
        setBackgroundImage("Battleship//waves.jpeg");
        JOptionPane.showMessageDialog(null, "Середній рівень гри почався! Кількість пострілів обмежена: " + MEDIUM_LEVEL_MAX_MOVES);
        JFrame gameFrame = new JFrame("Battleship");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(860, 650);
        gameFrame.setLocationRelativeTo(null);
        initLevel(gameFrame, "Medium Level", difficultyChoice, 13, 13, 550, 480, 550, 550);

        counterMoves();
    }

    private void hardLevel(int difficultyChoice) {
        Selection.BOAT_SIZES = new int[]{5, 4, 3, 3, 2, 2, 2, 2, 1, 1};
        setBackgroundImage("Battleship//waves.jpeg");
        JOptionPane.showMessageDialog(null, "Складний рівень гри почався!");
        JFrame gameFrame = new JFrame("Battleship");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(1060, 730);
        gameFrame.setLocationRelativeTo(null);
        gamePanel = new Panel(difficultyChoice, 15, 15); // Передаємо параметр складності
        gamePanel.setLayout(null);
        gameFrame.getContentPane().add(gamePanel);
        initLevel(gameFrame, "Hard Level", difficultyChoice, 15, 15, 600, 570, 600, 640);
    }


    //метод-лічильник кроків
    public void counterMoves(){
        playerMovesLabel = new JLabel("Player Moves: " + playerMoves);
        playerMovesLabel.setBounds(10, 10, 200, 30);
        playerMovesLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        aiMovesLabel = new JLabel("AI Moves: " + aiMoves);
        aiMovesLabel.setBounds(10, 40, 200, 30);
        aiMovesLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        gamePanel.add(playerMovesLabel);
        gamePanel.add(aiMovesLabel);

        // Ініціалізація кількості ходів
        playerMoves = 0;
        aiMoves = 0;
        playerMovesSet = new HashSet<>();

        // Додаємо слухачів для рахунку ходів
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point mousePosition = new Point(e.getX(), e.getY());
                String positionKey = mousePosition.x + "," + mousePosition.y;

                if (gamePanel.getGameState() == Panel.GameState.FiringShots && isPositionInside(mousePosition)) {
                    if (!playerMovesSet.contains(positionKey)) {
                        // Логіка для ходу гравця
                        if (playerMoves < MEDIUM_LEVEL_MAX_MOVES) {
                            playerMoves++;
                            playerMovesLabel.setText("Player Moves: " + playerMoves);
                            playerMovesSet.add(positionKey);

                            // Логіка для ходу AI
                            if (aiMoves < MEDIUM_LEVEL_MAX_MOVES) {
                                aiMoves++;
                                aiMovesLabel.setText("AI Moves: " + aiMoves);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Max moves reached. You lost!");
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "You cannot shoot at the same position twice!");
                    }
                }
            }
        });
    }

    public boolean isPositionInside(Point point) {
        return point.x >= 0 && point.x < gamePanel.getWidth() && point.y >= 0 && point.y < gamePanel.getHeight();
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

    private class RoundedButton extends JButton {
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

    public static void main(String[] args) {
        Menu game = new Menu("Морський бій");
    }
}
