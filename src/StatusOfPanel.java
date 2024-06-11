import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatusOfPanel extends JPanel {

    private final Font font = new Font("Arial", Font.BOLD, 19);
    private final String placingShipLine1 = "Place your Ships on the right!";
    private final String placingShipLine2 = "Z to rotate.";
    private final String gameOverLossLine = "Game Over! You Lost :(";
    private final String gameOverWinLine = "You won!";
    private final String gameOverBottomLine = "Press R to restart.";
    private JLabel topLine;
    private JLabel bottomLine;
    private JButton playSnakeButton;
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

        playSnakeButton = new JButton("Play Snake");
        playSnakeButton.setBounds((width - 150) / 2, (height / 4) * 3, 150, 40);
        playSnakeButton.setFont(new Font("Arial", Font.BOLD, 16));
        playSnakeButton.setVisible(false);
        playSnakeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menu != null) {
                    menu.playSnakeGame();
                }
            }
        });
        add(playSnakeButton);

        reset();
    }

    public void reset() {
        setTopLine(placingShipLine1);
        setBottomLine(placingShipLine2);
        playSnakeButton.setVisible(false);
    }

    public void showGameOver(boolean playerWon) {
        setTopLine(playerWon ? gameOverWinLine : gameOverLossLine);
        setBottomLine(playerWon ? "<html>Press R to restart</html>" : gameOverBottomLine);
        playSnakeButton.setVisible(true);
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
