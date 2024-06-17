import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * Клас для панелі з текстом
 *
 * */
public class StatusOfPanel extends JPanel {

    private final Font font = new Font("Arial", Font.BOLD, 19);
    private final String placingShipLine1 = "Place your Ships on the right!";
    private final String placingShipLine2 = "Z to rotate.";
    private static final String gameOverLossLine = "Game Over! You Lost :(";
    private static final String gameOverWinLine = "You won!";
    private static final String gameOverBottomLine = "Press R to restart.";
    private static JLabel topLine;
    private static JLabel bottomLine;
    private int panelWidth;
    private int panelHeight;
    private Menu menu;

    public StatusOfPanel(PositionXY position, int width, int height, Menu menu) {
        this.panelWidth = width;
        this.panelHeight = height;
        this.menu = menu;
        setLayout(null);
        setBackground(new Color(255, 255, 255));
        setBounds(position.x, position.y, width, height);

        topLine = new JLabel("", SwingConstants.CENTER);
        topLine.setBounds(0, 0, width, height / 2);
        topLine.setFont(font);
        add(topLine);

        bottomLine = new JLabel("", SwingConstants.CENTER);
        bottomLine.setBounds(0, height / 2, width, height / 4);
        bottomLine.setFont(font);
        add(bottomLine);

        reset();
    }

    public void reset() {
        setTopLine(placingShipLine1);
        setBottomLine(placingShipLine2);
        Menu.playSnakeButton.setVisible(false);
    }

    public static void showGameOver(boolean playerWon) {
        setTopLine(playerWon ? gameOverWinLine : gameOverLossLine);
        setBottomLine(playerWon ? "<html>Press R to restart</html>" : gameOverBottomLine);
        Menu.playSnakeButton.setVisible(true);
    }

    public static void setTopLine(String message) {
        topLine.setText(message);
    }

    public static void setBottomLine(String text) {
        bottomLine.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.setFont(font);
    }
}
