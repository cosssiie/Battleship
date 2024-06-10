import javax.swing.*;
import java.awt.*;

public class CustomMessage extends JDialog {
    private int fontSize;
    private String message;
    private Color backColor;
    private Color color;
    private int width;
    private int height;
    private int coordinateX;
    private int coordinateY;

    public CustomMessage(String message, int fontSize, Color backColor, Color color, int width, int height, int coordinateX, int coordinateY) {
        super();
        this.message = message;
        this.fontSize = fontSize;
        this.backColor = backColor;
        this.color = color;
        this.width = width;
        this.height = height;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        initialize();
    }

    private void initialize() {
        setSize(width, height);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(backColor);
        panel.setLayout(null);

        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setForeground(color);
        label.setBounds(coordinateX, coordinateY, width, 20);
        panel.add(label);

        JButton okayButton = new Menu.RoundedButton("Okay");
        okayButton.setBackground(new Color(5, 110, 180, 204));
        okayButton.setFont(new Font("Arial", Font.BOLD, 16));
        okayButton.setForeground(Color.WHITE);
        okayButton.setBounds(width / 2 - 50, height / 2, 100, 30);
        okayButton.addActionListener(e -> dispose());

        panel.add(okayButton);

        setContentPane(panel);
    }

    public void showMessageDialog() {
        setVisible(true);
    }
}
