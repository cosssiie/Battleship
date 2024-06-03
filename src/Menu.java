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
    public  static JButton autoPlaceButton;

    public Menu(String name) {
        setSize(600, 600); // Початковий розмір вікна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрує вікно на екрані
        setResizable(false); // Заборонити зміну розміру вікна

        // Створюємо кастомну панель для відображення зображення
        ImagePanel imagePanel = new ImagePanel(IMAGE_PATH);
        imagePanel.setLayout(null); // Використовуємо абсолютне позиціонування

        // Створюємо кнопку "Start" з округленими кутами
        RoundedButton startButton = new RoundedButton("Start");
        startButton.setBackground(new Color(255, 255, 255, 250));
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setForeground(Color.BLACK);
        startButton.setBounds(200, 470, 200, 50); // Встановлюємо координати та розмір кнопки
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Легкий рівень гри почався!");
                startGame(0); // Запускаємо гру з легким рівнем
            }
        });

        // Додаємо ефект анімації при наведенні
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(192, 196, 197));// Змінюємо колір при наведенні
                startButton.setBounds(210, 475, 185, 42); // Змінюємо координати при наведенні
                startButton.setFont(new Font("Arial", Font.BOLD, 18));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(255, 255, 255, 250)); // Повертаємо початковий колір
                startButton.setBounds(200, 470, 200, 50); // Повернути кнопку на місце
                startButton.setFont(new Font("Arial", Font.BOLD, 20));
            }
        });

        // Додаємо кнопку на панель з зображенням
        imagePanel.add(startButton);

        // Додаємо панель з зображенням у вікно
        add(imagePanel);

        // Відтворюємо музику на фоні
        menuMusicClip = playBackgroundMusic(MENU_MUSIC_PATH);

        // Відображаємо вікно
        setVisible(true);
    }

    // Метод для відтворення музики на фоні
    private Clip playBackgroundMusic(String audioPath) {
        try {
            File audioFile = new File(audioPath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Безперервне відтворення
            clip.start();
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Метод для зупинки музики
    private void stopMusic(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    private void startGame(int difficultyChoice) {
        // Зупиняємо музику меню
        stopMusic(menuMusicClip);

        // Відтворюємо музику для гри
        Clip gameMusicClip = playBackgroundMusic(GAME_MUSIC_PATH);

        JFrame gameFrame = new JFrame("Battleship");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.setSize(680, 450); // Задаємо розмір для нового вікна гри
        gameFrame.setLocationRelativeTo(null); // Центруємо вікно
        JLabel easyLevel = new JLabel("Easy level");
        easyLevel.setBounds(270, 0, 200, 30);
        easyLevel.setFont(new Font("Arial", Font.BOLD, 24));
        Panel gamePanel = new Panel(difficultyChoice);
        gamePanel.setLayout(null); // Встановлюємо абсолютне позиціонування для gamePanel
        gameFrame.getContentPane().add(gamePanel);

        autoPlaceButton = new RoundedButton("Auto Place Ships");
        autoPlaceButton.setBackground(new Color(19, 80, 217, 179));
        autoPlaceButton.setFont(new Font("Arial", Font.BOLD, 20));
        autoPlaceButton.setForeground(Color.WHITE);
        autoPlaceButton.setBounds(400, 350, 200, 50);
        autoPlaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.autoPlaceShips();
            }
        });
        gamePanel.add(easyLevel);
        gamePanel.add(autoPlaceButton);
        gameFrame.setVisible(true);

        // Закрити меню після запуску гри
        this.dispose();
    }

    // Внутрішній клас для панелі із зображенням
    private class ImagePanel extends JPanel {
        private Image image;

        public ImagePanel(String imagePath) {
            this.image = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                // Малюємо зображення з масштабуванням
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // Кастомний клас для кнопки з округленими кутами
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

            // Малюємо фон кнопки
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 20 - радіус заокруглення

            // Малюємо текст кнопки
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
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20); // 20 - радіус заокруглення
            g2.dispose();
        }
    }

    public static void main(String[] args) {
         Menu game = new Menu("Морський бій");

    }
}
