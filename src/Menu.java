import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Menu extends JFrame {

    private static final String MENU_MUSIC_PATH = "Battleship//mainMenuMusic.wav";
    private static final String GAME_MUSIC_PATH = "Battleship//gameMusic.wav";
    private static final String IMAGE_PATH = "Battleship//ship_photo.jpg";
    private Clip menuMusicClip;
    private Panel gamePanel;
    public static JButton autoPlaceButton;
    public static JButton nextLevelButton;

    public Menu(String name) {
        setSize(600, 600); // Зменшимо розмір вікна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        ImagePanel imagePanel = new ImagePanel(IMAGE_PATH);
        imagePanel.setLayout(null);

        RoundedButton startButton = new RoundedButton("Start");
        startButton.setBackground(new Color(255, 255, 255, 250));
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setForeground(Color.BLACK);
        startButton.setBounds(200, 470, 200, 50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Легкий рівень гри почався!");
                startGame(0);
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

        nextLevelButton = new RoundedButton("Next Level");
        nextLevelButton.setBackground(new Color(255, 255, 255, 250));
        nextLevelButton.setFont(new Font("Arial", Font.BOLD, 20));
        nextLevelButton.setForeground(Color.BLACK);
        nextLevelButton.setBounds(200, 530, 200, 50);
        nextLevelButton.setVisible(false);
        nextLevelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Наступний рівень!");
                if (gamePanel != null) {
                    gamePanel.nextLevel();
                }
            }
        });

        imagePanel.add(nextLevelButton);

        add(imagePanel);

        menuMusicClip = playBackgroundMusic(MENU_MUSIC_PATH);

        setVisible(true);
    }

    private Clip playBackgroundMusic(String audioPath) {
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

    private void stopMusic(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    private void startGame(int difficultyChoice) {
        stopMusic(menuMusicClip);
        Clip gameMusicClip = playBackgroundMusic(GAME_MUSIC_PATH);

        JFrame gameFrame = new JFrame("Battleship");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(680, 500); // Зменшимо розмір вікна гри
        gameFrame.setLocationRelativeTo(null);
        JLabel easyLevel = new JLabel("Easy level");
        easyLevel.setBounds(270, 0, 200, 30);
        easyLevel.setFont(new Font("Arial", Font.BOLD, 24));
        gamePanel = new Panel(difficultyChoice);
        gamePanel.setLayout(null);
        gameFrame.getContentPane().add(gamePanel);

        autoPlaceButton = new RoundedButton("Auto Place Ships");
        autoPlaceButton.setBackground(new Color(19, 80, 217, 179));
        autoPlaceButton.setFont(new Font("Arial", Font.BOLD, 20));
        autoPlaceButton.setForeground(Color.WHITE);
        autoPlaceButton.setBounds(400, 400, 200, 50); // Піднімаємо кнопку вище
        autoPlaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.autoPlaceShips();
            }
        });
        gamePanel.add(easyLevel);
        gamePanel.add(autoPlaceButton);
        gameFrame.setVisible(true);

        this.dispose();
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
