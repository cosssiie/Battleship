import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements KeyListener {

    public static void main(String[] args) {
        Main game = new Main();
    }

    private Panel gamePanel;

    public Main() {
        // Choose the AI Difficulty
        String[] options = new String[] {"Easy", "Medium", "Hard"};
        String message = "Easy will make moves entirely randomly,\nMedium will focus on areas where it finds ships,"
                + "\nand Hard will make smarter choices over Medium.";
        int difficultyChoice = JOptionPane.showOptionDialog(null, message,
                "Choose an AI Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        JFrame frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        gamePanel = new Panel(difficultyChoice);
        frame.getContentPane().add(gamePanel);

        frame.addKeyListener(this);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.handleInput(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
