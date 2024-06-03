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
        int aiChoice = JOptionPane.showOptionDialog(null, "Choose AI difficulty", "AI Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        gamePanel = new Panel(aiChoice);

        JFrame frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(gamePanel);
        gamePanel.setBounds(0, 0, gamePanel.getPreferredSize().width, gamePanel.getPreferredSize().height);

        frame.addKeyListener(this);
        frame.pack();
        frame.setSize(gamePanel.getPreferredSize().width, gamePanel.getPreferredSize().height + 100);
        frame.setVisible(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
