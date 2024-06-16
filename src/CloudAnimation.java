import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;


public class CloudAnimation extends JComponent {
    private ArrayList<Cloud> clouds;

    public CloudAnimation() {
        clouds = new ArrayList<>();

        int xRight = -600;
        int yRight = new Random().nextInt(20);
        int speedRight = 2 + new Random().nextInt(2);
        clouds.add(new Cloud("Battleship//cloud.png", xRight, yRight, -speedRight));

        Timer cloudTimer = new Timer(30, e -> {
            for (Cloud cloud : clouds) {
                cloud.move();
            }
            repaint();
        });
        cloudTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Cloud cloud : clouds) {
            cloud.draw(g);
        }
    }

    private static class Cloud {
        private int x, y, speed;
        private Image image;

        public Cloud(String imagePath, int x, int y, int speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.image = new ImageIcon(imagePath).getImage();
        }

        public void move() {
            if (x < -200) {
                x += 1;
            } else {
                speed = 0;
            }
        }

        public void draw(Graphics g) {
            g.drawImage(image, x, y, null);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cloud Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        CloudAnimation cloudAnimation = new CloudAnimation();
        frame.add(cloudAnimation);

        frame.setVisible(true);
    }
}
