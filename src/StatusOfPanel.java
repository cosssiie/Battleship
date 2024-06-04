import javax.swing.*;
import java.awt.*;

public class StatusOfPanel extends JPanel {

    private final Font font = new Font("Arial", Font.BOLD, 20);
    private final String placingShipLine1 = "Place your Ships on the right!";
    private final String placingShipLine2 = "Z to rotate.";
    private final String gameOverLossLine = "Game Over! You Lost :(";
    private final String gameOverWinLine = "You won!";
    private final String gameOverBottomLine = "Press R to restart.";
    private JLabel topLine;
    private JLabel bottomLine;

    public StatusOfPanel(PositionXY position, int width, int height) {
        setLayout(null);
        setBackground(new Color(255, 255, 255));
        setBounds(position.x, position.y, width, height);

        topLine = new JLabel("", SwingConstants.CENTER);
        topLine.setBounds(0, 0, width, height / 2);
        topLine.setFont(font);
        add(topLine);

        bottomLine = new JLabel("", SwingConstants.CENTER);
        bottomLine.setBounds(0, height / 2, width, height / 2);
        bottomLine.setFont(font);
        add(bottomLine);

        reset();
    }

    public void reset() {
        setTopLine(placingShipLine1);
        setBottomLine(placingShipLine2);
    }

    public void showGameOver(boolean playerWon) {
        setTopLine(playerWon ? gameOverWinLine : gameOverLossLine);
        setBottomLine(playerWon ? "<html>Press R to restart.<br>Press N to go to next level.</html>" : gameOverBottomLine);
    }

    public void setTopLine(String message) {
        topLine.setText(message);
    }

    public void setBottomLine(String text) {
        bottomLine.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.setFont(font);
    }
}
